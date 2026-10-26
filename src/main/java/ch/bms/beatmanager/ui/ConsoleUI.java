package ch.bms.beatmanager.ui;

import ch.bms.beatmanager.exception.InvalidInputException;
import ch.bms.beatmanager.exception.SampleNotClearedException;
import ch.bms.beatmanager.model.BeatProject;
import ch.bms.beatmanager.model.MusicalKey;
import ch.bms.beatmanager.model.Track;
import ch.bms.beatmanager.model.TrackType;
import ch.bms.beatmanager.pricing.BasicLeaseStrategy;
import ch.bms.beatmanager.pricing.ExclusiveStrategy;
import ch.bms.beatmanager.pricing.PricingStrategy;
import ch.bms.beatmanager.pricing.WavLeaseStrategy;
import ch.bms.beatmanager.service.BeatService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * CLI controller handling user I/O and delegating operations to BeatService.
 * AI-Declaration: Console menu layout and output formatting created with AI support.
 */
public class ConsoleUI {

    private final BeatService beatService;
    private final Scanner scanner;

    // Lazy-initialized pricing strategies
    private PricingStrategy basicStrategy;
    private PricingStrategy wavStrategy;
    private PricingStrategy exclusiveStrategy;

    public ConsoleUI(BeatService beatService) {
        this.beatService = beatService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        initializeSystem();
        boolean running = true;

        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleShowAllProjects();
                    break;
                case "2":
                    handleCreateProject();
                    break;
                case "3":
                    handleAddTrack();
                    break;
                case "4":
                    handleCalculatePrice();
                    break;
                case "5":
                    handleFilterByBpm();
                    break;
                case "6":
                    handleDeleteProject();
                    break;
                case "7":
                    handleSaveData();
                    break;
                case "0":
                    handleExit();
                    running = false;
                    break;
                default:
                    System.out.println("\n[!] Ungültige Auswahl. Bitte eine Zahl von 0 bis 7 eingeben.");
            }
        }
    }

    private void initializeSystem() {
        try {
            beatService.initializeData();
            System.out.println("======================================================================");
            System.out.println("   BEAT- & MUSIKPROJEKT-MANAGER (M4 Kompetenznachweis)");
            System.out.println("   Daten erfolgreich geladen (" + beatService.getAllProjects().size() + " Projekte bereit).");
            System.out.println("======================================================================");
        } catch (IOException e) {
            System.out.println("[FEHLER] Konnte Datei nicht laden: " + e.getMessage());
        }
    }

    private void printMainMenu() {
        System.out.println("\n---------------------------- HAUPTMENÜ -------------------------------");
        System.out.println(" [1] Alle Beat-Projekte & Spuren anzeigen (UC3 - Polymorphismus)");
        System.out.println(" [2] Neues Beat-Projekt anlegen           (UC1 - Validierung)");
        System.out.println(" [3] Spur zu Projekt hinzufügen           (UC2 - Factory Pattern)");
        System.out.println(" [4] Lizenzpreis berechnen                (UC4 - Strategy & Downcast)");
        System.out.println(" [5] Beats nach BPM-Bereich filtern       (Nice-to-Have Suche)");
        System.out.println(" [6] Beat-Projekt löschen                 (UC5)");
        System.out.println(" [7] Projekte in CSV-Datei speichern      (UC6 - Persistenz)");
        System.out.println(" [0] Speichern & Programm beenden");
        System.out.print("Deine Auswahl: ");
    }

    private void handleShowAllProjects() {
        List<BeatProject> projects = beatService.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("\nKeine Projekte vorhanden.");
            return;
        }
        System.out.println("\n======================= AKTUELLE BEAT-PROJEKTE =======================");
        for (BeatProject project : projects) {
            printProjectDetails(project);
        }
    }

    private void printProjectDetails(BeatProject project) {
        System.out.printf("ID: %-4s | Titel: %-22s | BPM: %-3d | Key: %-10s | Genre: %s%n",
                project.getId(),
                project.getTitle(),
                project.getBpm(),
                project.getMusicalKey().getLabel(),
                project.getGenre());
        System.out.print(project.getFormattedTrackList());
        System.out.println("----------------------------------------------------------------------");
    }

    private void handleCreateProject() {
        System.out.println("\n--- NEUES BEAT-PROJEKT ERSTELLEN ---");
        try {
            System.out.print("Projekttitel (z.B. 'Dark Bounce'): ");
            String title = scanner.nextLine();

            int bpm = readIntInput("Tempo in BPM (" + BeatService.MIN_BPM + "-" + BeatService.MAX_BPM + "): ");

            System.out.println("Verfügbare Tonarten: C_MINOR, D_MINOR, E_MINOR, F_MINOR, F_SHARP_MINOR, G_MINOR, A_MINOR, C_MAJOR, G_MAJOR");
            System.out.print("Tonart eingeben (z.B. 'F Minor' oder 'C_MINOR'): ");
            MusicalKey key = MusicalKey.fromString(scanner.nextLine());

            System.out.print("Genre (z.B. Trap, Drill, Reggaeton, Deutschrap): ");
            String genre = scanner.nextLine();

            BeatProject created = beatService.createProject(title, bpm, key, genre);
            System.out.println("\n[OK] Projekt '" + created.getTitle() + "' mit ID " + created.getId() + " erfolgreich angelegt!");

        } catch (InvalidInputException e) {
            System.out.println("\n[VALIDIERUNGS-FEHLER] " + e.getMessage());
        }
    }

    private void handleAddTrack() {
        System.out.println("\n--- SPUR ZU PROJEKT HINZUFÜGEN ---");
        try {
            System.out.print("Projekt-ID eingeben (z.B. B1): ");
            String projectId = scanner.nextLine().trim();
            BeatProject project = beatService.getProjectByIdOrThrow(projectId);

            System.out.println("Spur-Typ wählen: [1] Sample-Spur | [2] MIDI/VST-Spur | [3] Drum-Spur");
            System.out.print("Auswahl (1-3): ");
            TrackType type = parseTrackTypeChoice(scanner.nextLine().trim());

            System.out.print("Name der Spur (z.B. 'Lead Synth' oder 'Soul Vocal Sample'): ");
            String name = scanner.nextLine();

            double volume = readDoubleInput("Lautstärke in dB (-60.0 bis +6.0, Standard 0): ");

            String primaryDetail;
            String secondaryDetail;

            if (type == TrackType.SAMPLE) {
                System.out.print("Sample-Quelle (z.B. 'Splice' oder '70s Vinyl'): ");
                primaryDetail = scanner.nextLine();
                System.out.print("Ist das Sample rechtlich geklärt (Cleared)? (true/false): ");
                secondaryDetail = scanner.nextLine().trim();
            } else if (type == TrackType.MIDI) {
                System.out.print("Verwendetes VST-Plugin (z.B. 'FLEX', 'Sytrus', 'Omnisphere'): ");
                primaryDetail = scanner.nextLine();
                System.out.print("Preset-Name (z.B. '808 Bass' oder 'Dark Pluck'): ");
                secondaryDetail = scanner.nextLine();
            } else {
                System.out.print("Name des Drum-Kits (z.B. 'Trap Essentials Kit'): ");
                primaryDetail = scanner.nextLine();
                System.out.print("Länge des Patterns in Bars (z.B. 8, 16, 32): ");
                secondaryDetail = scanner.nextLine();
            }

            Track addedTrack = beatService.addTrackToProject(
                    project.getId(), type, name, volume, primaryDetail, secondaryDetail);
            System.out.println("\n[OK] Spur hinzugefügt: " + addedTrack.getTrackSummary());

        } catch (InvalidInputException e) {
            System.out.println("\n[FEHLER] " + e.getMessage());
        }
    }

    private void handleCalculatePrice() {
        System.out.println("\n--- LIZENZPREIS BERECHNEN (STRATEGY PATTERN) ---");
        try {
            System.out.print("Projekt-ID eingeben (z.B. B1 oder B2): ");
            String projectId = scanner.nextLine().trim();
            BeatProject project = beatService.getProjectByIdOrThrow(projectId);

            System.out.println("\nLizenzmodell wählen:");
            System.out.println(" [1] Basic MP3 Lease (Günstig, Non-Exclusive)");
            System.out.println(" [2] Premium WAV & Trackout Lease");
            System.out.println(" [3] Exclusive Rights (Prüft auf geklärte Samples!)");
            System.out.print("Auswahl (1-3): ");
            String licenseChoice = scanner.nextLine().trim();

            PricingStrategy strategy = selectPricingStrategy(licenseChoice);
            double finalPrice = beatService.calculateProjectPrice(projectId, strategy);

            System.out.println("\n==================== KALKULATION FÜR '" + project.getTitle().toUpperCase() + "' ====================");
            System.out.println(" Gewählte Lizenz : " + strategy.getLicenseName());
            System.out.println(" Bedingungen     : " + strategy.getLicenseConditions());
            System.out.println(" Anzahl Spuren   : " + project.getTracks().size());
            System.out.printf(" BERECHNETER PREIS: CHF %.2f%n", finalPrice);
            System.out.println("======================================================================");

        } catch (SampleNotClearedException e) {
            System.out.println("\n[LIZENZ-WARNUNG / EXCEPTION] " + e.getMessage());
            System.out.println("-> Tipp: Wähle eine Lease-Lizenz (1 oder 2) oder kläre zuerst das Sample '" + e.getSampleName() + "'.");
        } catch (InvalidInputException e) {
            System.out.println("\n[FEHLER] " + e.getMessage());
        }
    }

    private void handleFilterByBpm() {
        System.out.println("\n--- BEATS NACH BPM FILTERN ---");
        try {
            int min = readIntInput("Mindest-BPM (z.B. 130): ");
            int max = readIntInput("Maximal-BPM (z.B. 150): ");

            List<BeatProject> matches = beatService.findProjectsByBpmRange(min, max);
            if (matches.isEmpty()) {
                System.out.println("Keine Beats im Bereich von " + min + " bis " + max + " BPM gefunden.");
            } else {
                System.out.println("\nGefundene Projekte (" + matches.size() + "):");
                for (BeatProject project : matches) {
                    printProjectDetails(project);
                }
            }
        } catch (InvalidInputException e) {
            System.out.println("\n[FEHLER] " + e.getMessage());
        }
    }

    private void handleDeleteProject() {
        System.out.print("\nID des zu löschenden Projekts eingeben: ");
        String id = scanner.nextLine().trim();
        if (beatService.deleteProject(id)) {
            System.out.println("[OK] Projekt " + id + " wurde gelöscht.");
        } else {
            System.out.println("[!] Kein Projekt mit der ID '" + id + "' gefunden.");
        }
    }

    private void handleSaveData() {
        try {
            beatService.saveProjects();
            System.out.println("\n[OK] Alle Projekte wurden erfolgreich in 'beats_data.csv' gespeichert.");
        } catch (IOException e) {
            System.out.println("\n[SPEICHER-FEHLER] " + e.getMessage());
        }
    }

    private void handleExit() {
        handleSaveData();
        System.out.println("Programm beendet. Bis zum nächsten Beat!");
    }

    /**
     * Returns the selected PricingStrategy using lazy initialization.
     */
    private PricingStrategy selectPricingStrategy(String choice) throws InvalidInputException {
        switch (choice) {
            case "1":
                if (basicStrategy == null) {
                    basicStrategy = new BasicLeaseStrategy();
                }
                return basicStrategy;
            case "2":
                if (wavStrategy == null) {
                    wavStrategy = new WavLeaseStrategy();
                }
                return wavStrategy;
            case "3":
                if (exclusiveStrategy == null) {
                    exclusiveStrategy = new ExclusiveStrategy();
                }
                return exclusiveStrategy;
            default:
                throw new InvalidInputException("Ungültiges Lizenzmodell gewählt: " + choice);
        }
    }

    private TrackType parseTrackTypeChoice(String choice) throws InvalidInputException {
        switch (choice) {
            case "1":
                return TrackType.SAMPLE;
            case "2":
                return TrackType.MIDI;
            case "3":
                return TrackType.DRUM;
            default:
                throw new InvalidInputException("Bitte 1 (Sample), 2 (MIDI) oder 3 (Drum) wählen.");
        }
    }

    private int readIntInput(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String raw = scanner.nextLine().trim();
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Ungültige Ganzzahl-Eingabe: '" + raw + "'.", e);
        }
    }

    private double readDoubleInput(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String raw = scanner.nextLine().trim();
        if (raw.isEmpty()) {
            return Track.DEFAULT_VOLUME_DB;
        }
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Ungültige Dezimalzahl-Eingabe: '" + raw + "'.", e);
        }
    }
}