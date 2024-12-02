package studia.animalshelterdesktopapp.controllers;

import studia.animalshelterdesktopapp.AnimalShelter;

import java.io.*;
import java.util.List;

public class SerializationUtil {
    public static void serializeShelters(List<AnimalShelter> shelters, String filename) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(shelters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<AnimalShelter> deserializeShelters(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<AnimalShelter> shelters = (List<AnimalShelter>) ois.readObject();
            if(shelters != null) {
                for(AnimalShelter shelter : shelters) {
                    shelter.getAnimalList().forEach(animal -> animal.setShelter(shelter));
                    shelter.getRatings().forEach(rating -> rating.setShelter(shelter));
                }
            }
            return shelters;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
