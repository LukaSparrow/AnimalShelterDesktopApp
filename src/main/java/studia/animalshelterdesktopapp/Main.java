package studia.animalshelterdesktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.controllers.LoginView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            ShelterManager manager = new ShelterManager();
            AnimalShelter s1 = new AnimalShelter("Zjebowowo", 7);
            s1.addAnimal(new Animal("Janusz", "Korwin", AnimalCondition.CHORE, 12, 9999));
            s1.addAnimal(new Animal("hahaha", "sd", AnimalCondition.CHORE, 12, 1231));
            s1.addAnimal(new Animal("ggaba", "aaa", AnimalCondition.CHORE, 12, 995599));
            manager.addShelter("Zjebowo", 10);
            manager.addShelter(s1);

            // Wczytanie pliku FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/LoginView.fxml"));
            Parent root = loader.load();

            LoginView loginView = loader.getController();
            loginView.setManager(manager);

            // Ustawienie sceny
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.setMinWidth(640);
            primaryStage.setMinHeight(480);
            primaryStage.setTitle("Animal Shelter Manager");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}