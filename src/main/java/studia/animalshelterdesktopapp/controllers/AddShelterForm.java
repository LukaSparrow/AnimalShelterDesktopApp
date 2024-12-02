package studia.animalshelterdesktopapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.ShelterManager;

import static java.lang.Integer.parseInt;

public class AddShelterForm {
    private AdminView adminView;
    private ShelterManager manager = new ShelterManager();
    @FXML private TextField shelterNameField;
    @FXML private TextField shelterCapacityField;

    public void setAdminView(AdminView adminView) {
        this.adminView = adminView;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void handleAddShelter() {
        try {
            String shelterName = shelterNameField.getText();
            String shelterCapacityString = shelterCapacityField.getText();

            if (!shelterName.isEmpty() && !shelterCapacityString.isEmpty()) {
                int shelterCapacity = parseInt(shelterCapacityString);
                manager.addShelter(new AnimalShelter(shelterName, shelterCapacity));
                adminView.loadShelters();
                ((Stage) shelterNameField.getScene().getWindow()).close();
            } else {
                System.err.println("Wszystkie dane schroniska musza byc podane.");
                showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Wszystkie dane schroniska musza byc podane.");
            }
        }
        catch (NumberFormatException e) {
            System.err.println("Pojemnosc schroniska musi byc wartoscia liczbowa.");
            showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", e.getMessage());
        }
    }
}
