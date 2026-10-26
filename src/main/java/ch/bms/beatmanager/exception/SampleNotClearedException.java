package ch.bms.beatmanager.exception;

/**
 * Thrown when attempting an exclusive license calculation on a beat containing uncleared samples.
 */
public class SampleNotClearedException extends Exception {

    private final String sampleName;

    public SampleNotClearedException(String message, String sampleName) {
        super(message);
        this.sampleName = sampleName;
    }

    public String getSampleName() {
        return sampleName;
    }
}