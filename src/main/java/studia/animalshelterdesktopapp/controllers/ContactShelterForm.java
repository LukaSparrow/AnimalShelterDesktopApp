package studia.animalshelterdesktopapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.exceptions.ShelterNotFoundException;

public class ContactShelterForm {
    AnimalShelter shelter;
    @FXML Text contactInfoText;

    public void setShelter(AnimalShelter shelter) throws ShelterNotFoundException {
        if(shelter == null) {
            throw new ShelterNotFoundException("Schronisko nie istnieje.");
        }
        this.shelter = shelter;
        this.contactInfoText.setText("Dane kontaktowe schroniska " + shelter.getShelterName());
    }
}
