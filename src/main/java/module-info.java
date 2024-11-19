module studia.animalshelterdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;

    opens studia.animalshelterdesktopapp.controllers to javafx.fxml;
    opens studia.animalshelterdesktopapp.views to javafx.fxml;
    exports studia.animalshelterdesktopapp;
    exports studia.animalshelterdesktopapp.controllers;
    exports studia.animalshelterdesktopapp.exceptions;
}
