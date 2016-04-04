package com.bobmowzie.mowziesmobs.server.config;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class ConfigHandler {
    @ConfigEntry(name = "Foliaath Spawnrate", minValue = "0", maxValue = "100")
    public int spawnrateFoliaath = 20;
    @ConfigEntry(name = "Wroughtnaut Spawnrate", minValue = "0", maxValue = "100", comment = "Smaller number is more Wroughtnauts, 0 to disable spawning")
    public int spawnrateWroughtnaut = 50;
    @ConfigEntry(name = "Barakoa Spawnrate", minValue = "3", maxValue = "100")
    public int spawnrateBarakoa = 3;
}
