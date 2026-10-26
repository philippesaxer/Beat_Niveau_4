package ch.bms.beatmanager.model;

/**
 * Supported track types. Avoids hardcoded type strings across the codebase.
 */
public enum TrackType {
    SAMPLE("Audio-Sample Spur"),
    MIDI("MIDI / VST-Instrument"),
    DRUM("Drum-Kit / Percussion");

    private final String displayName;

    TrackType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}