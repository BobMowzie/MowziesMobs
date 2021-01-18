package com.bobmowzie.mowziesmobs.server.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.*;

public enum SpawnHandler {
    INSTANCE;

    public static Set<Biome> FROSTMAW_BIOMES;
    public static Set<Biome> BARAKO_BIOMES;
    public static Set<Biome> FERROUS_WROUGHTNAUT_BIOMES;

    private static final Map<String, Type> BY_NAME = new HashMap<String, Type>();

    public void registerSpawnPlacementTypes() {
        EntitySpawnPlacementRegistry.PlacementType.create("MMSPAWN", new TriPredicate<IWorldReader, BlockPos, EntityType<? extends MobEntity>>() {
            @Override
            public boolean test(IWorldReader t, BlockPos pos, EntityType<? extends MobEntity> entityType) {
                BlockState block = t.getBlockState(pos.down());
                boolean flag = block.getBlock() != Blocks.BEDROCK && block.getBlock() != Blocks.BARRIER && block.getMaterial().blocksMovement();
                BlockState iblockstateUp = t.getBlockState(pos);
                BlockState iblockstateUp2 = t.getBlockState(pos.up());
                flag = flag && WorldEntitySpawner.isSpawnableSpace(t, pos, iblockstateUp, iblockstateUp.getFluidState()) && WorldEntitySpawner.isSpawnableSpace(t, pos.up(), iblockstateUp2, iblockstateUp2.getFluidState());
                return flag;
            }
        });
    }

    public void registerSpawns() {
        EntitySpawnPlacementRegistry.PlacementType mmSpawn = EntitySpawnPlacementRegistry.PlacementType.valueOf("MMSPAWN");
        if (mmSpawn != null) {
            EntitySpawnPlacementRegistry.register(EntityHandler.FOLIAATH, mmSpawn, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            EntitySpawnPlacementRegistry.register(EntityHandler.LANTERN, mmSpawn, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            EntitySpawnPlacementRegistry.register(EntityHandler.BARAKOANA, mmSpawn, Heightmap.Type.WORLD_SURFACE, MowzieEntity::spawnPredicate);
            EntitySpawnPlacementRegistry.register(EntityHandler.NAGA, mmSpawn, Heightmap.Type.WORLD_SURFACE, MowzieEntity::spawnPredicate);
        }

        for (Type type : BiomeDictionary.Type.getAll()) {
            BY_NAME.put(type.getName(), type);
        }

        if (ConfigHandler.MOBS.FROSTMAW.generationConfig.generationFrequency.get() > 0) {
            FROSTMAW_BIOMES = getBiomesFromConfig(ConfigHandler.MOBS.FROSTMAW.generationConfig.biomeConfig);
            //System.out.println("Frostmaw biomes " + FROSTMAW_BIOMES);
        }
        if (ConfigHandler.MOBS.BARAKO.generationConfig.generationFrequency.get() > 0) {
            BARAKO_BIOMES = getBiomesFromConfig(ConfigHandler.MOBS.BARAKO.generationConfig.biomeConfig);
            //System.out.println("Barako biomes " + BARAKO_BIOMES);
        }
        if (ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationFrequency.get() > 0) {
            FERROUS_WROUGHTNAUT_BIOMES = getBiomesFromConfig(ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig);
            //System.out.println("Ferrous Wroughtnaut biomes " + FERROUS_WROUGHTNAUT_BIOMES);
        }

        if (ConfigHandler.MOBS.FOLIAATH.spawnConfig.spawnRate.get() > 0) {
            Set<Biome> foliaathBiomes = getBiomesFromConfig(ConfigHandler.MOBS.FOLIAATH.spawnConfig.biomeConfig);
//            System.out.println("Foliaath biomes " + foliaathBiomes);
            registerEntityWorldSpawn(EntityHandler.FOLIAATH, ConfigHandler.MOBS.FOLIAATH.spawnConfig.spawnRate.get(), ConfigHandler.MOBS.FOLIAATH.spawnConfig.minGroupSize.get(), ConfigHandler.MOBS.FOLIAATH.spawnConfig.maxGroupSize.get(), EntityClassification.MONSTER, foliaathBiomes.toArray(new Biome[foliaathBiomes.size()]));
        }
        if (ConfigHandler.MOBS.BARAKOA.spawnConfig.spawnRate.get() > 0) {
            Set<Biome> barakoaBiomes = getBiomesFromConfig(ConfigHandler.MOBS.BARAKOA.spawnConfig.biomeConfig);
            registerEntityWorldSpawn(EntityHandler.BARAKOANA, ConfigHandler.MOBS.BARAKOA.spawnConfig.spawnRate.get(), ConfigHandler.MOBS.BARAKOA.spawnConfig.minGroupSize.get(), ConfigHandler.MOBS.BARAKOA.spawnConfig.maxGroupSize.get(), EntityClassification.MONSTER, barakoaBiomes.toArray(new Biome[barakoaBiomes.size()]));
        }
        if (ConfigHandler.MOBS.GROTTOL.spawnConfig.spawnRate.get() > 0) {
            Set<Biome> grottolBiomes = getBiomesFromConfig(ConfigHandler.MOBS.GROTTOL.spawnConfig.biomeConfig);
            registerEntityWorldSpawn(EntityHandler.GROTTOL, ConfigHandler.MOBS.GROTTOL.spawnConfig.spawnRate.get(), ConfigHandler.MOBS.GROTTOL.spawnConfig.minGroupSize.get(), ConfigHandler.MOBS.GROTTOL.spawnConfig.maxGroupSize.get(), EntityClassification.MONSTER, grottolBiomes.toArray(new Biome[grottolBiomes.size()]));
        }
        if (ConfigHandler.MOBS.LANTERN.spawnConfig.spawnRate.get() > 0) {
            Set<Biome> lanternBiomes = getBiomesFromConfig(ConfigHandler.MOBS.LANTERN.spawnConfig.biomeConfig);
            registerEntityWorldSpawn(EntityHandler.LANTERN, ConfigHandler.MOBS.LANTERN.spawnConfig.spawnRate.get(), ConfigHandler.MOBS.LANTERN.spawnConfig.minGroupSize.get(), ConfigHandler.MOBS.LANTERN.spawnConfig.maxGroupSize.get(), EntityClassification.AMBIENT, lanternBiomes.toArray(new Biome[lanternBiomes.size()]));
        }
        if (ConfigHandler.MOBS.NAGA.spawnConfig.spawnRate.get() > 0) {
            Set<Biome> nagaBiomes = getBiomesFromConfig(ConfigHandler.MOBS.NAGA.spawnConfig.biomeConfig);
//            System.out.println("Naga biomes " + nagaBiomes);
            registerEntityWorldSpawn(EntityHandler.NAGA, ConfigHandler.MOBS.NAGA.spawnConfig.spawnRate.get(), ConfigHandler.MOBS.NAGA.spawnConfig.minGroupSize.get(), ConfigHandler.MOBS.NAGA.spawnConfig.maxGroupSize.get(), EntityClassification.MONSTER, nagaBiomes.toArray(new Biome[nagaBiomes.size()]));
        }
    }

    private void registerEntityWorldSpawn(EntityType<?> entity, int weight, int min, int max, EntityClassification classification, Biome... biomes) {
        for (Biome biome : biomes) {
            if (biome != null) {
                biome.getSpawns(classification).add(new Biome.SpawnListEntry(entity, weight, min, max));
            }
        }
    }

    private class BiomeCombo {
        Type[] neededTypes;
        boolean[] inverted;
        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.toUpperCase().replace(" ", "").split(",");
            neededTypes = new Type[typeStrings.length];
            inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].length() == 0) {
                    continue;
                }
                inverted[i] = typeStrings[i].charAt(0) == '!';
                String name = typeStrings[i].replace("!", "");
                Type neededType = BY_NAME.get(name);
                if (neededType == null) System.out.println("Mowzie's Mobs config warning: No biome dictionary type with name '" + name + "'. Unable to check for type.");
                neededTypes[i] = neededType;
            }
        }

        private boolean acceptsBiome(Biome biome) {
            Set<Type> thisTypes = BiomeDictionary.getTypes(biome);
            for (int i = 0; i < neededTypes.length; i++) {
                if (neededTypes[i] == null) continue;
                if (!inverted[i]) {
                    if (!thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
                else {
                    if (thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private Set<Biome> getBiomesFromConfig(ConfigHandler.BiomeConfig biomeConfig) {
        Set<String> biomeWhitelistNames = new HashSet<>(biomeConfig.biomeWhitelist.get());
        Set<String> biomeBlacklistNames = new HashSet<>(biomeConfig.biomeBlacklist.get());
        Set<String> whiteOrBlacklistedBiomes = new HashSet<>();
        Set<BiomeCombo> biomeCombos = new HashSet<>();
        for (String biomeComboString : biomeConfig.biomeTypes.get()) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            biomeCombos.add(biomeCombo);
        }

        Set<Biome> toReturn = new HashSet<>();
        for (Biome b : ForgeRegistries.BIOMES) {
            ResourceLocation biomeRegistryName = b.getRegistryName();
            if (biomeRegistryName != null) {
                String biomeName = biomeRegistryName.getPath();
                if (biomeWhitelistNames.contains(biomeName)) {
                    toReturn.add(b);
                    whiteOrBlacklistedBiomes.add(biomeName);
                    continue;
                }
                if (biomeBlacklistNames.contains(biomeName)) {
                    whiteOrBlacklistedBiomes.add(biomeName);
                    continue;
                }
                for (BiomeCombo biomeCombo : biomeCombos) {
                    if (biomeCombo.acceptsBiome(b)) toReturn.add(b);
                }
            }
        }

        for (String biomeName : biomeWhitelistNames) {
            if (!whiteOrBlacklistedBiomes.contains(biomeName)) {
                System.out.println("Mowzie's Mobs config warning: No biome with name '" + biomeName + "'. Unable to whitelist.");
            }
        }
        for (String biomeName : biomeBlacklistNames) {
            if (!whiteOrBlacklistedBiomes.contains(biomeName)) {
                System.out.println("Mowzie's Mobs config warning: No biome with name '" + biomeName + "'. Unable to blacklist.");
            }
        }

        return toReturn;
    }
}