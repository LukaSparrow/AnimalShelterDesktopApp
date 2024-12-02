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

public class AdminView extends AppView {

    @FXML private TableColumn<AnimalShelter, Void> modifyShelterButton;
    @FXML private TableColumn<AnimalShelter, Void> deleteShelterButton;

    @FXML private TableColumn<Animal, Void> modifyAnimalButton;
    @FXML private TableColumn<Animal, Void> deleteAnimalButton;

    @FXML private void handleAddShelter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/AddShelterForm.fxml"));
            Region root = loader.load();

            AddShelterForm addForm = loader.getController();
            addForm.setAdminView(this);

            Stage stage = new Stage();
            stage.setTitle("Add Shelter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file.");
        }
    }

    @FXML private void handleAddAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/AddAnimalForm.fxml"));
            Region root = loader.load();

            AddAnimalForm addForm = loader.getController();
            addForm.setShelter(this.selectedShelter);
            addForm.setAdminView(this);

            Stage stage = new Stage();
            stage.setTitle("Add Animal");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file.");
        }
    }

    public void initialize() {
        // Inicjalizacja kolumn dla schronisk
        shelterName.setCellValueFactory(new PropertyValueFactory<>("shelterName"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        addShelterModifyButton();
        addShelterDeleteButton();

        // Inicjalizacja kolumn dla zwierzat
        animalName.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        animalSpecies.setCellValueFactory(new PropertyValueFactory<>("animalSpecies"));
        animalCondition.setCellValueFactory(new PropertyValueFactory<>("animalCondition"));
        animalAge.setCellValueFactory(new PropertyValueFactory<>("animalAge"));
        animalPrice.setCellValueFactory(new PropertyValueFactory<>("animalPrice"));
        animalPrice.setCellFactory(column -> new TextFieldTableCell<>(new StringConverter<>() {
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
        addAnimalModifyButton();
        addAnimalDeleteButton();

        shelterTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
            {
                selectedShelter = newValue;
                try {
                    loadAnimalsForSelectedShelter(selectedShelter);
                } catch (ShelterNotFoundException shelterNotFoundException) {
                    System.err.println(shelterNotFoundException.getMessage());
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

    private void addShelterModifyButton() {
        Callback<TableColumn<AnimalShelter, Void>, TableCell<AnimalShelter,Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");
            {
                modifyButton.setOnAction(e -> {
                    AnimalShelter shelter = getTableView().getItems().get(getIndex());
                    try {
                        modifyShelter(shelter);
                    } catch (ShelterNotFoundException shelterNotFoundException) {
                        System.err.println(shelterNotFoundException.getMessage());
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
        this.modifyShelterButton.setCellFactory(cellFactory);
    }

    private void addShelterDeleteButton() {
        Callback<TableColumn<AnimalShelter, Void>, TableCell<AnimalShelter, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    AnimalShelter shelter = getTableView().getItems().get(getIndex());
                    try {
                        deleteShelter(shelter);
                    } catch (ShelterNotFoundException shelterNotFoundException) {
                        System.err.println("Schronisko nie istnieje.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        };

        this.deleteShelterButton.setCellFactory(cellFactory);
    }

    private void addAnimalModifyButton() {
        Callback<TableColumn<Animal, Void>, TableCell<Animal,Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");
            {
                modifyButton.setOnAction(e -> {
                    Animal animal = getTableView().getItems().get(getIndex());
                    try {
                        modifyAnimal(animal);
                    } catch (AnimalNotFoundException animalNotFoundException) {
                        System.err.println("Zwierze nie istnieje.");
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
        this.modifyAnimalButton.setCellFactory(cellFactory);
    }

    private void addAnimalDeleteButton() {
        Callback<TableColumn<Animal, Void>, TableCell<Animal, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Animal animal = getTableView().getItems().get(getIndex());
                    try {
                        deleteAnimal(animal);
                    } catch (AnimalNotFoundException animalNotFoundException) {
                        System.err.println("Zwierze nie istnieje.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        };

        this.deleteAnimalButton.setCellFactory(cellFactory);
    }

    private void modifyShelter(AnimalShelter shelter) throws ShelterNotFoundException {
        if(shelter == null) {
            throw new ShelterNotFoundException("Schronisko nie istnieje.");
        }
        System.out.println("Modifying shelter " + shelter.getShelterName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/ModifyShelterForm.fxml"));
            Region root = loader.load();

            ModifyShelterForm modifyForm = loader.getController();
            modifyForm.setShelter(shelter);
            modifyForm.initValues();
            modifyForm.setAdminView(this);

            Stage stage = new Stage();
            stage.setTitle("Modify Shelter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file.");
        }
    }

    private void deleteShelter(AnimalShelter shelter) throws ShelterNotFoundException {
        manager.removeShelter(shelter.getShelterName());
        loadShelters();
    }

    private void modifyAnimal(Animal animal) throws AnimalNotFoundException {
        if(animal == null) {
            throw new AnimalNotFoundException("Zwierze nie istnieje");
        }
        System.out.println("Modifying animal " + animal.getAnimalName());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/studia/animalshelterdesktopapp/views/ModifyAnimalForm.fxml"));
            Region root = loader.load();

            ModifyAnimalForm modifyForm = loader.getController();
            modifyForm.setAnimal(animal);
            modifyForm.initValues();
            modifyForm.setShelter(this.selectedShelter);
            modifyForm.setAdminView(this);

            Stage stage = new Stage();
            stage.setTitle("Modify Animal");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load FXML file.");
        }
    }

    private void deleteAnimal(Animal animal) throws AnimalNotFoundException {
        try {
            if (animal == null) {
                throw new AnimalNotFoundException("Zwierze nie istnieje.");
            }
            System.out.println("Deleting animal: " + animal.getAnimalName());
            manager.removeAnimalFromShelter(selectedShelter.getId(), animal.getId());
            loadAnimalsForSelectedShelter(selectedShelter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
