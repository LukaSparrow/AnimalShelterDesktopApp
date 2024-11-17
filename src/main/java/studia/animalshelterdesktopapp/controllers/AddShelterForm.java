package studia.animalshelterdesktopapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.AnimalShelter;

import static java.lang.Integer.parseInt;

public class AddShelterForm {
    private ObservableList<AnimalShelter> shelters;
    @FXML private TextField shelterNameField;
    @FXML private TextField shelterCapacityField;

    public void setShelters(ObservableList<AnimalShelter> shelters) {
        this.shelters = shelters;
    }

    @FXML
    private void handleAddShelter() {
        String shelterName = shelterNameField.getText();
        String shelterCapacityString = shelterCapacityField.getText();

        try {
            int shelterCapacity = parseInt(shelterCapacityString);
            this.shelters.add(new AnimalShelter(shelterName,shelterCapacity));
        }
        catch (NumberFormatException e) { e.printStackTrace(); }

        ((Stage)shelterNameField.getScene().getWindow()).close();
    }
}
