package studia.animalshelterdesktopapp;

public enum AnimalCondition {
    ZDROWE("Zdrowe"),
    CHORE("Chore"),
    W_TRAKCIE_ADOPCJI("W trakcie adopcji"),
    KWARANTANNA("Kwarantanna");

    private String label;

    AnimalCondition(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}