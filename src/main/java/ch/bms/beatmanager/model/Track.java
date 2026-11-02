package ch.bms.beatmanager.model;

/**
 * Abstract base class for all audio and instrument tracks.
 */
public abstract class Track {

    public static final double DEFAULT_VOLUME_DB = 0.0;
    public static final double MIN_VOLUME_DB = -60.0;
    public static final double MAX_VOLUME_DB = 6.0;

    private String name;
    private double volumeDb;

    public Track(String name, double volumeDb) {
        this.name = name;
        this.volumeDb = volumeDb;
    }

    public abstract String getTrackSummary();

    public abstract double getLicensingSurcharge();

    public abstract String toCsvString();

    public String getName() {
        return name;
    }

    public double getVolumeDb() {
        return volumeDb;
    }
}