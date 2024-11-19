package studia.animalshelterdesktopapp.controllers;

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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metoda sprawdzająca, czy wyjątek został rzucony przez Integer.parseInt()
    public static boolean wasThrownByParseInt(NumberFormatException e) {
        return AddAnimalForm.wasThrownByParseInt(e);
    }

    @FXML
    private void handleModifyAnimal() {
        try {
            String animalName = animalNameField.getText();
            String animalSpecies = animalSpeciesField.getText();
            AnimalCondition animalCondition = animalConditionCombobox.getValue();
            String animalAge = animalAgeField.getText();
            String animalPrice = animalPriceField.getText();

            int animalAgeInt = parseInt(animalAge);
            double animalPriceDouble = parseFloat(animalPrice);

            if(!animalName.isEmpty()) this.animal.setAnimalName(animalName);
            if(!animalSpecies.isEmpty()) this.animal.setAnimalSpecies(animalSpecies);
            if(animalCondition != null) this.animal.setAnimalCondition(animalCondition);
            if(animalAgeInt != 0) this.animal.setAnimalAge(animalAgeInt);
            if(animalPriceDouble != 0) this.animal.setAnimalPrice(animalPriceDouble);

            this.animalTableView.refresh();
            ((Stage)animalNameField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

            if (stackTrace.length > 0) {
                StackTraceElement thrower = stackTrace[0];
                if(wasThrownByParseInt(e)) {
                    System.err.println("Nieprawidlowy wiek zwierzecia.");
                    showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Nieprawidlowy wiek zwierzecia.");
                } else {
                    System.err.println("Nieprawidlowa cena zwierzecia.");
                    showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Nieprawidlowa cena zwierzecia.");
                }
            }
        }
    }
}