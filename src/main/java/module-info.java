module studia.animalshelterdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens studia.animalshelterdesktopapp.controllers to javafx.fxml;
    opens studia.animalshelterdesktopapp.views to javafx.fxml;
    opens studia.animalshelterdesktopapp to org.hibernate.orm.core;
    exports studia.animalshelterdesktopapp;
    exports studia.animalshelterdesktopapp.controllers;
    exports studia.animalshelterdesktopapp.exceptions;
}
