package studia.animalshelterdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalCondition;
import studia.animalshelterdesktopapp.AnimalShelter;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class AddAnimalForm {
    private ObservableList<Animal> animals;
    private AnimalShelter shelter;
    @FXML private TextField animalNameField;
    @FXML private TextField animalSpeciesField;
    @FXML private ComboBox<AnimalCondition> animalConditionCombobox;
    @FXML private TextField animalAgeField;
    @FXML private TextField animalPriceField;

    public void setAnimals(ObservableList<Animal> animals) {
        this.animals = animals;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    @FXML public void initialize() {
        animalConditionCombobox.getItems().setAll(AnimalCondition.values());
        this.animals = FXCollections.observableArrayList();
    }

    @FXML
    private void handleAddAnimal() {
        String animalName = animalNameField.getText();
        String animalSpecies = animalSpeciesField.getText();
        AnimalCondition animalCondition = animalConditionCombobox.getValue();
        String animalAge = animalAgeField.getText();
        String animalPrice = animalPriceField.getText();

        try {
            int animalAgeInt = parseInt(animalAge);
            double animalPriceDouble = parseFloat(animalPrice);
            if (animalName != null && animalSpecies != null && animalCondition != null && animalAgeInt != 0 && animalPriceDouble != 0) {
                Animal newAnimal = new Animal(animalName, animalSpecies, animalCondition, animalAgeInt, animalPriceDouble);
                //this.shelter.addAnimal(newAnimal);
                if(this.shelter.addAnimal(newAnimal)) {
                    this.animals.add(newAnimal);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        ((Stage)animalNameField.getScene().getWindow()).close();
    }
}
