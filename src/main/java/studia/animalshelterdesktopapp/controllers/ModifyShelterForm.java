package studia.animalshelterdesktopapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.ShelterManager;
import studia.animalshelterdesktopapp.exceptions.InvalidCapacityException;

import static java.lang.Integer.parseInt;

public class ModifyShelterForm {
    private AdminView adminView;
    private ShelterManager manager = new ShelterManager();
    private AnimalShelter shelter;
    @FXML private TextField shelterNameField;
    @FXML private TextField shelterCapacityField;

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    public void initValues() {
        this.shelterNameField.setPromptText(this.shelter.getShelterName());
        this.shelterCapacityField.setText(String.valueOf(this.shelter.getMaxCapacity()));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void handleModifyShelter() {
        try {
            String shelterName = shelterNameField.getText();
            String shelterCapacityString = shelterCapacityField.getText();
            int shelterCapacity = parseInt(shelterCapacityString);

            AnimalShelter modifiedShelter = new AnimalShelter(shelter.getShelterName(), shelterCapacity);

            if (!shelterName.isEmpty()) {
                modifiedShelter.setShelterName(shelterName);
            }
            if (shelterCapacity >= manager.getShelter(shelter.getId()).getAnimalList().size()) {
                modifiedShelter.setMaxCapacity(shelterCapacity);
                manager.updateShelter(shelter,modifiedShelter);
                adminView.loadShelters();
                ((Stage)shelterNameField.getScene().getWindow()).close();
            } else {
                System.err.println("W schronisku jest wiecej zwierzat niz wybrana pojemnosc");
                showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Pojemnosc schroniska musi byc wartoscia wieksza od liczby zwierzat juz sie tam znajdujacych.");
            }
        }
        catch (NumberFormatException e) {
            System.err.println("Pojemnosc schroniska musi byc wartoscia liczbowa.");
            showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Pojemnosc schroniska musi byc wartoscia liczbowa.");
        }
    }
}
