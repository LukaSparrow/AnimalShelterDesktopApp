package studia.animalshelterdesktopapp;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.compare;

public class AnimalShelter implements Printable {
    private String shelterName;
    private List<Animal> animalList;
    private int maxCapacity;
    private int animalCount;
    private double filling;

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.animalList = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.animalCount = 0;
        this.filling = 0;
    }

    public static Comparator<AnimalShelter> nameComparator = (s1, s2) -> s1.getShelterName().compareToIgnoreCase(s2.getShelterName());
    public static Comparator<AnimalShelter> capacityComparator = (s1, s2) -> Integer.compare(s1.getMaxCapacity(), s2.getMaxCapacity());
    public static Comparator<AnimalShelter> fillingComparator = (s1, s2) -> Double.compare(s2.getFilling(), s1.getFilling());


    @Override
    public void print() {
        System.out.printf("Schronisko o nazwie %s, o maksymalnej pojemnosci %d, przechowujace %d zwierzat.\n", this.shelterName, this.maxCapacity, this.animalCount);
    }

    public void updateFilling() {
        this.filling = (double) this.animalCount / (double) this.maxCapacity;
    }

    public boolean addAnimal(Animal animal) {
        if(this.animalCount >= this.maxCapacity) {
            System.err.println("Brak miejsc w schronisku.");
            return false;
        }
        for (Animal loopAnimal : animalList) {
            if (loopAnimal.compareTo(animal) == 0) {
                System.err.println("Zwierze juz znajduje sie w schronisku.");
                return false;
            }
        }
        ++this.animalCount;
        updateFilling();
        this.animalList.add(animal);
        return true;
    }

    public void removeAnimal(Animal animal) {
        this.animalList.remove(animal);
    }

    public Animal getAnimal(String animalName) {
        --this.animalCount;
        updateFilling();
        Animal searchedAnimal = search(animalName);
        changeCondition(searchedAnimal, AnimalCondition.W_TRAKCIE_ADOPCJI);
        animalList.remove(searchedAnimal);
        return searchedAnimal;
    }

    public void changeCondition(Animal animal, AnimalCondition condition) {
        animal.setAnimalCondition(condition);
    }

    public void changeAge(Animal animal, int age) {
        animal.setAnimalAge(age);
    }

    public int countByCondition(AnimalCondition condition) {
        return (int) this.animalList.stream().filter(animal -> animal.getAnimalCondition() == condition).count();
    }

    public void sortByName() {
        //List<Animal> sortedAnimalList = new ArrayList<>(this.animalList);

        Collections.sort(animalList, Animal::compareNameTo);

        //return sortedAnimalList;
    }

    public void sortByPrice() {
        //List<Animal> sortedAnimalList = new ArrayList<>(this.animalList);

        Collections.sort(animalList, Animal::comparePriceTo);

        //return sortedAnimalList;
    }

    public Animal search(String animalName) {
        List<Animal> sortedAnimalList = new ArrayList<>(this.animalList);
        sortedAnimalList.sort(Animal::compareNameTo);

        int index = Collections.binarySearch(sortedAnimalList, new Animal(animalName, null,null,0,0), Animal::compareNameTo);

        if(index >= 0) {
            return sortedAnimalList.get(index);
        }
        System.out.println("Nie znaleziono zwierzecia o takim imieniu.");
        return null;
    }

    public List<Animal> searchPartial(String textFragment) {
        //return animalList.stream().filter(animal -> animal.getName().contains(textFragment) || animal.getSpecies().contains(textFragment)).collect(Collectors.toList());
        List<Animal> resultList = new ArrayList<>();

        Comparator<Animal> comparator = new Comparator<Animal>() {
            @Override
            public int compare(Animal o1, Animal o2) {
                if (o1.getAnimalName().contains(textFragment) || o1.getAnimalSpecies().contains(textFragment)) {
                    return 0;
                }
                return 1;
            }
        };

        for(Animal animal : this.animalList) {
            if(comparator.compare(animal,null) == 0){
                resultList.add(animal);
            }
        }

        return resultList;
    }

    public void summary() {
        System.out.println("===================== ZWIERZETA W SCHRONISKU =====================");
        for(Animal animal : this.animalList) {
            animal.print();
        }
        System.out.println("==================================================================");
    }

    public Animal max() {
        return Collections.max(this.animalList, Animal::comparePriceTo);
    }

    public int getAnimalCount() {
        return this.animalCount;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public String getShelterName() {
        return this.shelterName;
    }

    public double getFilling() {return this.filling;}

    public List<Animal> getAnimalList() {return this.animalList;}
}
