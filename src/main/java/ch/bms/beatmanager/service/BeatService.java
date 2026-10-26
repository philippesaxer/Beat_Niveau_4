package ch.bms.beatmanager.service;

import ch.bms.beatmanager.exception.InvalidInputException;
import ch.bms.beatmanager.exception.SampleNotClearedException;
import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.model.DrumTrack;
import ch.bms.beatmanager.model.MidiTrack;
import ch.bms.beatmanager.model.MusicalKey;
import ch.bms.beatmanager.model.SampleTrack;
import ch.bms.beatmanager.model.Track;
import ch.bms.beatmanager.model.TrackType;
import ch.bms.beatmanager.persistence.Repository;
import ch.bms.beatmanager.pricing.PricingStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Core service handling business logic, input validation, and delegation to persistence/factories.
 */
public class BeatService {

    public static final int MIN_BPM = 40;
    public static final int MAX_BPM = 300;

    // Aggregation via constructor injection
    private final Repository<BeatProject> repository;
    private final List<BeatProject> projects;
    private int nextIdCounter = 1;

    public BeatService(Repository<BeatProject> repository) {
        this.repository = repository;
        this.projects = new ArrayList<>();
    }

    /**
     * Loads projects from storage or seeds initial demo data if storage is empty.
     */
    public void initializeData() throws IOException {
        List<BeatProject> loaded = repository.loadAll();
        projects.clear();
        if (loaded.isEmpty()) {
            seedDemoProjects();
            saveProjects();
        } else {
            projects.addAll(loaded);
            updateIdCounter();
        }
    }

    public void saveProjects() throws IOException {
        repository.saveAll(projects);
    }

    public BeatProject createProject(String title, int bpm, MusicalKey key, String genre) throws InvalidInputException {
        validateProjectInputs(title, bpm, genre);
        String id = "B" + (nextIdCounter++);
        BeatProject newProject = new BeatProject(id, title.trim(), bpm, key, genre.trim());
        projects.add(newProject);
        return newProject;
    }

    public Track addTrackToProject(
            String projectId,
            TrackType type,
            String trackName,
            double volumeDb,
            String primaryDetail,
            String secondaryDetail) throws InvalidInputException {

        BeatProject project = getProjectByIdOrThrow(projectId);
        Track createdTrack = TrackFactory.createTrack(type, trackName, volumeDb, primaryDetail, secondaryDetail);
        project.addTrack(createdTrack);
        return createdTrack;
    }

    public double calculateProjectPrice(String projectId, PricingStrategy strategy)
            throws InvalidInputException, SampleNotClearedException {

        BeatProject project = getProjectByIdOrThrow(projectId);
        if (project.getTracks().isEmpty()) {
            throw new InvalidInputException("Preisberechnung nicht möglich: Das Projekt enthält noch keine Spuren.");
        }
        return project.calculatePrice(strategy);
    }

    public boolean deleteProject(String projectId) {
        return projects.removeIf(p -> p.getId().equalsIgnoreCase(projectId.trim()));
    }

    public List<BeatProject> findProjectsByBpmRange(int minBpm, int maxBpm) throws InvalidInputException {
        if (minBpm > maxBpm) {
            throw new InvalidInputException("Minimaler BPM-Wert darf nicht grösser als der maximale BPM-Wert sein.");
        }
        return projects.stream()
                .filter(p -> p.getBpm() >= minBpm && p.getBpm() <= maxBpm)
                .collect(Collectors.toList());
    }

    public List<BeatProject> getAllProjects() {
        return new ArrayList<>(projects);
    }

    public BeatProject getProjectByIdOrThrow(String projectId) throws InvalidInputException {
        Optional<BeatProject> found = projects.stream()
                .filter(p -> p.getId().equalsIgnoreCase(projectId.trim()))
                .findFirst();
        if (found.isEmpty()) {
            throw new InvalidInputException("Kein Beat-Projekt mit der ID '" + projectId + "' gefunden.");
        }
        return found.get();
    }

    private void validateProjectInputs(String title, int bpm, String genre) throws InvalidInputException {
        if (title == null || title.isBlank()) {
            throw new InvalidInputException("Der Projekttitel darf nicht leer sein.");
        }
        if (bpm < MIN_BPM || bpm > MAX_BPM) {
            throw new InvalidInputException(
                    "Ungültiger BPM-Wert (" + bpm + "). Erlaubt sind Werte zwischen " + MIN_BPM + " und " + MAX_BPM + " BPM.");
        }
        if (genre == null || genre.isBlank()) {
            throw new InvalidInputException("Das Genre darf nicht leer sein.");
        }
    }

    private void updateIdCounter() {
        for (BeatProject project : projects) {
            String numericPart = project.getId().replaceAll("[^0-9]", "");
            if (!numericPart.isEmpty()) {
                int idNum = Integer.parseInt(numericPart);
                if (idNum >= nextIdCounter) {
                    nextIdCounter = idNum + 1;
                }
            }
        }
    }

    private void seedDemoProjects() {
        BeatProject trapBeat = new BeatProject("B1", "Midnight Marseille", 142, MusicalKey.F_MINOR, "French Trap");
        trapBeat.addTrack(
                new MidiTrack("Dark Pad", -4.5, "FLEX", "Dark Strings"),
                new DrumTrack("808 & Crispy Hats", -1.0, "Metro Boomin Kit", 16)
        );
        trapBeat.addTrack(new SampleTrack("Vintage Flute Loop", -6.0, "70s Vinyl Rip", false));

        BeatProject drillBeat = new BeatProject("B2", "Zurich Night Drive", 148, MusicalKey.C_MINOR, "Melodic Drill");
        drillBeat.addTrack(
                new MidiTrack("Grand Piano", -3.0, "Sytrus", "Concert Hall Piano"),
                new DrumTrack("Drill Bounce Drums", -0.5, "UK Drill Essentials", 32)
        );
        drillBeat.addTrack(new SampleTrack("Vocal Chop Atmos", -8.0, "Splice Royalty Free", true));

        projects.add(trapBeat);
        projects.add(drillBeat);
        nextIdCounter = 3;
    }
}