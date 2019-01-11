package com.bobmowzie.mowziesmobs.server.config;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class ConfigHandler {
    @ConfigEntry(name = "Foliaath Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateFoliaath = 20;
    @ConfigEntry(name = "Foliaath Health Multiplier", minValue = "0", maxValue = "20")
    public float healthScaleFoliaath = 1;
    @ConfigEntry(name = "Foliaath Attack Multiplier", minValue = "0", maxValue = "20")
    public float attackScaleFoliaath = 1;

    @ConfigEntry(name = "Wroughtnaut Spawn Rate", minValue = "0", maxValue = "100", comment = "Wroughtnaut Spawnrate: Smaller number causes more spawning, 0 to disable spawning")
    public int spawnrateWroughtnaut = 40;
    @ConfigEntry(name = "Wroughtnaut Health Multiplier", minValue = "0", maxValue = "20")
    public float healthScaleWroughtnaut = 1;
    @ConfigEntry(name = "Wroughtnaut Attack Multiplier", minValue = "0", maxValue = "20")
    public float attackScaleWroughtnaut = 1;

    @ConfigEntry(name = "Barakoa Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateBarakoa = 4;
    @ConfigEntry(name = "Barakoa Health Multiplier", minValue = "0", maxValue = "20")
    public float healthScaleBarakoa = 1;
    @ConfigEntry(name = "Barakoa Attack Multiplier", minValue = "0", maxValue = "20")
    public float attackScaleBarakoa = 1;

    @ConfigEntry(name = "Barako Spawn Rate", minValue = "0", maxValue = "100", comment = "Barako Spawnrate: Smaller number causes more spawning, 0 to disable spawning")
    public int spawnrateBarako = 15;
    @ConfigEntry(name = "Barako Health Multiplier", minValue = "0", maxValue = "20")
    public float healthScaleBarako = 1;
    @ConfigEntry(name = "Barako Attack Multiplier", minValue = "0", maxValue = "20")
    public float attackScaleBarako = 1;

    @ConfigEntry(name = "Frostmaw Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateFrostmaw = 1;
    @ConfigEntry(name = "Frostmaw Health Multiplier", minValue = "0", maxValue = "20")
    public float healthScaleFrostmaw = 1;
    @ConfigEntry(name = "Frostmaw Attack Multiplier", minValue = "0", maxValue = "20")
    public float attackScaleFrostmaw = 1;

    @ConfigEntry(name = "Grottol Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateGrottol = 2;

    @ConfigEntry(name = "Lantern Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateLantern = 2;

    @ConfigEntry(name = "Naga Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateNaga = 5;
    @ConfigEntry(name = "Naga Health Multiplier", minValue = "0", maxValue = "20")
    public float healthScaleNaga = 1;
    @ConfigEntry(name = "Naga Attack Multiplier", minValue = "0", maxValue = "20")
    public float attackScaleNaga = 1;
}
