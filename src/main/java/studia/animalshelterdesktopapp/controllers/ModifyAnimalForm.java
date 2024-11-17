package studia.animalshelterdesktopapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalCondition;
import studia.animalshelterdesktopapp.AnimalShelter;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;


public class ModifyAnimalForm {
    private Animal animal;
    private ObservableList<Animal> animals;
    private AnimalShelter shelter;
    private TableView<Animal> animalTableView;
    @FXML private TextField animalNameField;
    @FXML private TextField animalSpeciesField;
    @FXML private ComboBox<AnimalCondition> animalConditionCombobox;
    @FXML private TextField animalAgeField;
    @FXML private TextField animalPriceField;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setAnimals(ObservableList<Animal> animals) {
        this.animals = animals;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    public void setAnimalTableView(TableView<Animal> animalTableView) {
        this.animalTableView = animalTableView;
    }

    @FXML public void initialize() {
        animalConditionCombobox.getItems().setAll(AnimalCondition.values());
        animalConditionCombobox.setValue(AnimalCondition.ZDROWE);
    }

    public void initValues() {
        animalConditionCombobox.setValue(animal.getAnimalCondition());
        animalNameField.setPromptText(animal.getAnimalName());
        animalSpeciesField.setPromptText(animal.getAnimalSpecies());
        animalAgeField.setText(String.valueOf(animal.getAnimalAge()));
        animalPriceField.setText(String.valueOf(animal.getAnimalPrice()));
    }

    @FXML
    private void handleModifyAnimal() {
        String animalName = animalNameField.getText();
        String animalSpecies = animalSpeciesField.getText();
        AnimalCondition animalCondition = animalConditionCombobox.getValue();
        String animalAge = animalAgeField.getText();
        String animalPrice = animalPriceField.getText();

        try {
            int animalAgeInt = parseInt(animalAge);
            double animalPriceDouble = parseFloat(animalPrice);
            if(!animalName.isEmpty()) this.animal.setAnimalName(animalName);
            if(!animalSpecies.isEmpty()) this.animal.setAnimalSpecies(animalSpecies);
            if(animalCondition != null) this.animal.setAnimalCondition(animalCondition);
            if(animalAgeInt != 0) this.animal.setAnimalAge(animalAgeInt);
            if(animalPriceDouble != 0) this.animal.setAnimalPrice(animalPriceDouble);
            this.animalTableView.refresh();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        ((Stage)animalNameField.getScene().getWindow()).close();
    }
}
