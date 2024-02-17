package com.bobmowzie.mowziesmobs.server.world.spawn;

import java.util.HashMap;
import java.util.Map;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SpawnHandler {
    public static final Map<EntityType<?>, ConfigHandler.SpawnConfig> spawnConfigs = new HashMap<>();
    static {
        spawnConfigs.put(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig);
        spawnConfigs.put(EntityHandler.UMVUTHANA_RAPTOR.get(), ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig);
        spawnConfigs.put(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig);
        spawnConfigs.put(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig);
        spawnConfigs.put(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig);
    }

    public static void registerSpawnPlacementTypes() {
        SpawnPlacements.Type.create("MMSPAWN", new TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>>() {
            @Override
            public boolean test(LevelReader t, BlockPos pos, EntityType<? extends Mob> entityType) {
                BlockState block = t.getBlockState(pos.below());
                if (block.getBlock() == Blocks.BEDROCK || block.getBlock() == Blocks.BARRIER || !block.getMaterial().blocksMotion())
                    return false;
                BlockState iblockstateUp = t.getBlockState(pos);
                BlockState iblockstateUp2 = t.getBlockState(pos.above());
                return NaturalSpawner.isValidEmptySpawnBlock(t, pos, iblockstateUp, iblockstateUp.getFluidState(), entityType) && NaturalSpawner.isValidEmptySpawnBlock(t, pos.above(), iblockstateUp2, iblockstateUp2.getFluidState(), entityType);
            }
        });

        SpawnPlacements.Type mmSpawn = SpawnPlacements.Type.valueOf("MMSPAWN");
        if (mmSpawn != null) {
            SpawnPlacements.register(EntityHandler.FOLIAATH.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.LANTERN.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.UMVUTHANA_RAPTOR.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.NAGA.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.GROTTOL.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.UMVUTHANA_CRANE.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        }
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeName);
        if (ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added foliaath biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig, MobCategory.MONSTER, event);
        }
        if (ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added Barakoa biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.UMVUTHANA_RAPTOR.get(), ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig, MobCategory.MONSTER, event);
        }
        if (ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added grottol biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig, MobCategory.MONSTER, event);
        }
        if (ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added lantern biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, MobCategory.AMBIENT, event);
        }
        if (ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added naga biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, MobCategory.MONSTER, event);
        }
    }

    private static void registerEntityWorldSpawn(EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, MobCategory classification, BiomeLoadingEvent event) {
        event.getSpawns().getSpawner(classification).add(new MobSpawnSettings.SpawnerData(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }
}