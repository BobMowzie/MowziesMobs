package com.bobmowzie.mowziesmobs.server.world.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpawnHandler {
    public static final Map<EntityType<?>, ConfigHandler.SpawnConfig> spawnConfigs = new HashMap<>();
    static {
        spawnConfigs.put(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig);
        spawnConfigs.put(EntityHandler.BARAKOANA.get(), ConfigHandler.COMMON.MOBS.BARAKOA.spawnConfig);
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
            SpawnPlacements.register(EntityHandler.BARAKOANA.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.NAGA.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.GROTTOL.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
            SpawnPlacements.register(EntityHandler.BARAKOAYA.get(), mmSpawn, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        }
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeName);
        Optional<Holder<Biome>> biomeOp = ForgeRegistries.BIOMES.getHolder(biomeName);
        if (biomeOp.isPresent()) {
            Holder<Biome> biome = biomeOp.get();
            if (ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.spawnRate.get() > 0 && checkBiomeTags(biome, TagHandler.HAS_FOLIAATH, TagHandler.NO_FOLIAATH)) {
//            System.out.println("Added foliaath biome: " + biomeName.toString());
                registerEntityWorldSpawn(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig, MobCategory.MONSTER, event);
            }
            if (ConfigHandler.COMMON.MOBS.BARAKOA.spawnConfig.spawnRate.get() > 0 && checkBiomeTags(biome, TagHandler.HAS_BARAKOANA, TagHandler.NO_BARAKOANA)) {
//            System.out.println("Added Barakoa biome: " + biomeName.toString());
                registerEntityWorldSpawn(EntityHandler.BARAKOANA.get(), ConfigHandler.COMMON.MOBS.BARAKOA.spawnConfig, MobCategory.MONSTER, event);
            }
            if (ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.spawnRate.get() > 0 && checkBiomeTags(biome, TagHandler.HAS_GROTTOL, TagHandler.NO_GROTTOL)) {
//            System.out.println("Added grottol biome: " + biomeName.toString());
                registerEntityWorldSpawn(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig, MobCategory.MONSTER, event);
            }
            if (ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.spawnRate.get() > 0 && checkBiomeTags(biome, TagHandler.HAS_LANTERN, TagHandler.NO_LANTERN)) {
//            System.out.println("Added lantern biome: " + biomeName.toString());
                registerEntityWorldSpawn(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, MobCategory.AMBIENT, event);
            }
            if (ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.spawnRate.get() > 0 && checkBiomeTags(biome, TagHandler.HAS_NAGA, TagHandler.NO_NAGA)) {
//            System.out.println("Added naga biome: " + biomeName.toString());
                registerEntityWorldSpawn(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, MobCategory.MONSTER, event);
            }
        }
    }

    private static boolean checkBiomeTags(Holder<Biome> biome, TagKey<Biome> hasTag, TagKey<Biome> noTag) {
        return biome.is(hasTag) && !biome.is(noTag);
    }

    private static void registerEntityWorldSpawn(EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, MobCategory classification, BiomeLoadingEvent event) {
        event.getSpawns().getSpawner(classification).add(new MobSpawnSettings.SpawnerData(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }
}