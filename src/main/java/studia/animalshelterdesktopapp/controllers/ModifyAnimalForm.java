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
import studia.animalshelterdesktopapp.ShelterManager;
import studia.animalshelterdesktopapp.exceptions.ShelterNotFoundException;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;


public class ModifyAnimalForm {
    private ShelterManager manager = new ShelterManager();
    private Animal animal;
    private AnimalShelter shelter;
    private AdminView adminView;
    @FXML private TextField animalNameField;
    @FXML private TextField animalSpeciesField;
    @FXML private ComboBox<AnimalCondition> animalConditionCombobox;
    @FXML private TextField animalAgeField;
    @FXML private TextField animalPriceField;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
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

            Animal modifiedAnimal = new Animal(this.animal);

            if(!animalName.isEmpty()) modifiedAnimal.setAnimalName(animalName);
            if(!animalSpecies.isEmpty()) modifiedAnimal.setAnimalSpecies(animalSpecies);
            if(animalCondition != null) modifiedAnimal.setAnimalCondition(animalCondition);
            if(animalAgeInt != 0) modifiedAnimal.setAnimalAge(animalAgeInt);
            if(animalPriceDouble != 0) modifiedAnimal.setAnimalPrice(animalPriceDouble);

            manager.updateAnimalInShelter(shelter.getId(), animal.getId(), modifiedAnimal);
            adminView.loadAnimalsForSelectedShelter(shelter);

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
        } catch (ShelterNotFoundException e) {
            e.printStackTrace();
        }
    }
}