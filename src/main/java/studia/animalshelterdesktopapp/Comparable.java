package studia.animalshelterdesktopapp;

public interface Comparable<Animal>{
    int compareTo(Animal otherAnimal);
    int compareNameTo(Animal otherAnimal);
    int compareSpeciesTo(Animal otherAnimal);
    int compareAgeTo(Animal otherAnimal);
    int comparePriceTo(Animal otherAnimal);
}