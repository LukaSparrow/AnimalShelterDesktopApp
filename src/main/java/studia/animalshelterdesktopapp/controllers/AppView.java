package studia.animalshelterdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalCondition;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.ShelterManager;
import studia.animalshelterdesktopapp.exceptions.ManagerNotFoundException;
import studia.animalshelterdesktopapp.exceptions.ShelterNotFoundException;

import java.io.IOException;

public class AppView {
    protected ShelterManager manager;
    protected ObservableList<AnimalShelter> shelters;
    protected ObservableList<Animal> animals;
    protected AnimalShelter selectedShelter;
    protected static int counter1 = 0;
    protected static int counter2 = 0;

    @FXML protected Button logout;
    @FXML protected Button sortSheltersButton;
    @FXML protected Button sortAnimalsButton;

    @FXML protected TableView<AnimalShelter> shelterTable;
    @FXML protected TableColumn<AnimalShelter, String> shelterName;
    @FXML protected TableColumn<AnimalShelter, Integer> capacity;
    @FXML protected TableColumn<AnimalShelter, Integer> filling;

    @FXML protected TableView<Animal> animalsTable;
    @FXML protected TableColumn<Animal, String> animalName;
    @FXML protected TableColumn<Animal, String> animalSpecies;
    @FXML protected TableColumn<Animal, AnimalCondition> animalCondition;
    @FXML protected TableColumn<Animal, Integer> animalAge;
    @FXML protected TableColumn<Animal, Double> animalPrice;

    @FXML protected TextField searchAnimalField;
    @FXML protected TextField searchShelterField;

    public void setManager(ShelterManager manager) throws ManagerNotFoundException {
        this.manager = manager;
        if(this.manager == null) {
            throw new ManagerNotFoundException("Manadzer nie istnieje.");
        }
        this.shelters = FXCollections.observableArrayList(manager.getShelters().values());
        this.selectedShelter = shelters.getFirst();
        this.shelterTable.setItems(shelters);
        this.animals = FXCollections.observableArrayList(selectedShelter.getAnimalList());
        this.animalsTable.setItems(animals);
    }

    @FXML protected void handleSortShelter() {
        switch(counter1 % 3) {
            case 0:
                shelters.sort(AnimalShelter.nameComparator);
                break;
            case 1:
                shelters.sort(AnimalShelter.capacityComparator);
                break;
            case 2:
                shelters.sort(AnimalShelter.fillingComparator);
                break;
            default:
                shelters.sort(AnimalShelter.nameComparator);
                break;
        }
        shelterTable.refresh();
        counter1++;
    }

    @FXML protected void handleSortAnimal() {
        switch(counter2 % 4) {
            case 0:
                animals.sort(Animal::compareNameTo);
                System.out.println("Name");
                break;
            case 1:
                animals.sort(Animal::compareSpeciesTo);
                System.out.println("species");
                break;
            case 2:
                animals.sort(Animal::compareAgeTo);
                System.out.println("age");
                break;
            case 3:
                animals.sort(Animal::comparePriceTo);
                System.out.println("price");
                break;
        }
        animalsTable.refresh();
        counter2++;
    }

    protected void handleAnimalSearch() {
        animals = FXCollections.observableArrayList(selectedShelter.searchPartial(searchAnimalField.getText()));
        animalsTable.setItems(animals);
        animalsTable.refresh();
    }

    protected void handleShelterSearch() {
        shelters = FXCollections.observableArrayList(manager.searchPartial(searchShelterField.getText()));
        shelterTable.setItems(shelters);
        shelterTable.refresh();
    }

    protected void loadAnimalsForSelectedShelter(AnimalShelter selectedShelter) throws ShelterNotFoundException {
        if(selectedShelter == null) {
            throw new ShelterNotFoundException("Schronisko nie istnieje");
        }
        animals = FXCollections.observableArrayList(selectedShelter.getAnimalList());
        animalsTable.setItems(animals);
        animalsTable.refresh();
    }

    @FXML protected void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/LoginView.fxml"));
            Parent root = loader.load();

            // Tworzymy nowe okno
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Animal Shelter Manager");
            newStage.show();

            LoginView loginView = loader.getController();
            loginView.setManager(manager);

            // Zamykamy bieżące okno
            Stage currentStage = (Stage) logout.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            System.err.println("Failed to load FXML file.");
        }
        catch (ManagerNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
