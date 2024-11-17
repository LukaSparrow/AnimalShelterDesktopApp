package studia.animalshelterdesktopapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.ShelterManager;

import java.io.IOException;

public class LoginView {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Text loginResult;

    @FXML
    private VBox vbox;

    private ShelterManager manager;

    public void setManager(ShelterManager manager) {
        this.manager = manager;
    }

    @FXML
    private void initialize() {
        try {
            String backgroundPath = getClass().getResource("/studia/animalshelterdesktopapp/views/loginBackground.jpg").toExternalForm();
            vbox.setStyle("-fx-background-image: url('" + backgroundPath + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center; " +
                    "-fx-background-repeat: no-repeat;");

        } catch (NullPointerException e) {
            System.out.println("Background image not found!");
        }
    }

    @FXML
    private void login() {
        if(username.getText().equals("admin") && password.getText().equals("admin")) {
            System.out.println("Login Successful admin");
            loadView("AdminView.fxml", "Administrator");
        } else if(username.getText().equals("user") && password.getText().equals("user")) {
            System.out.println("Login Successful user");
            loadView("UserView.fxml", "User");
        } else {
            System.out.println("Login Unsuccessful");
            loginResult.setText("Login Unsuccessful, try again");
            loginResult.setLayoutX(vbox.getWidth()/2 - loginResult.getLayoutBounds().getWidth()/2);
        }
    }

    // Metoda do ładowania nowego widoku
    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/" + fxmlFile));
            Parent root = loader.load();

            if(title.equals("Administrator")) {
                AdminView adminViewController = loader.getController();
                adminViewController.setManager(this.manager);
            } else {
                UserView userViewController = loader.getController();
                userViewController.setManager(this.manager);
            }

            // Tworzymy nowe okno
            Stage newStage = new Stage();
            newStage.setMinWidth(960);
            newStage.setMinHeight(720);
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            newStage.setTitle(title);
            newStage.show();

            // Zamykamy bieżące okno
            Stage currentStage = (Stage) username.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
