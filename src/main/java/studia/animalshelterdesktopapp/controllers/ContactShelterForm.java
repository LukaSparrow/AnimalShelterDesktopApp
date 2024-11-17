package studia.animalshelterdesktopapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalShelter;

public class ContactShelterForm {
    AnimalShelter shelter;
    @FXML Text contactInfoText;

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
        this.contactInfoText.setText("Dane kontaktowe schroniska " + shelter.getShelterName());
    }
}
