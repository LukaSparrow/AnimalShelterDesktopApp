package studia.animalshelterdesktopapp.controllers;

import studia.animalshelterdesktopapp.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVUtil {

    public static void exportSheltersToCSV(List<AnimalShelter> shelters) {
        try(PrintWriter writer = new PrintWriter(new File("shelters.csv"))){
            StringBuilder sb = new StringBuilder();
            sb.append("Shelter Name");
            sb.append(',');
            sb.append("Max Capacity");
            sb.append('\n');

            for(AnimalShelter shelter : shelters){
                sb.append(shelter.getShelterName());
                sb.append(',');
                sb.append(shelter.getMaxCapacity());
                sb.append('\n');
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try(PrintWriter writer = new PrintWriter(new File("animals.csv"))){
            StringBuilder sb = new StringBuilder();
            sb.append("Name");
            sb.append(',');
            sb.append("Species");
            sb.append(',');
            sb.append("Condition");
            sb.append(',');
            sb.append("Age");
            sb.append(',');
            sb.append("Price");
            sb.append(',');
            sb.append("Shelter");
            sb.append('\n');

            for(AnimalShelter shelter : shelters){
                for(Animal animal : shelter.getAnimalList()){
                    sb.append(animal.getAnimalName());
                    sb.append(',');
                    sb.append(animal.getAnimalSpecies());
                    sb.append(',');
                    sb.append(animal.getAnimalCondition());
                    sb.append(',');
                    sb.append(animal.getAnimalAge());
                    sb.append(',');
                    sb.append(animal.getPrice());
                    sb.append(',');
                    sb.append(shelter.getShelterName());
                    sb.append('\n');
                }
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try(PrintWriter writer = new PrintWriter(new File("ratings.csv"))){
            StringBuilder sb = new StringBuilder();
            sb.append("Shelter Name");
            sb.append(',');
            sb.append("Value");
            sb.append(',');
            sb.append("Date");
            sb.append(',');
            sb.append("Comment");
            sb.append('\n');

            for(AnimalShelter shelter : shelters){
                for(Rating rating : shelter.getRatings()){
                    sb.append(shelter.getShelterName());
                    sb.append(',');
                    sb.append(rating.getValue());
                    sb.append(',');
                    sb.append(rating.getDate());
                    sb.append(',');
                    sb.append(rating.getComment());
                    sb.append('\n');
                }
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<AnimalShelter> importSheltersFromCSV(String shelterFile, String animalFile, String ratingFile) {

        List<AnimalShelter> shelters = new ArrayList<>();

        try(Scanner scanner = new Scanner(new File(shelterFile))){
            scanner.nextLine();
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(",");
                shelters.add(new AnimalShelter(line[0], Integer.parseInt(line[1])));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try(Scanner scanner = new Scanner(new File(animalFile))){
            scanner.nextLine();
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(",");
                AnimalCondition condition = AnimalCondition.fromString(line[2]);
                Animal animal = new Animal(line[0], line[1], condition, Integer.parseInt(line[3]), Double.parseDouble(line[4]));
                AnimalShelter shelter = shelters.stream().filter(x -> x.getShelterName().equals(line[5])).findFirst().orElse(null);
                if(shelter != null){
                    shelter.addAnimal(animal);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try(Scanner scanner = new Scanner(new File(ratingFile))){
            scanner.nextLine();
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(",");
                AnimalShelter shelter = shelters.stream().filter(x -> x.getShelterName().equals(line[0])).findFirst().orElse(null);
                if(shelter != null){
                    shelter.addRating(new Rating(Integer.parseInt(line[1]), (line.length == 4)?line[3]:" ", LocalDate.parse(line[2])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return shelters;
    }
}
