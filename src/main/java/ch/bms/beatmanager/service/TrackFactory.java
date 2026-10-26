package ch.bms.beatmanager.service;

import ch.bms.beatmanager.exception.InvalidInputException;
import ch.bms.beatmanager.model.DrumTrack;
import ch.bms.beatmanager.model.MidiTrack;
import ch.bms.beatmanager.model.SampleTrack;
import ch.bms.beatmanager.model.Track;
import ch.bms.beatmanager.model.TrackType;

/**
 * Simple Factory for validating parameters and instantiating Track subclasses.
 */
public class TrackFactory {

    private static final int MIN_PATTERN_BARS = 1;
    private static final int MAX_PATTERN_BARS = 64;

    private TrackFactory() {
        // Prevent instantiation of utility factory class
    }

    /**
     * Creates a specific Track subclass and returns it upcasted as Track.
     */
    public static Track createTrack(
            TrackType type,
            String name,
            double volumeDb,
            String primaryDetail,
            String secondaryDetail) throws InvalidInputException {

        validateCommonTrackInputs(name, volumeDb);

        switch (type) {
            case SAMPLE:
                boolean isCleared = Boolean.parseBoolean(secondaryDetail);
                return new SampleTrack(name.trim(), volumeDb, primaryDetail.trim(), isCleared);

            case MIDI:
                return new MidiTrack(name.trim(), volumeDb, primaryDetail.trim(), secondaryDetail.trim());

            case DRUM:
                int bars = parseAndValidateBars(secondaryDetail);
                return new DrumTrack(name.trim(), volumeDb, primaryDetail.trim(), bars);

            default:
                throw new InvalidInputException("Unbekannter Spur-Typ: " + type);
        }
    }

    private static void validateCommonTrackInputs(String name, double volumeDb) throws InvalidInputException {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Der Name der Spur darf nicht leer sein.");
        }
        if (volumeDb < Track.MIN_VOLUME_DB || volumeDb > Track.MAX_VOLUME_DB) {
            throw new InvalidInputException(
                    String.format("Lautstärke muss zwischen %.1f dB und +%.1f dB liegen.",
                            Track.MIN_VOLUME_DB, Track.MAX_VOLUME_DB));
        }
    }

    private static int parseAndValidateBars(String barsInput) throws InvalidInputException {
        try {
            int bars = Integer.parseInt(barsInput.trim());
            if (bars < MIN_PATTERN_BARS || bars > MAX_PATTERN_BARS) {
                throw new InvalidInputException(
                        "Drum-Pattern-Länge muss zwischen " + MIN_PATTERN_BARS + " und " + MAX_PATTERN_BARS + " Bars liegen.");
            }
            return bars;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Ungültige Zahl für Pattern-Länge (Bars): " + barsInput, e);
        }
    }
}