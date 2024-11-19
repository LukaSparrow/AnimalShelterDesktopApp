package studia.animalshelterdesktopapp;

import studia.animalshelterdesktopapp.exceptions.InvalidCapacityException;
import studia.animalshelterdesktopapp.exceptions.ShelterAlreadyExistsException;


import java.util.*;

public class ShelterManager {
    private Map<String, AnimalShelter> shelters;

    public ShelterManager() {
        this.shelters = new HashMap<>();
    }

    public boolean addShelter(String name, int capacity) throws InvalidCapacityException, ShelterAlreadyExistsException {
        if(capacity <= 0) {
            throw new InvalidCapacityException("Pojemnosc schroniska musi byc wieksza od zera.");
        }
        if(shelters.containsKey(name)) {
            throw new ShelterAlreadyExistsException("Schronisko o takiej nazwie juz istnieje.");
        }
        this.shelters.put(name, new AnimalShelter(name, capacity));
        return true;
    }

    public boolean addShelter(AnimalShelter shelter) throws InvalidCapacityException, ShelterAlreadyExistsException {
        if(shelter.getMaxCapacity() <= 0) {
            throw new InvalidCapacityException("Pojemnosc schroniska musi byc wieksza od zera.");
        }
        if(shelters.containsKey(shelter.getShelterName())) {
            throw new ShelterAlreadyExistsException("Schronisko o takiej nazwie juz istnieje.");
        }
        this.shelters.put(shelter.getShelterName(), shelter);
        return true;
    }

    public AnimalShelter getShelter(String name) {
        if(shelters.containsKey(name)) {
            return shelters.get(name);
        }
        System.err.println("Schronisko o takiej nazwie nie istnieje.");
        return null;
    }

    public List<AnimalShelter> searchPartial(String textFragment) {
        List<AnimalShelter> result = new ArrayList<>();
        String lowerCaseQuery = textFragment.toLowerCase();

        for (AnimalShelter shelter : shelters.values()) {
            if (shelter.getShelterName().toLowerCase().contains(lowerCaseQuery)) {
                result.add(shelter);
            }
        }
        return result;
    }

    public void removeShelter(String name) {
        if(shelters.containsKey(name)) {
            shelters.remove(name);
        } else {
            System.err.println("Schronisko o takiej nazwie nie istnieje.");
        }
    }

    public void removeShelter(AnimalShelter shelter) {
        if(shelters.containsKey(shelter.getShelterName())) {
            shelters.remove(shelter.getShelterName());
        } else {
            System.err.println("Schronisko o takiej nazwie nie istnieje.");
        }
    }

    public List<AnimalShelter> findEmpty() {
        List<AnimalShelter> resultList = new ArrayList<>();

        for(Map.Entry<String, AnimalShelter> shelter : shelters.entrySet()) {
            if(shelter.getValue().getAnimalCount() == 0) {
                resultList.add(shelter.getValue());
            }
        }
        if(resultList.isEmpty())
        {
            System.err.println("Nie znaleziono zadnego pustego schroniska.");
            return null;
        }
        return resultList;
    }

    public void summary() {
        System.out.println("=========================== SCHRONISKA ===========================");
        for(Map.Entry<String, AnimalShelter> shelter : shelters.entrySet()) {
            double filled = (double) shelter.getValue().getAnimalCount() / (double) shelter.getValue().getMaxCapacity() * 100;
            System.out.printf("Schronisko %s\nZapelnienie: %.2f%%\n----------------------------------\n", shelter.getKey(), filled);
        }
        System.out.println("==================================================================");
    }

    public Map<String, AnimalShelter> getShelters() {
        return this.shelters;
    }
}
