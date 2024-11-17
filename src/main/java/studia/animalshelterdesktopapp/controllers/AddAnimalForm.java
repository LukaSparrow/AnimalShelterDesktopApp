package studia.animalshelterdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalCondition;
import studia.animalshelterdesktopapp.AnimalShelter;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class AddAnimalForm {
    private TableView<AnimalShelter> shelterTableView;
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

    public void setShelterTableView(TableView<AnimalShelter> shelterTableView) {
        this.shelterTableView = shelterTableView;
    }

    @FXML public void initialize() {
        animalConditionCombobox.getItems().setAll(AnimalCondition.values());
        animalConditionCombobox.setValue(AnimalCondition.ZDROWE);
        this.animals = FXCollections.observableArrayList();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddAnimal() {
        String animalName = animalNameField.getText();
        String animalSpecies = animalSpeciesField.getText();
        AnimalCondition animalCondition = animalConditionCombobox.getValue();
        String animalAge = animalAgeField.getText();
        String animalPrice = animalPriceField.getText();

        if (!animalName.isEmpty() && !animalSpecies.isEmpty() && animalCondition != null && !animalAge.isEmpty() && !animalPrice.isEmpty()) {
            try {
                int animalAgeInt = parseInt(animalAge);
                double animalPriceDouble = parseFloat(animalPrice);

                Animal newAnimal = new Animal(animalName, animalSpecies, animalCondition, animalAgeInt, animalPriceDouble);
                if (this.shelter.addAnimal(newAnimal)) {
                    this.animals.add(newAnimal);
                    this.shelterTableView.refresh();
                    ((Stage)animalNameField.getScene().getWindow()).close();
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Takie zwierze juz istnieje.");
                }
            } catch(NumberFormatException e){
                e.printStackTrace();
                showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Wiek oraz cena musza byc wartosciami liczbowymi.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Wszystkie dane zwierzecia musza byc podane.");
        }
    }
}
