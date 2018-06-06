package com.bobmowzie.mowziesmobs.server.biome;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Set;

public enum BiomeDictionaryHandler {
    INSTANCE;

    public void onInit() {
        Biome.SpawnListEntry foliaathSpawn = new Biome.SpawnListEntry(EntityFoliaath.class, MowziesMobs.CONFIG.spawnrateFoliaath, 3, 1);
        Biome.SpawnListEntry tribeEliteSpawn = new Biome.SpawnListEntry(EntityBarakoana.class, MowziesMobs.CONFIG.spawnrateBarakoa, 1, 1);
        Biome.SpawnListEntry frostmawSpawn = new Biome.SpawnListEntry(EntityBarakoana.class, MowziesMobs.CONFIG.spawnrateFrostmaw, 1, 1);
        for (Biome jungleBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE)) {
            jungleBiome.getSpawnableList(EnumCreatureType.MONSTER).add(foliaathSpawn);
        }
        for (Biome savannaBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SAVANNA)) {
            savannaBiome.getSpawnableList(EnumCreatureType.MONSTER).add(tribeEliteSpawn);
        }
        for (Biome snowyBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SNOWY)) {
            if (!BiomeDictionary.isBiomeOfType(snowyBiome, BiomeDictionary.Type.BEACH) && !BiomeDictionary.isBiomeOfType(snowyBiome, BiomeDictionary.Type.OCEAN)) {
                snowyBiome.getSpawnableList(EnumCreatureType.MONSTER).add(frostmawSpawn);
            }
        }
    }
}