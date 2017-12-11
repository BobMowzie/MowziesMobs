package com.bobmowzie.mowziesmobs.server.config;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class ConfigHandler {
    @ConfigEntry(name = "Foliaath Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateFoliaath = 20;
    @ConfigEntry(name = "Foliaath Difficulty Multiplier", minValue = "0", maxValue = "20")
    public int difficultyScaleFoliaath = 1;
    @ConfigEntry(name = "Wroughtnaut Spawn Rate", minValue = "0", maxValue = "100", comment = "Wroughtnaut Spawnrate: Smaller number is more frequent, 0 to disable spawning")
    public int spawnrateWroughtnaut = 40;
    @ConfigEntry(name = "Wroughtnaut Difficulty Multiplier", minValue = "0", maxValue = "20")
    public int difficultyScaleWroughtnaut = 1;
    @ConfigEntry(name = "Barakoa Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateBarakoa = 4;
    @ConfigEntry(name = "Barakoa Difficulty Multiplier", minValue = "0", maxValue = "20")
    public int difficultyScaleBarakoa = 1;
    @ConfigEntry(name = "Barako Spawn Rate", minValue = "0", maxValue = "100", comment = "Barako Spawnrate: Smaller number is more frequent, 0 to disable spawning")
    public int spawnrateBarako = 15;
    @ConfigEntry(name = "Barako Difficulty Multiplier", minValue = "0", maxValue = "20")
    public int difficultyScaleBarako = 1;
    @ConfigEntry(name = "Frostmaw Spawn Rate", minValue = "0", maxValue = "100")
    public int spawnrateFrostmaw = 1;
    @ConfigEntry(name = "Frostmaw Difficulty Multiplier", minValue = "0", maxValue = "20")
    public int difficultyScaleFrostmaw = 1;
}
