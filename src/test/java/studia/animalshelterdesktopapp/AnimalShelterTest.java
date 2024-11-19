package studia.animalshelterdesktopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studia.animalshelterdesktopapp.exceptions.AnimalAlreadyExistsException;
import studia.animalshelterdesktopapp.exceptions.InvalidCapacityException;
import studia.animalshelterdesktopapp.exceptions.NotEnoughCapacityException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalShelterTest {

    private AnimalShelter shelter;
    private Animal dog;
    private Animal cat;

    @BeforeEach
    void setUp() {
        shelter = new AnimalShelter("Happy Paws", 5);
        dog = new Animal("Buddy", "Dog", AnimalCondition.ZDROWE, 3, 200.0);
        cat = new Animal("Mittens", "Cat", AnimalCondition.CHORE, 2, 150.0);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Happy Paws", shelter.getShelterName());
        assertEquals(5, shelter.getMaxCapacity());
        assertEquals(0, shelter.getAnimalCount());
        assertTrue(shelter.getAnimalList().isEmpty());
    }

    @Test
    void testAddAnimalSuccess() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        assertEquals(1, shelter.getAnimalCount());
        assertTrue(shelter.getAnimalList().contains(dog));
    }

    @Test
    void testAddAnimalAlreadyExistsException() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        assertThrows(AnimalAlreadyExistsException.class, () -> shelter.addAnimal(dog));
    }

    @Test
    void testAddAnimalNotEnoughCapacityException() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        for (int i = 0; i < 5; i++) {
            shelter.addAnimal(new Animal("Animal" + i, "Species", AnimalCondition.ZDROWE, 1, 100.0));
        }
        assertThrows(NotEnoughCapacityException.class, () -> shelter.addAnimal(cat));
    }

    @Test
    void testRemoveAnimal() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        shelter.removeAnimal(dog);
        assertEquals(0, shelter.getAnimalCount());
        assertFalse(shelter.getAnimalList().contains(dog));
    }

    @Test
    void testChangeCondition() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(cat);
        shelter.changeCondition(cat, AnimalCondition.KWARANTANNA);
        assertEquals(AnimalCondition.KWARANTANNA, cat.getAnimalCondition());
    }

    @Test
    void testSortByName() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(cat);
        shelter.addAnimal(dog);
        shelter.sortByName();

        List<Animal> sortedList = shelter.getAnimalList();
        assertEquals("Buddy", sortedList.get(0).getAnimalName());
        assertEquals("Mittens", sortedList.get(1).getAnimalName());
    }

    @Test
    void testSearchByName() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        Animal foundAnimal = shelter.search("Buddy");
        assertNotNull(foundAnimal);
        assertEquals(dog, foundAnimal);
    }

    @Test
    void testSearchPartial() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);

        List<Animal> foundAnimals = shelter.searchPartial("itt");
        assertEquals(1, foundAnimals.size());
        assertEquals("Mittens", foundAnimals.getFirst().getAnimalName());
    }

    @Test
    void testMax() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);
        Animal mostExpensiveAnimal = shelter.max();
        assertEquals(dog, mostExpensiveAnimal);
    }

    @Test
    void testSetShelterName() {
        shelter.setShelterName("New Name");
        assertEquals("New Name", shelter.getShelterName());
    }

    @Test
    void testSetShelterCapacity() throws InvalidCapacityException {
        shelter.setShelterCapacity(10);
        assertEquals(10, shelter.getMaxCapacity());
    }

    @Test
    void testSetShelterCapacityInvalid() {
        assertThrows(InvalidCapacityException.class, () -> shelter.setShelterCapacity(0));
    }

    @Test
    void testCountByCondition() throws AnimalAlreadyExistsException, NotEnoughCapacityException {
        shelter.addAnimal(dog);
        shelter.addAnimal(cat);

        assertEquals(1, shelter.countByCondition(AnimalCondition.ZDROWE));
        assertEquals(1, shelter.countByCondition(AnimalCondition.CHORE));
    }
}