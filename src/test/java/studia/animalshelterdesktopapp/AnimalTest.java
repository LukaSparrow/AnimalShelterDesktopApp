package studia.animalshelterdesktopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalTest {
    private Animal animal1;
    private Animal animal2;
    private Animal animal3;

    @BeforeEach
    void setUp() {
        animal1 = new Animal("Leo", "Dog", AnimalCondition.ZDROWE, 3, 150.0);
        animal2 = new Animal("Milo", "Cat", AnimalCondition.CHORE, 2, 80.0);
        animal3 = new Animal("Leo", "Dog", AnimalCondition.ZDROWE, 3, 150.0);
    }

    @Test
    void testAnimalConstructorAndGetters() {
        assertEquals("Leo", animal1.getAnimalName());
        assertEquals("Dog", animal1.getAnimalSpecies());
        assertEquals(AnimalCondition.ZDROWE, animal1.getAnimalCondition());
        assertEquals(3, animal1.getAnimalAge());
        assertEquals(150.0, animal1.getAnimalPrice());
    }

    @Test
    void testCopyConstructor() {
        Animal copy = new Animal(animal1);
        assertEquals(animal1.getAnimalName(), copy.getAnimalName());
        assertEquals(animal1.getAnimalSpecies(), copy.getAnimalSpecies());
        assertEquals(animal1.getAnimalCondition(), copy.getAnimalCondition());
        assertEquals(animal1.getAnimalAge(), copy.getAnimalAge());
        assertEquals(animal1.getAnimalPrice(), copy.getAnimalPrice());
    }

    @Test
    void testSetters() {
        animal1.setAnimalName("Max");
        animal1.setAnimalSpecies("Rabbit");
        animal1.setAnimalAge(5);
        animal1.setAnimalPrice(200.0);
        animal1.setAnimalCondition(AnimalCondition.CHORE);

        assertEquals("Max", animal1.getAnimalName());
        assertEquals("Rabbit", animal1.getAnimalSpecies());
        assertEquals(5, animal1.getAnimalAge());
        assertEquals(200.0, animal1.getAnimalPrice());
        assertEquals(AnimalCondition.CHORE, animal1.getAnimalCondition());
    }

    @Test
    void testCompareTo() {
        assertEquals(0, animal1.compareTo(animal3)); // Są identyczne
        assertTrue(animal1.compareTo(animal2) < 0); // "Leo" < "Milo"
    }

    @Test
    void testCompareNameTo() {
        assertTrue(animal1.compareNameTo(animal2) < 0); // "Leo" < "Milo"
        assertEquals(0, animal1.compareNameTo(animal3)); // Te same nazwy
    }

    @Test
    void testCompareSpeciesTo() {
        assertTrue(animal1.compareSpeciesTo(animal2) > 0); // "Dog" > "Cat"
    }

    @Test
    void testCompareAgeTo() {
        assertTrue(animal1.compareAgeTo(animal2) > 0); // 3 > 2
        assertEquals(0, animal1.compareAgeTo(animal3)); // Te same wieki
    }

    @Test
    void testComparePriceTo() {
        assertTrue(animal1.comparePriceTo(animal2) > 0); // 150.0 > 80.0
        assertEquals(0, animal1.comparePriceTo(animal3)); // Te same ceny
    }
}