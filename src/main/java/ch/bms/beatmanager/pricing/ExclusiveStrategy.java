package ch.bms.beatmanager.pricing;

import ch.bms.beatmanager.exception.SampleNotClearedException;
import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.model.SampleTrack;
import ch.bms.beatmanager.model.Track;

/**
 * Pricing strategy for full exclusive rights buyout.
 * Enforces strict copyright clearance on all sample tracks.
 */
public class ExclusiveStrategy implements PricingStrategy {

    private static final double BASE_EXCLUSIVE_PRICE_CHF = 350.00;
    private static final double EXCLUSIVE_MULTIPLIER = 2.5;

    @Override
    public double calculateTotal(BeatProject project) throws SampleNotClearedException {
        double total = BASE_EXCLUSIVE_PRICE_CHF;

        for (Track track : project.getTracks()) {
            verifySampleClearance(track);
            total += track.getLicensingSurcharge() * EXCLUSIVE_MULTIPLIER;
        }
        return Math.round(total * 20.0) / 20.0;
    }

    /**
     * Downcasts Track to SampleTrack to verify sample clearance before exclusive sale.
     */
    private void verifySampleClearance(Track track) throws SampleNotClearedException {
        if (track instanceof SampleTrack) {
            SampleTrack sampleTrack = (SampleTrack) track;
            if (!sampleTrack.isCleared()) {
                throw new SampleNotClearedException(
                        "Exklusiv-Verkauf abgelehnt! Das verwendete Sample '" + sampleTrack.getName()
                                + "' (Quelle: " + sampleTrack.getSampleSource() + ") ist rechtlich nicht geklärt.",
                        sampleTrack.getName()
                );
            }
        }
    }

    @Override
    public String getLicenseName() {
        return "Exclusive Rights";
    }

    @Override
    public String getLicenseConditions() {
        return "Unlimitierte Streams und Verkäufe, inkl. Stems. Beat wird sofort aus dem Store entfernt.";
    }
}