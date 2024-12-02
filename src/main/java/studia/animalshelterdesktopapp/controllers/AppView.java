package studia.animalshelterdesktopapp.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.*;
import studia.animalshelterdesktopapp.exceptions.ManagerNotFoundException;
import studia.animalshelterdesktopapp.exceptions.ShelterNotFoundException;

import java.io.IOException;
import java.util.List;

public class AppView {
    protected String shelterFilter = "";
    protected String animalFilter = "";
    protected ShelterManager manager = new ShelterManager();
    protected AnimalShelter selectedShelter;

    @FXML protected Button logout;
    @FXML protected Button sortSheltersButton;
    @FXML protected Button sortAnimalsButton;

    @FXML protected TableView<AnimalShelter> shelterTable;
    @FXML protected TableColumn<AnimalShelter, String> shelterName;
    @FXML protected TableColumn<AnimalShelter, Integer> capacity;
    @FXML protected TableColumn<AnimalShelter, String> shelterRatings;

    @FXML protected TableView<Animal> animalsTable;
    @FXML protected TableColumn<Animal, String> animalName;
    @FXML protected TableColumn<Animal, String> animalSpecies;
    @FXML protected TableColumn<Animal, AnimalCondition> animalCondition;
    @FXML protected TableColumn<Animal, Integer> animalAge;
    @FXML protected TableColumn<Animal, Double> animalPrice;

    @FXML protected TextField searchAnimalField;
    @FXML protected TextField searchShelterField;

    @FXML protected void handleSortShelter() {
        ObservableList<AnimalShelter> shelters = shelterTable.getItems();
        FXCollections.sort(shelters);
    }

    @FXML protected void handleSortAnimal() {
        ObservableList<Animal> animals = animalsTable.getItems();
        FXCollections.sort(animals);
    }

    protected void handleAnimalSearch() {
        try {
        loadAnimalsForSelectedShelter(selectedShelter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleShelterSearch() {
        loadShelters();
    }

    protected void loadAnimalsForSelectedShelter(AnimalShelter selectedShelter) throws ShelterNotFoundException {
        if(selectedShelter == null) {
            throw new ShelterNotFoundException("Schronisko nie istnieje");
        }
        List<Animal> animals = manager.findAnimalsInShelter(selectedShelter.getId(), animalFilter, null);
        animalsTable.setItems(FXCollections.observableList(animals));
        animalsTable.refresh();
    }

    protected void loadRatingsSummary() {
        List<Object[]> summary = manager.getShelterRatingsSummary();

        shelterRatings.setCellValueFactory(cellData -> {
            AnimalShelter shelter = cellData.getValue();

            Object[] matchingRow = summary.stream()
                    .filter(row -> row[0].equals(shelter.getShelterName()))
                    .findFirst()
                    .orElse(null);

            if (matchingRow != null && matchingRow.length >= 3) {
                double avgRating = (double) matchingRow[1];
                long count = (long) matchingRow[2];
                String formattedRating = String.format("%.2f", avgRating);
                return new SimpleStringProperty(formattedRating + " (" + count + ")");
            } else {
                return new SimpleStringProperty("Brak danych");
            }
        });
    }

    protected void loadShelters() {
        try {
            List<AnimalShelter> shelters = manager.findSheltersByName(shelterFilter);
            shelterTable.getItems().setAll(FXCollections.observableList(shelters));
            loadRatingsSummary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSerializeShelters() {
        try {
            this.manager.serializeShelters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeserializeShelters() {
        try {
            this.manager.deserializeShelters();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadShelters();
    }

    public void onExportShelters() {
        try {
            this.manager.exportSheltersToCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onImportShelters() {
        try {
            this.manager.importSheltersFromCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadShelters();
    }

    @FXML protected void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/LoginView.fxml"));
            Parent root = loader.load();

            // Tworzymy nowe okno
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Animal Shelter Manager");
            newStage.show();

            LoginView loginView = loader.getController();
            loginView.setManager(manager);

            // Zamykamy bieżące okno
            Stage currentStage = (Stage) logout.getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            System.err.println("Failed to load FXML file.");
        }
        catch (ManagerNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
