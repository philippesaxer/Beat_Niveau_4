package ch.bms.beatmanager.model;

/**
 * Represents an audio sample track with copyright clearance state.
 */
public class SampleTrack extends Track {

    private static final double CLEARED_SAMPLE_FEE = 20.0;
    private static final double UNCLEARED_BASE_FEE = 5.0;

    private String sampleSource;
    private boolean isCleared;

    public SampleTrack(String name, double volumeDb, String sampleSource, boolean isCleared) {
        super(name, volumeDb);
        this.sampleSource = sampleSource;
        this.isCleared = isCleared;
    }

    public SampleTrack(String name, String sampleSource, boolean isCleared) {
        super(name);
        this.sampleSource = sampleSource;
        this.isCleared = isCleared;
    }

    @Override
    public String getTrackSummary() {
        String clearanceText = isCleared ? "Cleared (Lizenzfrei)" : "NICHT Cleared (Risiko!)";
        return String.format("[SAMPLE] %-18s | Vol: %5.1f dB | Quelle: %-15s | Status: %s",
                getName(), getVolumeDb(), sampleSource, clearanceText);
    }

    @Override
    public double getLicensingSurcharge() {
        return isCleared ? CLEARED_SAMPLE_FEE : UNCLEARED_BASE_FEE;
    }

    @Override
    public String toCsvString() {
        return String.join(";", "SAMPLE", getName(), String.valueOf(getVolumeDb()), sampleSource, String.valueOf(isCleared));
    }

    public String getSampleSource() {
        return sampleSource;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }
}