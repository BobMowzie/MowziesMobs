package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class MowzieStructure extends Structure {
    private final ConfigHandler.GenerationConfig config;
    private Set<Holder<Biome>> allowedBiomes;
    private boolean doCheckHeight;
    private boolean doAvoidWater;
    private boolean doAvoidStructures;

    public MowzieStructure(Structure.StructureSettings settings, ConfigHandler.GenerationConfig config, Set<Holder<Biome>> allowedBiomes, boolean doCheckHeight, boolean doAvoidWater, boolean doAvoidStructures) {
        super(settings);
        this.config = config;
        this.allowedBiomes = allowedBiomes;
        this.doCheckHeight = doCheckHeight;
        this.doAvoidWater = doAvoidWater;
        this.doAvoidStructures = doAvoidStructures;
    }

    public MowzieStructure(Structure.StructureSettings settings, ConfigHandler.GenerationConfig config, Set<Holder<Biome>> allowedBiomes) {
        this(settings, config, allowedBiomes, true, true, true);
    }

    public MowzieStructure(Structure.StructureSettings settings, ConfigHandler.GenerationConfig config) {
        super(settings);
        this.config = config;
    }
    
    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    	if(this.checkLocation(context)) {
    		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> {
    			this.generatePieces(builder, context);
    		});
    	}
    	return Optional.empty();
    }
    
    public void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
    	
    }
    
    public boolean checkLocation(GenerationContext context) {
    	return this.checkLocation(context, config, allowedBiomes, doCheckHeight, doAvoidWater, doAvoidStructures);
    }

    protected boolean checkLocation(GenerationContext context, ConfigHandler.GenerationConfig config, Set<Holder<Biome>> allowedBiomes, boolean checkHeight, boolean avoidWater, boolean avoidStructures) {
        if (config.generationDistance.get() < 0) {
            return false;
        }

        ChunkPos chunkPos = context.chunkPos();
        BlockPos centerOfChunk = new BlockPos((chunkPos.x << 4) + 7, 0, (chunkPos.z << 4) + 7);

        int i = chunkPos.getMiddleBlockX();
        int j = chunkPos.getMiddleBlockZ();
        int k = context.chunkGenerator().getFirstOccupiedHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        Holder<Biome> biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(i), QuartPos.fromBlock(k), QuartPos.fromBlock(j), context.randomState().sampler());
        if (!allowedBiomes.contains(biome)) {
            return false;
        }

        if (checkHeight) {
        	double minHeight = config.heightMin.get();
            double maxHeight = config.heightMax.get();
            int landHeight = getLowestY(context, 16, 16);
            if (minHeight != -65 && landHeight < minHeight) return false;
            if (maxHeight != -65 && landHeight > maxHeight) return false;
        }

        if (avoidWater) {
            ChunkGenerator chunkGenerator = context.chunkGenerator();
            LevelHeightAccessor heightLimitView = context.heightAccessor();
            int centerHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, context.randomState());
            NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView, context.randomState());
            BlockState topBlock = columnOfBlocks.getBlock(centerHeight);
            if (!topBlock.getFluidState().isEmpty()) return false;
        }

        /* TODO: Fix this later
        if (avoidStructures) {
            List<? extends String> structuresToAvoid = config.avoidStructures.get();
            ChunkGeneratorStructureState generatorState = serverLevel.getChunkSource().getGeneratorState();
            Registry<StructureSet> structureSetRegistry = context.registryAccess().registryOrThrow(Registries.STRUCTURE_SET);
            for (String structureName : structuresToAvoid) {
                Optional<StructureSet> structureSetOptional = structureSetRegistry.getOptional(new ResourceLocation(structureName));
                if (structureSetOptional.isEmpty()) continue;
                Optional<ResourceKey<StructureSet>> resourceKeyOptional = structureSetRegistry.getResourceKey(structureSetOptional.get());
                if (resourceKeyOptional.isEmpty()) continue;
                Optional<Holder.Reference<StructureSet>> holderOptional = structureSetRegistry.getHolder(resourceKeyOptional.get());
                if (holderOptional.isEmpty()) continue;
                if (generatorState.hasStructureChunkInRange(holderOptional.get(), chunkPos.x, chunkPos.z, 3)) {
                    return false;
                }
            }
        }*/

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
