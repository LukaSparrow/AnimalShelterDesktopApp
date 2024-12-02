package studia.animalshelterdesktopapp.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import studia.animalshelterdesktopapp.Animal;
import studia.animalshelterdesktopapp.AnimalShelter;
import javafx.util.Callback;
import studia.animalshelterdesktopapp.exceptions.AnimalNotFoundException;
import studia.animalshelterdesktopapp.exceptions.ShelterNotFoundException;
import java.io.IOException;
import java.util.List;

public class UserView extends AppView {

    @FXML private TableColumn<AnimalShelter, Void> contactShelterButton;
    @FXML private TableColumn<AnimalShelter, Void> rateShelterButton;
    @FXML private TableColumn<Animal, Void> adoptAnimalButton;

    public void initialize() {
        // Inicjalizacja kolumn dla schronisk
        shelterName.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        addShelterContactButton();
        addShelterRateButton();

        // Inicjalizacja kolumn dla zwierzat
        animalName.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        animalSpecies.setCellValueFactory(new PropertyValueFactory<>("animalSpecies"));
        animalCondition.setCellValueFactory(new PropertyValueFactory<>("animalCondition"));
        animalAge.setCellValueFactory(new PropertyValueFactory<>("animalAge"));
        animalPrice.setCellValueFactory(new PropertyValueFactory<>("animalPrice"));
        animalPrice.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Formatowanie do dwóch miejsc po przecinku
                return String.format("%.2f", value);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }));
        addAnimalAdoptButton();

        shelterTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
            {
                selectedShelter = newValue;
                try {
                    loadAnimalsForSelectedShelter(selectedShelter);
                } catch (ShelterNotFoundException shelterNotFoundException) {
                    System.err.println("Schronisko nie istnieje");
                }
            } else {
                animalsTable.getItems().clear();
            }
        });

        searchAnimalField.setOnKeyReleased(event -> {
            //if(event.getCode() == KeyCode.ENTER) {
            animalFilter = searchAnimalField.getText().toLowerCase();
            handleAnimalSearch();
            //}
        });

        searchShelterField.setOnKeyReleased(event -> {
            //if(event.getCode() == KeyCode.ENTER) {
            shelterFilter = searchShelterField.getText().toLowerCase();
            handleShelterSearch();
            //}
        });

        loadShelters();
        try {
            loadAnimalsForSelectedShelter(manager.getAllShelters().getFirst());
        } catch (ShelterNotFoundException shelterNotFoundException) {
            System.err.println(shelterNotFoundException.getMessage());
        }
    }

    public void rateShelter(AnimalShelter shelter) throws ShelterNotFoundException {
        if(shelter == null) {
            throw new ShelterNotFoundException("Shelter not found");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/RateShelterForm.fxml"));
            Region root = loader.load();

            RateShelterForm rateForm = loader.getController();
            rateForm.setShelter(shelter);
            rateForm.setUserView(this);

            Stage stage = new Stage();
            stage.setTitle("Rate Shelter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file.");
        }
    }

    private void addShelterRateButton() {
        Callback<TableColumn<AnimalShelter, Void>, TableCell<AnimalShelter,Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Rate");
            {
                modifyButton.setOnAction(e -> {
                    AnimalShelter shelter = getTableView().getItems().get(getIndex());
                    try {
                        rateShelter(shelter);
                    } catch (ShelterNotFoundException shelterNotFoundException) {
                        System.err.println("Schronisko nie istnieje.");
                    }
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
        this.rateShelterButton.setCellFactory(cellFactory);
    }

    private void addShelterContactButton() {
        Callback<TableColumn<AnimalShelter, Void>, TableCell<AnimalShelter,Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Contact");
            {
                modifyButton.setOnAction(e -> {
                    AnimalShelter shelter = getTableView().getItems().get(getIndex());
                    try {
                        contactShelter(shelter);
                    } catch (ShelterNotFoundException shelterNotFoundException) {
                        System.err.println("Schronisko nie istnieje.");
                    }
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
                    try {
                        adoptAnimal(animal);
                        loadAnimalsForSelectedShelter(selectedShelter);
                    } catch (AnimalNotFoundException animalNotFoundException) {
                        System.err.println("Zwierze nie istnieje");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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

    private void contactShelter(AnimalShelter shelter) throws ShelterNotFoundException {
        if(shelter == null) {
            throw new ShelterNotFoundException("Schronisko nie istnieje");
        }
        System.out.println("Contacting shelter: " + shelter.getShelterName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/ContactShelterForm.fxml"));
            Region root = loader.load();

            ContactShelterForm contactForm = loader.getController();
            contactForm.setShelter(shelter);

            Stage stage = new Stage();
            stage.setTitle("Contact Shelter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file.");
        }
    }

    private void adoptAnimal(Animal animal) throws AnimalNotFoundException {
        try {
            if (animal == null) {
                throw new AnimalNotFoundException("Zwierze nie istnieje.");
            }
            boolean res = manager.adoptAnimal(selectedShelter.getId(), animal.getId());
            if (!res) {
                System.err.println("Animal: " + animal.getAnimalName() + " already in adoption");
                return;
            }
            loadAnimalsForSelectedShelter(selectedShelter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
