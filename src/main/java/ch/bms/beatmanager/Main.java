package ch.bms.beatmanager;

import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.persistence.CsvProjectRepository;
import ch.bms.beatmanager.persistence.Repository;
import ch.bms.beatmanager.service.BeatService;
import ch.bms.beatmanager.ui.ConsoleUI;

/**
 * Application entry point wiring persistence, service, and UI layers.
 */
public class Main {

    private static final String DEFAULT_STORAGE_FILE = "beats_data.csv";

    public static void main(String[] args) {
        Repository<BeatProject> repository = new CsvProjectRepository(DEFAULT_STORAGE_FILE);
        BeatService beatService = new BeatService(repository);
        ConsoleUI ui = new ConsoleUI(beatService);

        ui.start();
    }
}