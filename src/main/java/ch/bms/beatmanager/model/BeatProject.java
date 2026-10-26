package ch.bms.beatmanager.model;

import ch.bms.beatmanager.exception.SampleNotClearedException;
import ch.bms.beatmanager.pricing.PricingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Domain model representing a beat project and its associated tracks.
 */
public class BeatProject {

    private final String id;
    private String title;
    private int bpm;
    private MusicalKey musicalKey;
    private String genre;
    private final List<Track> tracks;

    public BeatProject(String id, String title, int bpm, MusicalKey musicalKey, String genre) {
        this.id = id;
        this.title = title;
        this.bpm = bpm;
        this.musicalKey = musicalKey;
        this.genre = genre;
        // Composition: lifecycle of the track list is bound to BeatProject
        this.tracks = new ArrayList<>();
    }

    // Delegates list management to the underlying ArrayList
    public void addTrack(Track track) {
        if (track != null) {
            this.tracks.add(track);
        }
    }

    // Overloaded convenience method to add two tracks at once
    public void addTrack(Track firstTrack, Track secondTrack) {
        addTrack(firstTrack);
        addTrack(secondTrack);
    }

    /**
     * Delegates price calculation to the provided PricingStrategy implementation.
     */
    public double calculatePrice(PricingStrategy strategy) throws SampleNotClearedException {
        return strategy.calculateTotal(this);
    }

    /**
     * Builds a formatted string of all tracks using dynamic binding on Track#getTrackSummary().
     */
    public String getFormattedTrackList() {
        if (tracks.isEmpty()) {
            return "   (Noch keine Spuren in diesem Projekt vorhanden)\n";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tracks.size(); i++) {
            Track currentTrack = tracks.get(i);
            sb.append("   ").append(i + 1).append(". ").append(currentTrack.getTrackSummary()).append("\n");
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public MusicalKey getMusicalKey() {
        return musicalKey;
    }

    public String getGenre() {
        return genre;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }
}