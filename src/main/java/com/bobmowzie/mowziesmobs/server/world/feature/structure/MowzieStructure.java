package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class MowzieStructure extends StructureFeature<NoneFeatureConfiguration> {
    private ConfigHandler.GenerationConfig config;

    public MowzieStructure(Codec<NoneFeatureConfiguration> codec, ConfigHandler.GenerationConfig config, PieceGenerator<NoneFeatureConfiguration> generator) {
        super(codec, PieceGeneratorSupplier.simple((c) -> MowzieStructure.checkLocation(c, config), generator), PostPlacementProcessor.NONE);
        this.config = config;
    }

    @Override
    public boolean canGenerate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, StructureManager structureManager, long seed, ChunkPos chunkPos, NoneFeatureConfiguration featureConfiguration, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> biomeCheck) {
        return super.canGenerate(registryAccess, chunkGenerator, biomeSource, structureManager, seed, chunkPos, featureConfiguration, heightAccessor, this::checkBiome);
    }

    protected boolean checkBiome(Holder<Biome> biome) {
        Optional<ResourceKey<Biome>> key = biome.unwrapKey();
        if (key.isEmpty()) return false;
        return BiomeChecker.isBiomeInConfig(config.biomeConfig, key.get());
    }

    protected static <C extends FeatureConfiguration> boolean checkLocation(PieceGeneratorSupplier.Context<C> context, ConfigHandler.GenerationConfig config) {
        if (config.generationDistance.get() < 0) {
            return false;
        }
        // TODO Dimension checking. Should it go here?

        ChunkPos chunkPos = context.chunkPos();
        BlockPos centerOfChunk = new BlockPos((chunkPos.x << 4) + 7, 0, (chunkPos.z << 4) + 7);

        if (!context.validBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG)) {
            return false;
        }

        double minHeight = config.heightMin.get();
        double maxHeight = config.heightMax.get();
        int landHeight = context.getLowestY(16, 16);
        if (minHeight != -1 && landHeight < minHeight) return false;
        if (maxHeight != -1 && landHeight > maxHeight) return false;

        ChunkGenerator chunkGenerator = context.chunkGenerator();
        LevelHeightAccessor heightLimitView = context.heightAccessor();
        int centerHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView);
        NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView);
        BlockState topBlock = columnOfBlocks.getBlock(centerHeight);
        if (!topBlock.getFluidState().isEmpty()) return false;

        List<? extends String> avoidStructures = config.avoidStructures.get();
        Registry<StructureSet> structureSetRegistry = context.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
        for (String structureName : avoidStructures) {
            Optional<StructureSet> structureSetOptional = structureSetRegistry.getOptional(new ResourceLocation(structureName));
            if (structureSetOptional.isEmpty()) continue;
            Optional<ResourceKey<StructureSet>> resourceKeyOptional = structureSetRegistry.getResourceKey(structureSetOptional.get());
            if (resourceKeyOptional.isEmpty()) continue;
            if (context.chunkGenerator().hasFeatureChunkInRange(resourceKeyOptional.get(), context.seed(), chunkPos.x, chunkPos.z, 10)) {
                return false;
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
