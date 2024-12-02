package studia.animalshelterdesktopapp;

import studia.animalshelterdesktopapp.exceptions.AnimalAlreadyExistsException;
import studia.animalshelterdesktopapp.exceptions.InvalidCapacityException;
import studia.animalshelterdesktopapp.exceptions.NotEnoughCapacityException;
import studia.animalshelterdesktopapp.exceptions.ShelterAlreadyExistsException;

public class DataGenerator {
    private static DataGenerator instance;
    private static final Object lock = new Object();
    private static final String[] shelterNames = {"PSIAKOWO I SPÓŁKA", "STOWARZYSZENIE PAKA DLA ZWIERZAKA", "FUNDACJA 7 ŻYCZEŃ", "PRZYTULISKO W JADOWIE", "FUNDACJA PSI PATROL", "PRZYTUL PSIAKA"};
    private static final String[] animalNames = {"Burek", "Luna", "Max", "Bella", "Rocky", "Milo", "Kira", "Nala", "Leo", "Zoe", "Bruno", "Daisy", "Oskar", "Loki", "Coco", "Rex", "Tosia", "Simba", "Gucio", "Fiona"};
    private static final String[] animalSpecies = {"Pies", "Kot", "Królik", "Chomik", "Świnka morska", "Papuga", "Żółw", "Fretka", "Koń", "Gołąb", "Szczur", "Kanarek", "Gekon", "Wąż", "Rybka", "Jeż", "Szynszyla", "Kura", "Kaczka", "Ślimak"};
    private static final AnimalCondition[] values = AnimalCondition.values();

    public static DataGenerator getInstance() {
        synchronized (lock) {
            if (DataGenerator.instance == null) {
                DataGenerator.instance = new DataGenerator();
            }
        }
        return DataGenerator.instance;
    }

    private DataGenerator() {
        System.out.println("Generator utworzony.");
    }

    public ShelterManager generateData() {
        ShelterManager manager = new ShelterManager();
        int numOfShelters = Math.max((int)(Math.random() * shelterNames.length),5);
        for(int i=0; i < numOfShelters; i++) {
            manager.addShelter(
                    new AnimalShelter(
                            shelterNames[(int) (Math.random() * (shelterNames.length - 1))],
                            (int) (Math.random() * 10 + 5)
                    )
            );
        }
        for(AnimalShelter shelter : manager.getAllShelters()) {
            int numOfAnimals = Math.max((int)(Math.random() * shelter.getMaxCapacity()),1);
            for(int j=0; j < numOfAnimals; j++) {
                manager.addAnimalToShelter(
                        shelter.getId(),
                        new Animal(
                                animalNames[(int)(Math.random() * (animalNames.length - 1))],
                                animalSpecies[(int)(Math.random() * (animalSpecies.length - 1))],
                                values[(int)(Math.random() * (AnimalCondition.values().length - 1))],
                                (int)(Math.random() * 25 + 1),
                                Math.random() * 900 + 100
                                )
                );
            }
        }
        return manager;
    }

    public static String[] getShelterNames() {
        return  shelterNames;
    }

    public static String[] getAnimalNames() {
        return animalNames;
    }

    public static String[] getAnimalSpecies() {
        return animalSpecies;
    }
}
