package com.bobmowzie.mowziesmobs.server.world.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class SpawnHandler {

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
        if (ConfigHandler.MOBS.FOLIAATH.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.MOBS.FOLIAATH.spawnConfig.biomeConfig, biomeName)) {
//            System.out.println("Added foliaath biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.FOLIAATH, ConfigHandler.MOBS.FOLIAATH.spawnConfig, EntityClassification.MONSTER, event);
        }
        if (ConfigHandler.MOBS.BARAKOA.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.MOBS.BARAKOA.spawnConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Barakoa biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.BARAKOANA, ConfigHandler.MOBS.BARAKOA.spawnConfig, EntityClassification.MONSTER, event);
        }
        if (ConfigHandler.MOBS.GROTTOL.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.MOBS.GROTTOL.spawnConfig.biomeConfig, biomeName)) {
//            System.out.println("Added grottol biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.GROTTOL, ConfigHandler.MOBS.GROTTOL.spawnConfig, EntityClassification.MONSTER, event);
        }
        if (ConfigHandler.MOBS.LANTERN.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.MOBS.LANTERN.spawnConfig.biomeConfig, biomeName)) {
//            System.out.println("Added lantern biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.LANTERN, ConfigHandler.MOBS.LANTERN.spawnConfig, EntityClassification.AMBIENT, event);
        }
        if (ConfigHandler.MOBS.NAGA.spawnConfig.spawnRate.get() > 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.MOBS.NAGA.spawnConfig.biomeConfig, biomeName)) {
//            System.out.println("Added naga biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.NAGA, ConfigHandler.MOBS.NAGA.spawnConfig, EntityClassification.MONSTER, event);
        }
    }

    private static void registerEntityWorldSpawn(EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, EntityClassification classification, BiomeLoadingEvent event) {
        event.getSpawns().getSpawner(classification).add(new MobSpawnInfo.Spawners(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }
}