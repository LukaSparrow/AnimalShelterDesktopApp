package studia.animalshelterdesktopapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.ShelterManager;

import static java.lang.Integer.parseInt;

public class AddShelterForm {
    private ObservableList<AnimalShelter> shelters;
    private TableView<AnimalShelter> shelterTableView;
    private ShelterManager manager;
    @FXML private TextField shelterNameField;
    @FXML private TextField shelterCapacityField;

    public void setManager(ShelterManager manager) {
        this.manager = manager;
    }

    public void setShelters(ObservableList<AnimalShelter> shelters) {
        this.shelters = shelters;
    }

    public void setShelterTableView(TableView<AnimalShelter> shelterTableView) {
        this.shelterTableView = shelterTableView;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private void handleAddShelter() {
        String shelterName = shelterNameField.getText();
        String shelterCapacityString = shelterCapacityField.getText();

        if(!shelterName.isEmpty() && !shelterCapacityString.isEmpty()) {
            try {
                int shelterCapacity = parseInt(shelterCapacityString);
                if(this.manager.addShelter(new AnimalShelter(shelterName, shelterCapacity))) {
                    this.shelters.add(new AnimalShelter(shelterName, shelterCapacity));
                    this.shelterTableView.refresh();
                }
                ((Stage) shelterNameField.getScene().getWindow()).close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Pojemnosc schroniska musi byc wartoscia liczbowa.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Nieprawidlowe dane", "Wszystkie dane schroniska musza byc podane.");
        }
    }
}
