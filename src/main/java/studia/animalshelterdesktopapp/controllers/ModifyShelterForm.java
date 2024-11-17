package studia.animalshelterdesktopapp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.AnimalShelter;

import static java.lang.Integer.parseInt;

public class ModifyShelterForm {
    private AnimalShelter shelter;
    private TableView<AnimalShelter> shelterTableView;
    @FXML private TextField shelterNameField;
    @FXML private TextField shelterCapacityField;

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    public void setShelterTableView(TableView<AnimalShelter> shelterTableView) {
        this.shelterTableView = shelterTableView;
    }

    public void initValues() {
        this.shelterNameField.setPromptText(this.shelter.getShelterName());
        this.shelterCapacityField.setText(String.valueOf(this.shelter.getMaxCapacity()));
    }

    @FXML
    private void handleModifyShelter() {
        String shelterName = shelterNameField.getText();
        String shelterCapacityString = shelterCapacityField.getText();

        try {
            int shelterCapacity = parseInt(shelterCapacityString);
            if(!shelterName.isEmpty()) {
                this.shelter.setShelterName(shelterName);
            }
            if(shelterCapacity > 0) {
                if(shelterCapacity >= shelter.getAnimalCount())
                {
                    this.shelter.setShelterCapacity(shelterCapacity);
                } else {
                    System.err.println("W schronisku jest wiecej zwierzat niz wybrana pojemnosc");
                    return;
                }
            } else {
                System.err.println("Pojemnosc schroniska musi byc wieksza od 0");
                return;
            }
            this.shelter.updateFilling();
            this.shelterTableView.refresh();
        }
        catch (NumberFormatException e) { e.printStackTrace(); }

        ((Stage)shelterNameField.getScene().getWindow()).close();
    }
}
