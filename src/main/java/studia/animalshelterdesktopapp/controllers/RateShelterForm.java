package studia.animalshelterdesktopapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.ShelterManager;

public class RateShelterForm {
    private AnimalShelter shelter;
    private ShelterManager manager = new ShelterManager();
    private UserView userView;
    @FXML private TextField rateShelterComment;
    @FXML private ComboBox<Integer> rating;

    @FXML public void initialize() {
        rating.getItems().addAll(1, 2, 3, 4, 5);
        rating.setValue(5);
    }

    public void setUserView(UserView userView) {
        this.userView = userView;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    @FXML private void handleRateShelter() {
        String comment = rateShelterComment.getText();
        int ratingValue = rating.getValue();

        manager.addRating(shelter, ratingValue, comment);
        userView.loadShelters();
        ((Stage) rating.getScene().getWindow()).close();
    }
}
