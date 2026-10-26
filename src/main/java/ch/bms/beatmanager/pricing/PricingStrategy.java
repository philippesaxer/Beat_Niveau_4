package ch.bms.beatmanager.pricing;

import ch.bms.beatmanager.exception.SampleNotClearedException;
import ch.bms.beatmanager.model.BeatProject;

/**
 * Strategy pattern interface for calculating beat license prices.
 */
public interface PricingStrategy {

    double calculateTotal(BeatProject project) throws SampleNotClearedException;

    String getLicenseName();

    String getLicenseConditions();
}