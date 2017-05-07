package com.bobmowzie.mowziesmobs.server.biome;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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
//        Biome.SpawnListEntry foliaathSpawn = new Biome.SpawnListEntry(EntityFoliaath.class, MowziesMobs.CONFIG.spawnrateFoliaath, 3, 1);
//        Biome.SpawnListEntry tribeEliteSpawn = new Biome.SpawnListEntry(EntityBarakoana.class, MowziesMobs.CONFIG.spawnrateBarakoa, 0, 0);
//        for (Biome jungleBiome : BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE)) {
//            jungleBiome.getSpawnableList(EnumCreatureType.MONSTER).add(foliaathSpawn);
//        }
//        for (Biome savannaBiome : BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA)) {
//            savannaBiome.getSpawnableList(EnumCreatureType.MONSTER).add(tribeEliteSpawn);
//        }

        Multimap<BiomeDictionary.Type, Biome> biomesAndTypes = HashMultimap.create();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            for (BiomeDictionary.Type t : types)
            {
                biomesAndTypes.put(t, b);
                System.out.println(t.getName() + ", " + b.getBiomeName());
            }
        }

        EntityRegistry.addSpawn(EntityFoliaath.class, MowziesMobs.CONFIG.spawnrateFoliaath, 1, 3, EnumCreatureType.MONSTER, biomesAndTypes.get(BiomeDictionary.Type.LUSH).toArray(new Biome[biomesAndTypes.get(BiomeDictionary.Type.JUNGLE).size()]));
        EntityRegistry.addSpawn(EntityBarakoana.class, MowziesMobs.CONFIG.spawnrateBarakoa, 1, 1, EnumCreatureType.MONSTER, biomesAndTypes.get(BiomeDictionary.Type.SAVANNA).toArray(new Biome[biomesAndTypes.get(BiomeDictionary.Type.SAVANNA).size()]));
    }
}