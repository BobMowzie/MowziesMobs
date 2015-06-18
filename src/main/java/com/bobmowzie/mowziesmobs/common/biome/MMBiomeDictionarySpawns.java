package com.bobmowzie.mowziesmobs.common.biome;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class MMBiomeDictionarySpawns
{
    public static void init()
    {
        BiomeGenBase.SpawnListEntry foliaathSpawn = new BiomeGenBase.SpawnListEntry(EntityFoliaath.class, 20, 3, 1);
        for (BiomeGenBase jungleBiome : BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE))
            jungleBiome.getSpawnableList(EnumCreatureType.monster).add(foliaathSpawn);
    }
}