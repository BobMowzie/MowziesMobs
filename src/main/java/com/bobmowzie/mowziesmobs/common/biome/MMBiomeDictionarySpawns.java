package com.bobmowzie.mowziesmobs.common.biome;

import com.bobmowzie.mowziesmobs.common.config.MMConfigHandler;
import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeElite;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class MMBiomeDictionarySpawns
{
    public static void init()
    {
        BiomeGenBase.SpawnListEntry foliaathSpawn = new BiomeGenBase.SpawnListEntry(EntityFoliaath.class, MMConfigHandler.spawnrateFoliaath, 3, 1);
        BiomeGenBase.SpawnListEntry tribeEliteSpawn = new BiomeGenBase.SpawnListEntry(EntityTribeElite.class, MMConfigHandler.spawnrateFoliaath, 1, 1);
        for (BiomeGenBase jungleBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE))
            jungleBiome.getSpawnableList(EnumCreatureType.monster).add(foliaathSpawn);
        for (BiomeGenBase savannaBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SAVANNA))
            savannaBiome.getSpawnableList(EnumCreatureType.monster).add(tribeEliteSpawn);
    }
}