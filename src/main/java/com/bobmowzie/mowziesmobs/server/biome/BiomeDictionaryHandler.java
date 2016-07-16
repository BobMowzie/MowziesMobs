package com.bobmowzie.mowziesmobs.server.biome;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeElite;

public enum BiomeDictionaryHandler {
    INSTANCE;

    public void onInit() {
        Biome.SpawnListEntry foliaathSpawn = new Biome.SpawnListEntry(EntityFoliaath.class, MowziesMobs.CONFIG.spawnrateFoliaath, 3, 1);
        Biome.SpawnListEntry tribeEliteSpawn = new Biome.SpawnListEntry(EntityTribeElite.class, MowziesMobs.CONFIG.spawnrateBarakoa, 0, 0);
        for (Biome jungleBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE)) {
            jungleBiome.getSpawnableList(EnumCreatureType.MONSTER).add(foliaathSpawn);
        }
        for (Biome savannaBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SAVANNA)) {
            savannaBiome.getSpawnableList(EnumCreatureType.MONSTER).add(tribeEliteSpawn);
        }
    }
}