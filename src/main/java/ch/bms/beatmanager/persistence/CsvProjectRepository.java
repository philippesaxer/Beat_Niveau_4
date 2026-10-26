package ch.bms.beatmanager.persistence;

import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.model.DrumTrack;
import ch.bms.beatmanager.model.MidiTrack;
import ch.bms.beatmanager.model.MusicalKey;
import ch.bms.beatmanager.model.SampleTrack;
import ch.bms.beatmanager.model.Track;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CSV-based implementation of the Repository interface.
 * AI-Declaration: CSV serialization and parsing logic generated with AI assistance.
 */
public class CsvProjectRepository implements Repository<BeatProject> {

    private static final String PROJECT_PREFIX = "PROJECT";
    private static final String TRACK_PREFIX = "TRACK";
    private static final String DELIMITER = ";";

    private final String filePath;

    public CsvProjectRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveAll(List<BeatProject> projects) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (BeatProject project : projects) {
                writer.write(formatProjectLine(project));
                writer.newLine();
                for (Track track : project.getTracks()) {
                    writer.write(TRACK_PREFIX + DELIMITER + project.getId() + DELIMITER + track.toCsvString());
                    writer.newLine();
                }
            }
        }
    }

    @Override
    public List<BeatProject> loadAll() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        List<BeatProject> projects = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processCsvLine(line, projects);
            }
        }
        return projects;
    }

    @Override
    public Optional<BeatProject> findById(String id) throws IOException {
        return loadAll().stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    private String formatProjectLine(BeatProject project) {
        return String.join(DELIMITER,
                PROJECT_PREFIX,
                project.getId(),
                project.getTitle(),
                String.valueOf(project.getBpm()),
                project.getMusicalKey().name(),
                project.getGenre());
    }

    private void processCsvLine(String line, List<BeatProject> projects) {
        if (line.isBlank()) {
            return;
        }
        String[] parts = line.split(DELIMITER);
        if (PROJECT_PREFIX.equals(parts[0]) && parts.length >= 6) {
            BeatProject project = new BeatProject(
                    parts[1],
                    parts[2],
                    Integer.parseInt(parts[3]),
                    MusicalKey.fromString(parts[4]),
                    parts[5]
            );
            projects.add(project);
        } else if (TRACK_PREFIX.equals(parts[0]) && parts.length >= 7) {
            attachTrackToProject(parts, projects);
        }
    }

    private void attachTrackToProject(String[] parts, List<BeatProject> projects) {
        String projectId = parts[1];
        String trackType = parts[2];
        String name = parts[3];
        double volume = Double.parseDouble(parts[4]);

        Track track = null;
        if ("SAMPLE".equals(trackType)) {
            track = new SampleTrack(name, volume, parts[5], Boolean.parseBoolean(parts[6]));
        } else if ("MIDI".equals(trackType)) {
            track = new MidiTrack(name, volume, parts[5], parts[6]);
        } else if ("DRUM".equals(trackType)) {
            track = new DrumTrack(name, volume, parts[5], Integer.parseInt(parts[6]));
        }

        if (track != null) {
            for (BeatProject project : projects) {
                if (project.getId().equals(projectId)) {
                    project.addTrack(track);
                    break;
                }
            }
        }
    }
}