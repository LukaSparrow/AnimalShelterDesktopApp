package studia.animalshelterdesktopapp;

import studia.animalshelterdesktopapp.DAO.AnimalDAO;
import studia.animalshelterdesktopapp.DAO.AnimalShelterDAO;
import studia.animalshelterdesktopapp.DAO.RatingDAO;
import studia.animalshelterdesktopapp.controllers.CSVUtil;
import studia.animalshelterdesktopapp.controllers.SerializationUtil;

import java.io.IOException;
import java.util.*;

public class ShelterManager {
    private AnimalShelterDAO shelterDAO;
    private AnimalDAO animalDAO;
    private RatingDAO ratingDAO;

    public ShelterManager() {
        this.shelterDAO = new AnimalShelterDAO();
        this.animalDAO = new AnimalDAO();
        this.ratingDAO = new RatingDAO();
    }

    public void addShelter(AnimalShelter shelter) {
        try {
            if(ValidationService.validate(shelter)) {
                this.shelterDAO.insert(shelter);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void removeShelter(String name) {
        AnimalShelter shelter = shelterDAO.getAll()
                .stream()
                .filter(x -> x.getShelterName().toLowerCase().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Shelter not found: " + name));
        shelterDAO.delete(shelter);
    }

    public AnimalShelter getShelter(String name) {
        return shelterDAO.getAll()
                .stream()
                .filter(shelter -> shelter.getShelterName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public AnimalShelter getShelter(Long id) {
        return shelterDAO.get(id);
    }

    public List<AnimalShelter> getAllShelters() {
        return this.shelterDAO.getAll();
    }

    public List<AnimalShelter> findEmptyShelters() {
        return shelterDAO.getAll()
                .stream()
                .filter(shelter -> shelter.getAnimalList().isEmpty())
                .toList();
    }

    public void summary(){
        System.out.println(" ");
        shelterDAO.getAll().forEach(shelter -> {
            System.out.println("Shelter: " + shelter.getShelterName());
            System.out.println("Percentage fill " + (shelter.getAnimalList().size() * 100) / shelter.getMaxCapacity() + "%");
        });
    }

    public void updateShelter(AnimalShelter shelter, AnimalShelter updatedShelter) {
        AnimalShelter original = shelterDAO.get(shelter.getId());

        if(original.getAnimalList().stream().count() > updatedShelter.getMaxCapacity()) {
            System.err.println("Shelter overflow");
            return;
        }

        original.setShelterName(updatedShelter.getShelterName());
        original.setMaxCapacity(updatedShelter.getMaxCapacity());

        shelterDAO.update(original);
    }

    public void deleteAllShelters() {
        List<AnimalShelter> shelters = shelterDAO.getAll();
        shelters.forEach(shelter -> shelterDAO.delete(shelter));
    }

    public Long getShelterIdByName(String name) {
        return shelterDAO.getAll()
                .stream()
                .filter(shelter -> shelter.getShelterName().equals(name))
                .findFirst()
                .map(AnimalShelter::getId)
                .orElse(null);
    }

    public List<AnimalShelter> findSheltersByName(String filter) {
        return shelterDAO.findByName(filter);
    }

    public List<AnimalShelter> getAllSheltersWithAnimalsAndRatings() {
        return shelterDAO.getAllSheltersWithAnimalsAndRatings();
    }

    public List<Object[]> getShelterRatingsSummary() {
        return shelterDAO.getShelterRatingsSummaryUsingCriteria();
    }

    // Obsluga serwisowa
    public void addAnimalToShelter(Long shelterId, Animal animal) {
        try {
            AnimalShelter shelter = shelterDAO.findById(shelterId);
            if (shelter == null) {
                throw new IllegalArgumentException("Shelter with ID " + shelterId + " not found.");
            }

            if (shelter.getAnimalList().size() >= shelter.getMaxCapacity()) {
                throw new IllegalStateException("Shelter is full, cannot add more animals.");
            }

            boolean exists = shelter.getAnimalList().stream()
                    .anyMatch(a -> a.getAnimalName().equals(animal.getAnimalName())
                            && a.getAnimalSpecies().equals(animal.getAnimalSpecies())
                            && a.getAnimalAge() == animal.getAnimalAge());
            if (exists) {
                throw new IllegalArgumentException("Animal already exists in the shelter.");
            }

            animal.setShelter(shelter);

            animalDAO.insert(animal);

            shelter.getAnimalList().add(animal);
            shelterDAO.update(shelter);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add animal to shelter: " + e.getMessage());
        }
    }

    public void updateAnimalInShelter(Long shelterId, Long animalId, Animal updatedAnimal) {
        try {
            AnimalShelter shelter = shelterDAO.findById(shelterId);
            if (shelter == null) {
                throw new IllegalArgumentException("Shelter with ID " + shelterId + " not found.");
            }

            Animal animal = animalDAO.get(animalId);
            if (animal == null) {
                throw new IllegalArgumentException("Animal with ID " + animalId + " not found.");
            }

            if (!shelter.getAnimalList().stream().anyMatch(x -> x.getId().equals(animalId))) {
                throw new IllegalArgumentException("Animal with ID " + animalId + " is not in the shelter.");
            }

            animal.setAnimalName(updatedAnimal.getAnimalName());
            animal.setAnimalSpecies(updatedAnimal.getAnimalSpecies());
            animal.setAnimalAge(updatedAnimal.getAnimalAge());
            animal.setAnimalPrice(updatedAnimal.getAnimalPrice());
            animal.setAnimalCondition(updatedAnimal.getAnimalCondition());

            animalDAO.update(animal);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update animal in shelter: " + e.getMessage());
        }
    }

    public void removeAnimalFromShelter(Long shelterId, Long animalId) {
        try {
            AnimalShelter shelter = shelterDAO.findById(shelterId);
            if (shelter == null) {
                throw new IllegalArgumentException("Shelter with ID " + shelterId + " not found.");
            }

            Animal animal = animalDAO.get(animalId);
            if (animal == null) {
                throw new IllegalArgumentException("Animal with ID " + animalId + " not found.");
            }

            if (!shelter.getAnimalList().removeIf(x -> x.getId().equals(animalId))) {
                throw new IllegalArgumentException("Animal with ID " + animalId + " is not in the shelter.");
            }

            animal.setShelter(null);
            animalDAO.update(animal);

            shelterDAO.update(shelter);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to remove animal from shelter: " + e.getMessage());
        }
    }

    public List<Animal> findAnimalsInShelter(Long shelterId, String filter, AnimalCondition condition) {
        if(filter == null && condition == null) {
            return animalDAO.getAllInShelter(shelterId);
        } else {
            return animalDAO.getAllInShelter(shelterId, filter, condition);
        }
    }

    public boolean adoptAnimal(Long shelterId, Long animalId) {
        try {
            AnimalShelter shelter = shelterDAO.findById(shelterId);
            if (shelter == null) {
                System.err.println("Shelter with ID " + shelterId + " not found.");
            }

            Animal animal = animalDAO.get(animalId);
            if (animal == null) {
                System.err.println("Animal with ID " + animalId + " not found.");
            }

            if (!shelter.getAnimalList().stream().anyMatch(x -> x.getId().equals(animalId))) {
                System.err.println("Animal with ID " + animalId + " is not in the shelter.");
            }

            if (animal.getAnimalCondition() == AnimalCondition.W_TRAKCIE_ADOPCJI) {
                return false;
            } else {
                animal.setAnimalCondition(AnimalCondition.W_TRAKCIE_ADOPCJI);
                animalDAO.update(animal);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to adopt animal: " + e.getMessage());
        }
    }

    public void addRating(AnimalShelter shelter, int value, String comment) {
        Rating rating = new Rating(value, comment);
        rating.setShelter(shelter);
        rating.setValue(value);
        rating.setComment(comment);
        ratingDAO.insert(rating);
    }

    public List<Rating> getRatingsForShelter(AnimalShelter shelter) {
        return ratingDAO.getAll().stream()
                .filter(rating -> rating.getShelter().equals(shelter))
                .toList();
    }

    public void deleteRating(Long id) {
        Rating rating = ratingDAO.get(id);
        if(rating != null)
            ratingDAO.delete(rating);
    }

    public void serializeShelters() throws IOException {
        SerializationUtil.serializeShelters(this.getAllShelters(), "shelters.bin");
    }

    public void deserializeShelters() throws IOException {
        List<AnimalShelter> shelters = SerializationUtil.deserializeShelters("shelters.bin");
        this.deleteAllShelters();
        Map<Long, Long> shelterMap = new HashMap<>();

        if(shelters != null) {
            for(AnimalShelter shelter : shelters) {
                AnimalShelter newShelter = new AnimalShelter(shelter.getShelterName(), shelter.getMaxCapacity());
                this.addShelter(newShelter);

                Long newShelterId = this.getShelterIdByName(newShelter.getShelterName());
                shelterMap.put(shelter.getId(), newShelterId);
            }

            for(AnimalShelter shelter : shelters) {
                Long newShelterId = shelterMap.get(shelter.getId());
                for(Animal animal : shelter.getAnimalList()) {
                    this.addAnimalToShelter(newShelterId, animal);
                }
                for(Rating rating : shelter.getRatings()) {
                    this.addRating(this.getShelter(newShelterId), rating.getValue(), rating.getComment());
                }
            }
        }
    }

    public void exportSheltersToCSV() throws IOException {
        CSVUtil.exportSheltersToCSV(this.getAllSheltersWithAnimalsAndRatings());
    }

    public void importSheltersFromCSV() throws IOException {
        List<AnimalShelter> shelters = CSVUtil.importSheltersFromCSV("shelters.csv", "animals.csv", "ratings.csv");
        this.deleteAllShelters();
        if(shelters != null) {
            for(AnimalShelter shelter : shelters) {
                this.addShelter(shelter);
            }
        }
    }
}
