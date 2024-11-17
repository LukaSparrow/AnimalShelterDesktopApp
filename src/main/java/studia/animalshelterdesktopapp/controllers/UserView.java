package studia.animalshelterdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalCondition;
import studia.animalshelterdesktopapp.AnimalShelter;
import studia.animalshelterdesktopapp.ShelterManager;
import javafx.util.Callback;
import java.io.IOException;

public class UserView {
    private ShelterManager manager;
    private ObservableList<AnimalShelter> shelters;
    private ObservableList<Animal> animals;
    private AnimalShelter selectedShelter;
    private static int counter1 = 0;
    private static int counter2 = 0;

    public void setManager(ShelterManager manager) {
        this.manager = manager;
        if(this.manager != null) {
            this.shelters = FXCollections.observableArrayList(manager.getShelters().values());
            this.selectedShelter = shelters.getFirst();
            this.shelterTable.setItems(shelters);
            this.animals = FXCollections.observableArrayList(selectedShelter.getAnimalList());
            this.animalsTable.setItems(animals);
        }
    }

    @FXML private Button logout;
    @FXML private Button sortSheltersButton;
    @FXML private Button sortAnimalsButton;

    @FXML private TableView<AnimalShelter> shelterTable;
    @FXML private TableColumn<AnimalShelter, String> shelterName;
    @FXML private TableColumn<AnimalShelter, Integer> capacity;
    @FXML private TableColumn<AnimalShelter, Integer> filling;
    @FXML private TableColumn<AnimalShelter, Void> contactShelterButton;

    @FXML private TableView<Animal> animalsTable;
    @FXML private TableColumn<Animal, String> animalName;
    @FXML private TableColumn<Animal, String> animalSpecies;
    @FXML private TableColumn<Animal, AnimalCondition> animalCondition;
    @FXML private TableColumn<Animal, Integer> animalAge;
    @FXML private TableColumn<Animal, Integer> animalPrice;
    @FXML private TableColumn<Animal, Void> adoptAnimalButton;

    @FXML private TextField searchAnimalField;
    @FXML private TextField searchShelterField;

    @FXML private void handleSortShelter() {
        switch(counter1 % 3) {
            case 0:
                shelters.sort(AnimalShelter.nameComparator);
                break;
            case 1:
                shelters.sort(AnimalShelter.capacityComparator);
                break;
            case 2:
                shelters.sort(AnimalShelter.fillingComparator);
                break;
            default:
                shelters.sort(AnimalShelter.nameComparator);
                break;
        }
        shelterTable.refresh();
        counter1++;
    }

    @FXML private void handleSortAnimal() {
        switch(counter2 % 4) {
            case 0:
                animals.sort(Animal::compareNameTo);
                System.out.println("Name");
                break;
            case 1:
                animals.sort(Animal::compareSpeciesTo);
                System.out.println("species");
                break;
            case 2:
                animals.sort(Animal::compareAgeTo);
                System.out.println("age");
                break;
            case 3:
                animals.sort(Animal::comparePriceTo);
                System.out.println("price");
                break;
        }
        animalsTable.refresh();
        counter2++;
    }

    // Funkcja pomocnicza do wyświetlania alertów
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize() {
        // Inicjalizacja kolumn dla schronisk
        shelterName.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        filling.setCellValueFactory(new PropertyValueFactory<>("filling"));
        addShelterContactButton();

        // Inicjalizacja kolumn dla zwierzat
        animalName.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        animalSpecies.setCellValueFactory(new PropertyValueFactory<>("animalSpecies"));
        animalCondition.setCellValueFactory(new PropertyValueFactory<>("animalCondition"));
        animalAge.setCellValueFactory(new PropertyValueFactory<>("animalAge"));
        animalPrice.setCellValueFactory(new PropertyValueFactory<>("animalPrice"));
        addAnimalAdoptButton();

        shelterTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
            {
                selectedShelter = newValue;
                loadAnimalsForSelectedShelter(selectedShelter);
            } else {
                animalsTable.getItems().clear();
            }
        });

        searchAnimalField.setOnKeyReleased(event -> {
            //if(event.getCode() == KeyCode.ENTER) {
            handleAnimalSearch();
            //}
        });

        searchShelterField.setOnKeyReleased(event -> {
            //if(event.getCode() == KeyCode.ENTER) {
            handleShelterSearch();
            //}
        });
    }

    private void handleAnimalSearch() {
        animals = FXCollections.observableArrayList(selectedShelter.searchPartial(searchAnimalField.getText()));
        animalsTable.setItems(animals);
        animalsTable.refresh();
    }

    private void handleShelterSearch() {
        shelters = FXCollections.observableArrayList(manager.searchPartial(searchShelterField.getText()));
        shelterTable.setItems(shelters);
        shelterTable.refresh();
    }

    private void loadAnimalsForSelectedShelter(AnimalShelter selectedShelter) {
        animals = FXCollections.observableArrayList(selectedShelter.getAnimalList());
        animalsTable.setItems(animals);
        animalsTable.refresh();
    }

    private void addShelterContactButton() {
        Callback<TableColumn<AnimalShelter, Void>, TableCell<AnimalShelter,Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Contact");
            {
                modifyButton.setOnAction(e -> {
                    AnimalShelter shelter = getTableView().getItems().get(getIndex());
                    contactShelter(shelter);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item,empty);
                if(empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        };
        this.contactShelterButton.setCellFactory(cellFactory);
    }

    private void addAnimalAdoptButton() {
        Callback<TableColumn<Animal, Void>, TableCell<Animal,Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Adopt");
            {
                modifyButton.setOnAction(e -> {
                    Animal animal = getTableView().getItems().get(getIndex());
                    adoptAnimal(animal);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item,empty);
                if(empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        };
        this.adoptAnimalButton.setCellFactory(cellFactory);
    }

    private void contactShelter(AnimalShelter shelter) {
        System.out.println("Contacting shelter: " + shelter.getShelterName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/ContactShelterForm.fxml"));
            Region root = loader.load();

            ContactShelterForm contactForm = loader.getController();
            contactForm.setShelter(this.selectedShelter);

            Stage stage = new Stage();
            stage.setTitle("Contact Shelter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adoptAnimal(Animal animal) {
        System.out.println("Adopting animal: " + animal.getAnimalName());
        this.selectedShelter.getAnimal(animal.getAnimalName());
        //animals.remove(animal);
        animalsTable.refresh();
    }

    @FXML private void logOut() {
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
        } catch (IOException e) {
            System.err.println("Error loading view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
