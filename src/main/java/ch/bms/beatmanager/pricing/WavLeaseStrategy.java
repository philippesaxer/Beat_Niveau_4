package ch.bms.beatmanager.pricing;

import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.model.Track;

/**
 * Pricing strategy for standard commercial WAV + trackout stems leases.
 */
public class WavLeaseStrategy implements PricingStrategy {

    private static final double BASE_PRICE_CHF = 79.90;
    private static final double SURCHARGE_FACTOR = 0.60;
    private static final double STEM_EXPORT_FEE_PER_TRACK = 3.00;

    @Override
    public double calculateTotal(BeatProject project) {
        double total = BASE_PRICE_CHF;
        for (Track track : project.getTracks()) {
            total += (track.getLicensingSurcharge() * SURCHARGE_FACTOR) + STEM_EXPORT_FEE_PER_TRACK;
        }
        return Math.round(total * 20.0) / 20.0;
    }

    @Override
    public String getLicenseName() {
        return "Premium WAV & Trackout Lease";
    }

    @Override
    public String getLicenseConditions() {
        return "WAV + Einzelspuren (Stems), max. 500'000 Streams, kommerzielle Nutzung erlaubt.";
    }
}