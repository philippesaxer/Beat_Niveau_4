package ch.bms.beatmanager.pricing;

import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.model.Track;

/**
 * Pricing strategy for entry-level non-exclusive MP3 leases.
 */
public class BasicLeaseStrategy implements PricingStrategy {

    private static final double BASE_PRICE_CHF = 29.90;
    private static final double SURCHARGE_FACTOR = 0.25;

    @Override
    public double calculateTotal(BeatProject project) {
        double trackSurcharges = 0.0;
        for (Track track : project.getTracks()) {
            trackSurcharges += track.getLicensingSurcharge() * SURCHARGE_FACTOR;
        }
        return roundToFiveRappen(BASE_PRICE_CHF + trackSurcharges);
    }

    @Override
    public String getLicenseName() {
        return "Basic MP3 Lease (Non-Exclusive)";
    }

    @Override
    public String getLicenseConditions() {
        return "MP3-Datei (320 kbps), max. 50'000 Streams, Beat bleibt weiter im Verkauf.";
    }

    private double roundToFiveRappen(double amount) {
        return Math.round(amount * 20.0) / 20.0;
    }
}