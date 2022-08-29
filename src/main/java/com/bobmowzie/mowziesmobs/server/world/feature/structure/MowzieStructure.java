package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class MowzieStructure extends StructureFeature<NoneFeatureConfiguration> {
    private final ConfigHandler.GenerationConfig config;

    public MowzieStructure(Codec<NoneFeatureConfiguration> codec, ConfigHandler.GenerationConfig config, TagKey<Biome> blockedBiomes, PieceGenerator<NoneFeatureConfiguration> generator, boolean doCheckHeight, boolean doAvoidWater, boolean doAvoidStructures) {
        super(codec, PieceGeneratorSupplier.simple((c) -> MowzieStructure.checkLocation(c, config, blockedBiomes, doCheckHeight, doAvoidWater, doAvoidStructures), generator), PostPlacementProcessor.NONE);
        this.config = config;
    }

    public MowzieStructure(Codec<NoneFeatureConfiguration> codec, ConfigHandler.GenerationConfig config, TagKey<Biome> blockedBiomes, PieceGenerator<NoneFeatureConfiguration> generator) {
        this(codec, config, blockedBiomes, generator, true, true, true);
    }

    protected static <C extends FeatureConfiguration> boolean checkLocation(PieceGeneratorSupplier.Context<C> context, ConfigHandler.GenerationConfig config, TagKey<Biome> blockedBiomes, boolean checkHeight, boolean avoidWater, boolean avoidStructures) {
        if (config.generationDistance.get() < 0) {
            return false;
        }

        ChunkPos chunkPos = context.chunkPos();
        BlockPos centerOfChunk = new BlockPos((chunkPos.x << 4) + 7, 0, (chunkPos.z << 4) + 7);

        if (!context.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG)) {
            return false;
        }

        int i = chunkPos.getMiddleBlockX();
        int j = chunkPos.getMiddleBlockZ();
        int k = context.chunkGenerator().getFirstOccupiedHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        Holder<Biome> biome = context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j));
        if (biome.is(blockedBiomes)) {
            return false;
        }

        if (checkHeight) {
            double minHeight = config.heightMin.get();
            double maxHeight = config.heightMax.get();
            int landHeight = context.getLowestY(16, 16);
            if (minHeight != -65 && landHeight < minHeight) return false;
            if (maxHeight != -65 && landHeight > maxHeight) return false;
        }

        if (avoidWater) {
            ChunkGenerator chunkGenerator = context.chunkGenerator();
            LevelHeightAccessor heightLimitView = context.heightAccessor();
            int centerHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView);
            NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView);
            BlockState topBlock = columnOfBlocks.getBlock(centerHeight);
            if (!topBlock.getFluidState().isEmpty()) return false;
        }

        if (avoidStructures) {
            List<? extends String> structuresToAvoid = config.avoidStructures.get();
            Registry<StructureSet> structureSetRegistry = context.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
            for (String structureName : structuresToAvoid) {
                Optional<StructureSet> structureSetOptional = structureSetRegistry.getOptional(new ResourceLocation(structureName));
                if (structureSetOptional.isEmpty()) continue;
                Optional<ResourceKey<StructureSet>> resourceKeyOptional = structureSetRegistry.getResourceKey(structureSetOptional.get());
                if (resourceKeyOptional.isEmpty()) continue;
                if (context.chunkGenerator().hasFeatureChunkInRange(resourceKeyOptional.get(), context.seed(), chunkPos.x, chunkPos.z, 5)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public ConfigHandler.GenerationConfig getConfig() {
        return config;
    }
}
