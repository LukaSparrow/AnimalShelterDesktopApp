package studia.animalshelterdesktopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studia.animalshelterdesktopapp.exceptions.InvalidCapacityException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class DataGeneratorTest {

    private DataGenerator dataGenerator;
    private ShelterManager shelterManager;

    @BeforeEach
    void setUp() {
        dataGenerator = DataGenerator.getInstance();
        shelterManager = dataGenerator.generateData();
    }

    // Test sprawdzający, czy DataGenerator jest singletonem
    @Test
    void testSingleton() {
        DataGenerator dataGenerator1 = DataGenerator.getInstance();
        DataGenerator dataGenerator2 = DataGenerator.getInstance();
        assertSame(dataGenerator1, dataGenerator2, "DataGenerator should be a singleton.");
    }

    // Test sprawdzający generowanie schronisk
    @Test
    void testGenerateShelters() {
        // Ensure shelterManager is not null
        assertNotNull(shelterManager, "Shelter manager should not be null.");

        // Get the shelters map
        Map<String, AnimalShelter> shelters = shelterManager.getShelters();

        // Ensure there is a reasonable number of shelters (at least 1, or better more)
        assertFalse(shelters.isEmpty(), "There should be at least 1 shelter.");

        // Ensure each shelter has a non-empty, non-null name
        shelters.forEach((name, shelter) -> {
            assertNotNull(name, "Shelter name should not be null.");
            assertFalse(name.isEmpty(), "Shelter name should not be empty.");
        });

        // Ensure all shelter names are unique
        Set<String> uniqueNames = new HashSet<>(shelters.keySet());
        assertEquals(shelters.size(), uniqueNames.size(), "All shelter names should be unique.");
    }

    // Test sprawdzający, czy generowanie zwierząt nie przekracza pojemności schronisk
    @Test
    void testNoExceedingShelterCapacity() {
        shelterManager.getShelters().forEach((key, shelter) -> {
            List<Animal> animals = shelter.getAnimalList();
            assertTrue(animals.size() <= shelter.getMaxCapacity(),
                    "Number of animals in shelter " + key + " should not exceed its capacity.");
        });
    }

    // Test sprawdzający poprawność losowania zwierząt
    @Test
    void testGeneratedAnimals() {
        shelterManager.getShelters().forEach((key, shelter) -> shelter.getAnimalList().forEach(animal -> {
            assertNotNull(animal.getAnimalName(), "Animal name should not be null.");
            assertNotNull(animal.getAnimalSpecies(), "Animal species should not be null.");
            assertNotNull(animal.getAnimalCondition(), "Animal condition should not be null.");
            assertTrue(animal.getAnimalAge() >= 1 && animal.getAnimalAge() <= 25,
                    "Animal age should be between 1 and 25.");
            assertTrue(animal.getAnimalPrice() >= 100 && animal.getAnimalPrice() <= 1000,
                    "Animal price should be between 100 and 1000.");
        }));
    }

    // Test sprawdzający, czy generowanie danych nie powoduje żadnych wyjątków
    @Test
    void testGenerateDataWithoutExceptions() {
        assertDoesNotThrow(() -> {
            ShelterManager manager = dataGenerator.generateData();
            assertNotNull(manager);
            assertFalse(manager.getShelters().isEmpty(), "At least one shelter should be generated.");
        }, "Generating data should not throw any exceptions.");
    }

    // Test sprawdzający, czy schroniska mają zwierzęta
    @Test
    void testSheltersHaveAnimals() {
        shelterManager.getShelters().forEach((key, shelter) -> assertTrue(shelter.getAnimalCount() > 0, "Shelter should have at least one animal."));
    }

    // Test sprawdzający, czy liczba zwierząt w schronisku nie przekracza pojemności
    @Test
    void testShelterCapacity() {
        shelterManager.getShelters().forEach((key, shelter) -> assertTrue(shelter.getAnimalCount() <= shelter.getMaxCapacity(),
                "Animal count should not exceed shelter capacity."));
    }

    // Test, czy metody getShelterNames(), getAnimalNames(), getAnimalSpecies() zwracają niepustą tablicę
    @Test
    void testGetterMethods() {
        assertNotNull(DataGenerator.getShelterNames(), "Shelter names should not be null.");
        assertTrue(DataGenerator.getShelterNames().length > 0, "There should be some shelter names.");

        assertNotNull(DataGenerator.getAnimalNames(), "Animal names should not be null.");
        assertTrue(DataGenerator.getAnimalNames().length > 0, "There should be some animal names.");

        assertNotNull(DataGenerator.getAnimalSpecies(), "Animal species should not be null.");
        assertTrue(DataGenerator.getAnimalSpecies().length > 0, "There should be some animal species.");
    }

    // Test sprawdzający, czy wyjątek InvalidCapacityException jest rzucany dla nieprawidłowej pojemności
    @Test
    void testInvalidCapacityException() {
        ShelterManager invalidShelterManager = new ShelterManager();
        assertThrows(InvalidCapacityException.class, () -> invalidShelterManager.addShelter(
                new AnimalShelter(
                "Invalid Shelter", 0)
                ),
                "Should throw InvalidCapacityException when trying to add shelter with invalid capacity."
        );
    }
}
