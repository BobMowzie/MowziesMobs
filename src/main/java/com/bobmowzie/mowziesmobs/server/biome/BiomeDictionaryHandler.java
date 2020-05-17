package com.bobmowzie.mowziesmobs.server.biome;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoana;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Set;

public enum BiomeDictionaryHandler {
    INSTANCE;

    public static Set<Biome> FROSTMAW_BIOMES;
    public static Set<Biome> BARAKO_BIOMES;

    public void onInit() {
        Multimap<BiomeDictionary.Type, Biome> biomesAndTypes = HashMultimap.create();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            for (BiomeDictionary.Type t : types)
            {
                biomesAndTypes.put(t, b);
            }
        }

        FROSTMAW_BIOMES = new ObjectArraySet<>();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            if (types.contains(BiomeDictionary.Type.SNOWY) && !types.contains(BiomeDictionary.Type.BEACH) && !types.contains(BiomeDictionary.Type.OCEAN) && !types.contains(BiomeDictionary.Type.RIVER))
                FROSTMAW_BIOMES.add(b);
        }

        Set<Biome> overworldBiomes = new ObjectArraySet<>();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            if (types.contains(BiomeDictionary.Type.VOID) || types.contains(BiomeDictionary.Type.NETHER) || types.contains(BiomeDictionary.Type.END)) continue;
            overworldBiomes.add(b);
        }

        Set<Biome> lanternBiomes = new ObjectArraySet<>();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            if (types.contains(BiomeDictionary.Type.FOREST) && types.contains(BiomeDictionary.Type.MAGICAL))
                lanternBiomes.add(b);
        }
        lanternBiomes.add(Biomes.ROOFED_FOREST);
        lanternBiomes.add(Biomes.MUTATED_ROOFED_FOREST);

        Set<Biome> nagaBiomes = new ObjectArraySet<>();
        for (Biome b : Biome.REGISTRY)
        {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            if (types.contains(BiomeDictionary.Type.BEACH) && (types.contains(BiomeDictionary.Type.HILLS) || types.contains(BiomeDictionary.Type.MOUNTAIN)))
                nagaBiomes.add(b);
        }
        nagaBiomes.add(Biomes.STONE_BEACH);

        if (ConfigHandler.FOLIAATH.SPAWN_DATA.spawnRate > 0) {
            EntityRegistry.addSpawn(EntityFoliaath.class, ConfigHandler.FOLIAATH.SPAWN_DATA.spawnRate, ConfigHandler.FOLIAATH.SPAWN_DATA.minGroupSize, ConfigHandler.FOLIAATH.SPAWN_DATA.maxGroupSize, EnumCreatureType.MONSTER, biomesAndTypes.get(BiomeDictionary.Type.JUNGLE).toArray(new Biome[biomesAndTypes.get(BiomeDictionary.Type.JUNGLE).size()]));
        }
        if (ConfigHandler.BARAKOA.SPAWN_DATA.spawnRate > 0) {
            EntityRegistry.addSpawn(EntityBarakoana.class, ConfigHandler.BARAKOA.SPAWN_DATA.spawnRate, ConfigHandler.BARAKOA.SPAWN_DATA.minGroupSize, ConfigHandler.BARAKOA.SPAWN_DATA.maxGroupSize, EnumCreatureType.MONSTER, biomesAndTypes.get(BiomeDictionary.Type.SAVANNA).toArray(new Biome[biomesAndTypes.get(BiomeDictionary.Type.SAVANNA).size()]));
        }
        if (ConfigHandler.GROTTOL.SPAWN_DATA.spawnRate > 0) {
            EntityRegistry.addSpawn(EntityGrottol.class, ConfigHandler.GROTTOL.SPAWN_DATA.spawnRate, ConfigHandler.GROTTOL.SPAWN_DATA.minGroupSize, ConfigHandler.GROTTOL.SPAWN_DATA.maxGroupSize, EnumCreatureType.MONSTER, overworldBiomes.toArray(new Biome[overworldBiomes.size()]));
        }
        if (ConfigHandler.LANTERN.SPAWN_DATA.spawnRate > 0) {
            EntityRegistry.addSpawn(EntityLantern.class, ConfigHandler.LANTERN.SPAWN_DATA.spawnRate, ConfigHandler.LANTERN.SPAWN_DATA.minGroupSize, ConfigHandler.LANTERN.SPAWN_DATA.maxGroupSize, EnumCreatureType.AMBIENT, lanternBiomes.toArray(new Biome[lanternBiomes.size()]));
        }
        if (ConfigHandler.NAGA.SPAWN_DATA.spawnRate > 0) {
            EntityRegistry.addSpawn(EntityNaga.class, ConfigHandler.NAGA.SPAWN_DATA.spawnRate, ConfigHandler.NAGA.SPAWN_DATA.minGroupSize, ConfigHandler.NAGA.SPAWN_DATA.maxGroupSize, EnumCreatureType.MONSTER, nagaBiomes.toArray(new Biome[nagaBiomes.size()]));
        }
    }
}