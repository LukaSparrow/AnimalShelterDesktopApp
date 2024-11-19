package studia.animalshelterdesktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.controllers.LoginView;
import studia.animalshelterdesktopapp.exceptions.ManagerNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            ShelterManager manager = DataGenerator.getInstance().generateData();

            // Wczytanie pliku FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/LoginView.fxml"));
            Parent root = loader.load();

            LoginView loginView = loader.getController();
            loginView.setManager(manager);

            // Ustawienie sceny
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
            primaryStage.setMinWidth(640);
            primaryStage.setMinHeight(480);
            primaryStage.setTitle("Animal Shelter Manager");
            primaryStage.setScene(scene);
            primaryStage.show();

        }
        catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            System.err.println("Failed to load FXML file.");
        }
        catch (ManagerNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}