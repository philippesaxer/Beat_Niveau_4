package ch.bms.beatmanager.model;

/**
 * Represents a virtual instrument (VST/MIDI) track.
 */
public class MidiTrack extends Track {

    private static final double VST_SURCHARGE = 10.0;

    private String vstPluginName;
    private String presetName;

    public MidiTrack(String name, double volumeDb, String vstPluginName, String presetName) {
        super(name, volumeDb);
        this.vstPluginName = vstPluginName;
        this.presetName = presetName;
    }

    public MidiTrack(String name, String vstPluginName, String presetName) {
        super(name);
        this.vstPluginName = vstPluginName;
        this.presetName = presetName;
    }

    @Override
    public String getTrackSummary() {
        return String.format("[MIDI]   %-18s | Vol: %5.1f dB | VST: %-18s | Preset: %s",
                getName(), getVolumeDb(), vstPluginName, presetName);
    }

    @Override
    public double getLicensingSurcharge() {
        return VST_SURCHARGE;
    }

    @Override
    public String toCsvString() {
        return String.join(";", "MIDI", getName(), String.valueOf(getVolumeDb()), vstPluginName, presetName);
    }

    public String getVstPluginName() {
        return vstPluginName;
    }

    public String getPresetName() {
        return presetName;
    }
}