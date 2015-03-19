package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class MMBiomeDictionarySpawns
{
    static BiomeGenBase[] jungleBiomes = BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE);
    static BiomeGenBase.SpawnListEntry foliaathSpawn = new BiomeGenBase.SpawnListEntry(EntityFoliaath.class, 20, 3, 1);

    public static void init()
    {
        System.out.println("Number of jungle biomes is" + jungleBiomes.length);
        for(int i = 0; i < jungleBiomes.length; i++)
        {
            jungleBiomes[i].getSpawnableList(EnumCreatureType.monster).add(foliaathSpawn);
        }
    }
}
