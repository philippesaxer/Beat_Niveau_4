package ch.bms.beatmanager.model;

/**
 * Abstract base class for all audio and instrument tracks.
 */
public abstract class Track {

    // Clean Code: Named constants instead of magic numbers
    public static final double DEFAULT_VOLUME_DB = 0.0;
    public static final double MIN_VOLUME_DB = -60.0;
    public static final double MAX_VOLUME_DB = 6.0;

    private String name;
    private double volumeDb;
    private boolean isMuted;

    public Track(String name, double volumeDb) {
        this.name = name;
        this.volumeDb = volumeDb;
        this.isMuted = false;
    }

    // Overloaded constructor using default volume (0.0 dB)
    public Track(String name) {
        this(name, DEFAULT_VOLUME_DB);
    }

    public abstract String getTrackSummary();

    public abstract double getLicensingSurcharge();

    public abstract String toCsvString();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolumeDb() {
        return volumeDb;
    }

    public void setVolumeDb(double volumeDb) {
        this.volumeDb = volumeDb;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}