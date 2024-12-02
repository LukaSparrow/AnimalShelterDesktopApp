package studia.animalshelterdesktopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studia.animalshelterdesktopapp.exceptions.AnimalAlreadyExistsException;
import studia.animalshelterdesktopapp.exceptions.InvalidCapacityException;
import studia.animalshelterdesktopapp.exceptions.NotEnoughCapacityException;
import studia.animalshelterdesktopapp.exceptions.ShelterAlreadyExistsException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShelterManagerTest {

    private ShelterManager shelterManager;
    private AnimalShelter shelter1;
    private AnimalShelter shelter2;

    @BeforeEach
    void setUp() {
        shelterManager = new ShelterManager();
        shelter1 = new AnimalShelter("Happy Paws", 10);
        shelter2 = new AnimalShelter("Safe Haven", 5);
    }

    @Test
    void testConstructor() {
        assertNotNull(shelterManager);
        assertTrue(shelterManager.getAllShelters().isEmpty());
    }

    @Test
    void testAddShelterAlreadyExistsException() throws InvalidCapacityException, ShelterAlreadyExistsException {
        shelterManager.addShelter(shelter1);
        assertThrows(ShelterAlreadyExistsException.class, () -> shelterManager.addShelter(shelter1));
    }

    @Test
    void testAddShelterInvalidCapacityException() {
        assertThrows(InvalidCapacityException.class, () -> shelterManager.addShelter(new AnimalShelter("Test Shelter", -1)));
    }

    @Test
    void testGetShelter() throws InvalidCapacityException, ShelterAlreadyExistsException {
        shelterManager.addShelter(shelter1);
        AnimalShelter retrievedShelter = shelterManager.getShelter(shelter1.getShelterName());
        assertNotNull(retrievedShelter);
        assertEquals(shelter1, retrievedShelter);
    }

    @Test
    void testGetShelterNotFound() {
        AnimalShelter retrievedShelter = shelterManager.getShelter("NonExistent Shelter");
        assertNull(retrievedShelter);
    }

    @Test
    void testRemoveShelterByName() throws InvalidCapacityException, ShelterAlreadyExistsException {
        shelterManager.addShelter(shelter1);
        shelterManager.removeShelter(shelter1.getShelterName());
        assertFalse(shelterManager.getShelter(shelter1.getShelterName()) != null);
    }

    @Test
    void testRemoveShelterNotFound() {
        shelterManager.removeShelter("NonExistent Shelter");
        // Nic się nie dzieje, ale nie rzuca się wyjątek
        assertTrue(shelterManager.getAllShelters().isEmpty());
    }

    @Test
    void testFindEmptyShelters() throws InvalidCapacityException, ShelterAlreadyExistsException, NotEnoughCapacityException, AnimalAlreadyExistsException {
        shelterManager.addShelter(shelter1);
        shelterManager.addShelter(shelter2);

        List<AnimalShelter> emptyShelters = shelterManager.findEmptyShelters();
        assertNotNull(emptyShelters);
        assertFalse(emptyShelters.isEmpty());

        shelter2.addAnimal(new Animal("Buddy", "Dog", AnimalCondition.ZDROWE, 3, 150.0));
        emptyShelters = shelterManager.findEmptyShelters();
        assertEquals(1, emptyShelters.size());
        assertEquals(shelter1, emptyShelters.getFirst());
    }

    @Test
    void testGetAllShelters() {
        shelterManager.addShelter(shelter1);
        shelterManager.addShelter(shelter2);

        List<AnimalShelter> shelters = shelterManager.getAllShelters();
        assertEquals(2, shelters.size());
        assertTrue(shelterManager.getShelter(shelter1.getShelterName()).equals(shelter1));
        assertTrue(shelterManager.getShelter(shelter2.getShelterName()).equals(shelter2));
    }
}
