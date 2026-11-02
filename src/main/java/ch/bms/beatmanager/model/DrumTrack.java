package ch.bms.beatmanager.model;

/**
 * Represents a drum or percussion pattern track.
 */
public class DrumTrack extends Track {

    private static final double FEE_PER_BAR = 0.50;

    private String drumKitName;
    private int patternLengthBars;

    public DrumTrack(String name, double volumeDb, String drumKitName, int patternLengthBars) {
        super(name, volumeDb);
        this.drumKitName = drumKitName;
        this.patternLengthBars = patternLengthBars;
    }

    @Override
    public String getTrackSummary() {
        return String.format("[DRUM]   %-18s | Vol: %5.1f dB | Kit: %-18s | Länge: %d Bars",
                getName(), getVolumeDb(), drumKitName, patternLengthBars);
    }

    @Override
    public double getLicensingSurcharge() {
        return patternLengthBars * FEE_PER_BAR;
    }

    @Override
    public String toCsvString() {
        return String.join(";", "DRUM", getName(), String.valueOf(getVolumeDb()), drumKitName, String.valueOf(patternLengthBars));
    }

}