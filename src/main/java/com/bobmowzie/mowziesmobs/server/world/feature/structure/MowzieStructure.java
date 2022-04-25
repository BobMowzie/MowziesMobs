package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public abstract class MowzieStructure extends StructureFeature<NoneFeatureConfiguration> {
    public MowzieStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public abstract ConfigHandler.GenerationConfig getGenerationConfig();

    public boolean checkHeightLimitAgainstSurface() {
        return true;
    }

    public boolean avoidStructures() {
        return false;
    }

    public boolean avoidWater() {
        return true;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration featureConfig) {
        BlockPos centerOfChunk = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);
        if (checkHeightLimitAgainstSurface()) {
            int landHeight = chunkGenerator.getFirstFreeHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
            double minHeight = getGenerationConfig().heightMin.get();
            double maxHeight = getGenerationConfig().heightMax.get();
            if (minHeight != -1 && landHeight < minHeight) return false;
            if (maxHeight != -1 && landHeight > maxHeight) return false;
        }
        if (avoidWater()) {
            int landHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
            BlockGetter columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
            BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
            if (!topBlock.getFluidState().isEmpty()) return false;
        }
        List<? extends String> avoidStructures = getGenerationConfig().avoidStructures.get();
        for (String structureName : avoidStructures) {
            StructureFeature<?> structure = ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(structureName));
            if (structure == null) continue;
            if (structureNearby(structure, chunkGenerator, seed, chunkRandom, chunkX, chunkZ)) return false;
        }
        return super.isFeatureChunk(chunkGenerator, biomeSource, seed, chunkRandom, chunkX, chunkZ, biome, chunkPos, featureConfig);
    }

    private boolean structureNearby(StructureFeature structure, ChunkGenerator chunkGenerator, long seed, WorldgenRandom chunkRandom, int chunkX, int chunkY) {
        StructureFeatureConfiguration structureseparationsettings = chunkGenerator.getSettings().getConfig(structure);
        if (structureseparationsettings != null) {
            for (int i = chunkX - 10; i <= chunkX + 10; ++i) {
                for (int j = chunkY - 10; j <= chunkY + 10; ++j) {
                    ChunkPos chunkpos = structure.getPotentialFeatureChunk(structureseparationsettings, seed, chunkRandom, i, j);
                    if (i == chunkpos.x && j == chunkpos.z) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
