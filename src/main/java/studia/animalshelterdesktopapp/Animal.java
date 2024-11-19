package studia.animalshelterdesktopapp;

public class Animal implements Comparable<Animal>, Printable {
    private String animalName;
    private String animalSpecies;
    private AnimalCondition animalCondition;
    private int animalAge;
    private double animalPrice;

    public Animal(String name, String species, AnimalCondition condition, int age, double price) {
        this.animalName = name;
        this.animalSpecies = species;
        this.animalCondition = condition;
        this.animalAge = age;
        this.animalPrice = price;
    }

    public Animal(Animal animal) {
        this.animalName = animal.getAnimalName();
        this.animalSpecies = animal.getAnimalSpecies();
        this.animalCondition = animal.getAnimalCondition();
        this.animalAge = animal.getAnimalAge();
        this.animalPrice = animal.getAnimalPrice();
    }

    @Override
    public void print() {
        System.out.printf("==================================================================\nImie zwierzecia: %s\nGatunek: %s\nStan: %s\nWiek: %d\nCena: %.2f\n", this.animalName, this.animalSpecies, this.animalCondition, this.animalAge, this.animalPrice);
    }

    @Override
    public int compareTo(Animal otherAnimal) {
        if(this.animalName.compareTo(otherAnimal.animalName) != 0) {
            return this.animalName.compareTo(otherAnimal.animalName);
        }
        if(this.animalSpecies.compareTo(otherAnimal.animalSpecies) != 0) {
            return this.animalSpecies.compareTo(otherAnimal.animalSpecies);
        }
        return Integer.compare(this.animalAge, otherAnimal.animalAge);
    }

    @Override
    public int compareNameTo(Animal otherAnimal) {
        return this.animalName.compareTo(otherAnimal.animalName);
    }

    @Override
    public int compareSpeciesTo(Animal otherAnimal) {
        return this.animalSpecies.compareTo(otherAnimal.animalSpecies);
    }

    @Override
    public int compareAgeTo(Animal otherAnimal) {
        return Integer.compare(this.animalAge, otherAnimal.animalAge);
    }

    @Override
    public int comparePriceTo(Animal otherAnimal) {
        return Double.compare(this.animalPrice, otherAnimal.animalPrice);
    }

    public void setAnimalName(String name) {
        this.animalName = name;
    }

    public void setAnimalSpecies(String species) {
        this.animalSpecies = species;
    }

    public void setAnimalPrice(double price) {
        this.animalPrice = price;
    }

    public void setAnimalCondition(AnimalCondition animalCondition) {
        this.animalCondition = animalCondition;
    }

    public void setAnimalAge(int animalAge) {
        this.animalAge = animalAge;
    }

    public AnimalCondition getAnimalCondition() {
        return this.animalCondition;
    }

    public String getAnimalName() {
        return this.animalName;
    }

    public String getAnimalSpecies() {
        return this.animalSpecies;
    }

    public int getAnimalAge() {return this.animalAge;}

    public double getAnimalPrice() {return this.animalPrice;}
}
