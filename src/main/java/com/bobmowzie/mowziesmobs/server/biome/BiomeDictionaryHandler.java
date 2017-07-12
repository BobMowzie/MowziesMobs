package com.bobmowzie.mowziesmobs.server.biome;

import com.bobmowzie.mowziesmobs.server.entity.EntityFrostmaw;
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

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

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
            }
        }

        Set<Biome> frostmawBiomes = new ObjectArraySet<>();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            if (types.contains(BiomeDictionary.Type.SNOWY) && !types.contains(BiomeDictionary.Type.BEACH) && !types.contains(BiomeDictionary.Type.OCEAN) && !types.contains(BiomeDictionary.Type.RIVER))
                frostmawBiomes.add(b);
        }

        EntityRegistry.addSpawn(EntityFoliaath.class, MowziesMobs.CONFIG.spawnrateFoliaath, 0, 3, EnumCreatureType.MONSTER, biomesAndTypes.get(BiomeDictionary.Type.JUNGLE).toArray(new Biome[biomesAndTypes.get(BiomeDictionary.Type.JUNGLE).size()]));
        EntityRegistry.addSpawn(EntityBarakoana.class, MowziesMobs.CONFIG.spawnrateBarakoa, 1, 1, EnumCreatureType.MONSTER, biomesAndTypes.get(BiomeDictionary.Type.SAVANNA).toArray(new Biome[biomesAndTypes.get(BiomeDictionary.Type.SAVANNA).size()]));
        EntityRegistry.addSpawn(EntityFrostmaw.class, MowziesMobs.CONFIG.spawnrateFrostmaw, 1, 1, EnumCreatureType.MONSTER, frostmawBiomes.toArray(new Biome[frostmawBiomes.size()]));
    }
}