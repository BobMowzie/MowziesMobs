package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class MowzieStructure extends Structure {
    private final ConfigHandler.GenerationConfig config;
    private Set<ResourceLocation> allowedBiomes;
    private boolean doCheckHeight;
    private boolean doAvoidWater;
    private boolean doAvoidStructures;

    public MowzieStructure(Structure.StructureSettings settings, ConfigHandler.GenerationConfig config, Set<ResourceLocation> allowedBiomes, boolean doCheckHeight, boolean doAvoidWater, boolean doAvoidStructures) {
        super(settings);
        this.config = config;
        this.allowedBiomes = allowedBiomes;
        this.doCheckHeight = doCheckHeight;
        this.doAvoidWater = doAvoidWater;
        this.doAvoidStructures = doAvoidStructures;
    }

    public MowzieStructure(Structure.StructureSettings settings, ConfigHandler.GenerationConfig config, Set<ResourceLocation> allowedBiomes) {
        this(settings, config, allowedBiomes, true, true, true);
    }

    public MowzieStructure(Structure.StructureSettings settings, ConfigHandler.GenerationConfig config) {
        super(settings);
        this.config = config;
    }
    
    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    	return checkLocation(context, this.config, this.allowedBiomes, this.doCheckHeight, this.doAvoidWater, this.doAvoidStructures);
    }

    protected Optional<Structure.GenerationStub> checkLocation(GenerationContext context, ConfigHandler.GenerationConfig config, Set<ResourceLocation> allowedBiomes, boolean checkHeight, boolean avoidWater, boolean avoidStructures) {
        if (config.generationDistance.get() < 0) {
            return Optional.empty();
        }

        ChunkPos chunkPos = context.chunkPos();
        BlockPos centerOfChunk = new BlockPos((chunkPos.x << 4) + 7, 0, (chunkPos.z << 4) + 7);

        int i = chunkPos.getMiddleBlockX();
        int j = chunkPos.getMiddleBlockZ();
        int k = context.chunkGenerator().getFirstOccupiedHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        Holder<Biome> biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j), context.randomState().sampler());
        if (!allowedBiomes.contains(ForgeRegistries.BIOMES.getKey(biome.value()))) {
            return Optional.empty();
        }

        if (checkHeight) {
            double minHeight = config.heightMin.get();
            double maxHeight = config.heightMax.get();
            int landHeight = getLowestY(context, 16, 16);
            if (minHeight != -65 && landHeight < minHeight) return Optional.empty();
            if (maxHeight != -65 && landHeight > maxHeight) return Optional.empty();
        }

        if (avoidWater) {
            ChunkGenerator chunkGenerator = context.chunkGenerator();
            LevelHeightAccessor heightLimitView = context.heightAccessor();
            int centerHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, context.randomState());
            NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView, context.randomState());
            BlockState topBlock = columnOfBlocks.getBlock(centerHeight);
            if (!topBlock.getFluidState().isEmpty()) return Optional.empty();
        }

        if (avoidStructures) {
            List<? extends String> structuresToAvoid = config.avoidStructures.get();
            Registry<StructureSet> structureSetRegistry = context.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
            for (String structureName : structuresToAvoid) {
                Optional<StructureSet> structureSetOptional = structureSetRegistry.getOptional(new ResourceLocation(structureName));
                if (structureSetOptional.isEmpty()) continue;
                Optional<ResourceKey<StructureSet>> resourceKeyOptional = structureSetRegistry.getResourceKey(structureSetOptional.get());
                if (resourceKeyOptional.isEmpty()) continue;
                if (context.chunkGenerator().hasStructureChunkInRange(BuiltinRegistries.STRUCTURE_SETS.getHolderOrThrow(resourceKeyOptional.get()), context.randomState(), context.seed(), chunkPos.x, chunkPos.z, 5)) {
                    return Optional.empty();
                }
            }
        }

        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> {
        	this.generatePieces(builder, context);
        });
    }
    
    public abstract void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context);

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public ConfigHandler.GenerationConfig getConfig() {
        return config;
    }
}
