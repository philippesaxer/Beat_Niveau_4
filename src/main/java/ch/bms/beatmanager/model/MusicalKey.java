package ch.bms.beatmanager.model;

/**
 * Standard musical keys for beat projects.
 */
public enum MusicalKey {
    C_MINOR("C Minor"),
    D_MINOR("D Minor"),
    E_MINOR("E Minor"),
    F_MINOR("F Minor"),
    F_SHARP_MINOR("F# Minor"),
    G_MINOR("G Minor"),
    A_MINOR("A Minor"),
    C_MAJOR("C Major"),
    G_MAJOR("G Major");

    private final String label;

    MusicalKey(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Parses user input into a MusicalKey, falling back to C_MINOR if unrecognized.
     */
    public static MusicalKey fromString(String input) {
        if (input == null) {
            return C_MINOR;
        }
        String normalized = input.trim().toUpperCase();
        for (MusicalKey key : values()) {
            if (key.name().equalsIgnoreCase(normalized) || key.label.equalsIgnoreCase(input.trim())) {
                return key;
            }
        }
        return C_MINOR;
    }
}