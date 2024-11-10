module studia.animalshelterdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens studia.animalshelterdesktopapp.controllers to javafx.fxml;
    opens studia.animalshelterdesktopapp.views to javafx.fxml;
    exports studia.animalshelterdesktopapp;
}
