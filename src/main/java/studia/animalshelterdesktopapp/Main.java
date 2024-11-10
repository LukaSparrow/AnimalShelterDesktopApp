package studia.animalshelterdesktopapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Wczytanie pliku FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/LoginView.fxml"));
            Parent root = loader.load();

            // Ustawienie sceny
            Scene scene = new Scene(root);
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