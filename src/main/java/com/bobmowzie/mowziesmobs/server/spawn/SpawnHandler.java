package com.bobmowzie.mowziesmobs.server.spawn;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.*;

public class SpawnHandler {
    public static Set<Biome> FOLIAATH_BIOMES = new HashSet<>();
    public static Set<Biome> FERROUS_WROUGHTNAUT_BIOMES = new HashSet<>();
    public static Set<Biome> BARAKOA_BIOMES = new HashSet<>();
    public static Set<Biome> BARAKO_BIOMES = new HashSet<>();
    public static Set<Biome> FROSTMAW_BIOMES = new HashSet<>();
    public static Set<Biome> GROTTOL_BIOMES = new HashSet<>();
    public static Set<Biome> LANTERN_BIOMES = new HashSet<>();
    public static Set<Biome> NAGA_BIOMES = new HashSet<>();

    public static Set<String> WHITELISTED_BIOMES = new HashSet<>();
    public static Set<String> BLACKLISTED_BIOMES = new HashSet<>();

    public static void registerSpawnPlacementTypes() {
        EntitySpawnPlacementRegistry.PlacementType.create("MMSPAWN", new TriPredicate<IWorldReader, BlockPos, EntityType<? extends MobEntity>>() {
            @Override
            public boolean test(IWorldReader t, BlockPos pos, EntityType<? extends MobEntity> entityType) {
                BlockState block = t.getBlockState(pos.down());
                if (block.getBlock() == Blocks.BEDROCK || block.getBlock() == Blocks.BARRIER || !block.getMaterial().blocksMovement())
                    return false;
                BlockState iblockstateUp = t.getBlockState(pos);
                BlockState iblockstateUp2 = t.getBlockState(pos.up());
                return WorldEntitySpawner.func_234968_a_(t, pos, iblockstateUp, iblockstateUp.getFluidState(), entityType) && WorldEntitySpawner.func_234968_a_(t, pos.up(), iblockstateUp2, iblockstateUp2.getFluidState(), entityType);
            }
        });

        EntitySpawnPlacementRegistry.PlacementType mmSpawn = EntitySpawnPlacementRegistry.PlacementType.valueOf("MMSPAWN");
        if (mmSpawn != null) {
            EntitySpawnPlacementRegistry.register(EntityHandler.FOLIAATH, mmSpawn, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            EntitySpawnPlacementRegistry.register(EntityHandler.LANTERN, mmSpawn, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            EntitySpawnPlacementRegistry.register(EntityHandler.BARAKOANA, mmSpawn, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
            EntitySpawnPlacementRegistry.register(EntityHandler.NAGA, mmSpawn, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        }
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        Biome biome = ForgeRegistries.BIOMES.getValue(biomeName);
        if (ConfigHandler.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && isBiomeInConfig(ConfigHandler.MOBS.FROSTMAW.generationConfig.biomeConfig, biomeName)) {
            FROSTMAW_BIOMES.add(biome);
//            System.out.println("Added frostmaw biome: " + biomeName.toString());
        }
        if (ConfigHandler.MOBS.BARAKO.generationConfig.generationDistance.get() >= 0 && isBiomeInConfig(ConfigHandler.MOBS.BARAKO.generationConfig.biomeConfig, biomeName)) {
            BARAKO_BIOMES.add(biome);
//            System.out.println("Added Barako biome: " + biomeName.toString());
        }
        if (ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && isBiomeInConfig(ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig, biomeName)) {
            FERROUS_WROUGHTNAUT_BIOMES.add(biome);
//            System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
        }

        if (ConfigHandler.MOBS.FOLIAATH.spawnConfig.spawnRate.get() > 0 && isBiomeInConfig(ConfigHandler.MOBS.FOLIAATH.spawnConfig.biomeConfig, biomeName)) {
            FOLIAATH_BIOMES.add(biome);
//            System.out.println("Added foliaath biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.FOLIAATH, ConfigHandler.MOBS.FOLIAATH.spawnConfig, EntityClassification.MONSTER, event);
        }
        if (ConfigHandler.MOBS.BARAKOA.spawnConfig.spawnRate.get() > 0 && isBiomeInConfig(ConfigHandler.MOBS.BARAKOA.spawnConfig.biomeConfig, biomeName)) {
            BARAKOA_BIOMES.add(biome);
//            System.out.println("Added Barakoa biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.BARAKOANA, ConfigHandler.MOBS.BARAKOA.spawnConfig, EntityClassification.MONSTER, event);
        }
        if (ConfigHandler.MOBS.GROTTOL.spawnConfig.spawnRate.get() > 0 && isBiomeInConfig(ConfigHandler.MOBS.GROTTOL.spawnConfig.biomeConfig, biomeName)) {
            GROTTOL_BIOMES.add(biome);
//            System.out.println("Added grottol biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.GROTTOL, ConfigHandler.MOBS.GROTTOL.spawnConfig, EntityClassification.MONSTER, event);
        }
        if (ConfigHandler.MOBS.LANTERN.spawnConfig.spawnRate.get() > 0 && isBiomeInConfig(ConfigHandler.MOBS.LANTERN.spawnConfig.biomeConfig, biomeName)) {
            LANTERN_BIOMES.add(biome);
//            System.out.println("Added lantern biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.LANTERN, ConfigHandler.MOBS.LANTERN.spawnConfig, EntityClassification.AMBIENT, event);
        }
        if (ConfigHandler.MOBS.NAGA.spawnConfig.spawnRate.get() > 0 && isBiomeInConfig(ConfigHandler.MOBS.NAGA.spawnConfig.biomeConfig, biomeName)) {
            NAGA_BIOMES.add(biome);
//            System.out.println("Added naga biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.NAGA, ConfigHandler.MOBS.NAGA.spawnConfig, EntityClassification.MONSTER, event);
        }
    }

    private static void registerEntityWorldSpawn(EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, EntityClassification classification, BiomeLoadingEvent event) {
        event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }

    public static void postLoad() {
        Set<ResourceLocation> biomeKeys = ForgeRegistries.BIOMES.getKeys();
        for (String s : WHITELISTED_BIOMES) {
            boolean keyExists = false;
            for (ResourceLocation r : biomeKeys) {
                if (r.toString().equals(s)) {
                    keyExists = true;
                    break;
                }
            }
            if (!keyExists) System.out.println("Unable to whitelist biome '" + s + "'. No biome with that name exists.");
        }
        for (String s : BLACKLISTED_BIOMES) {
            boolean keyExists = false;
            for (ResourceLocation r : biomeKeys) {
                if (r.toString().equals(s)) {
                    keyExists = true;
                    break;
                }
            }
            if (!keyExists) System.out.println("Unable to blacklist biome '" + s + "'. No biome with that name exists.");
        }
    }

    private static class BiomeCombo {
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
                Collection<Type> allTypes = Type.getAll();
                Type neededType = Type.getType(name);
                if (!allTypes.contains(neededType)) System.out.println("Mowzie's Mobs config warning: No biome dictionary type with name '" + name + "'. Unable to check for type.");
                neededTypes[i] = neededType;
            }
        }

        private boolean acceptsBiome(RegistryKey<Biome> biome) {
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

    private static boolean isBiomeInConfig(ConfigHandler.BiomeConfig biomeConfig, ResourceLocation biomeName) {
        WHITELISTED_BIOMES.addAll(biomeConfig.biomeWhitelist.get());
        BLACKLISTED_BIOMES.addAll(biomeConfig.biomeBlacklist.get());
        if (biomeConfig.biomeWhitelist.get().contains(biomeName.toString())) {
            return true;
        }
        if (biomeConfig.biomeBlacklist.get().contains(biomeName.toString())) {
            return false;
        }
        Set<BiomeCombo> biomeCombos = new HashSet<>();
        for (String biomeComboString : biomeConfig.biomeTypes.get()) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            biomeCombos.add(biomeCombo);
        }
        RegistryKey<Biome> biomeRegistryKey = RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, biomeName);
        for (BiomeCombo biomeCombo : biomeCombos) {
            if (biomeCombo.acceptsBiome(biomeRegistryKey)) return true;
        }
        return false;
    }
}