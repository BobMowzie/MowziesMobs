package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

public abstract class MowzieStructure extends Structure<NoFeatureConfig> {
    public MowzieStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }
    /*public MowzieStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return getGenerationConfig().generationDistance.get();
    }

    @Override
    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return getGenerationConfig().generationSeparation.get();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (getBiomeFeatureDistance(generator) == -1 || getBiomeFeatureSeparation(generator) == -1) return false;
        List<String> dimensionNames = getGenerationConfig().dimensions.get();
        ResourceLocation currDimensionName = worldIn.getDimension().getType().getRegistryName();
        if (currDimensionName == null || !dimensionNames.contains(currDimensionName.toString())) {
            return false;
        }
        if (checkHeightLimitAgainstSurface()) {
            int surfaceY = generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
            if (surfaceY < getGenerationConfig().heightMin.get()) return false;
            if (surfaceY > getGenerationConfig().heightMax.get()) return false;
        }

        return super.place(worldIn, generator, rand, pos, config);
    }

    public abstract ConfigHandler.GenerationConfig getGenerationConfig();

    public boolean checkHeightLimitAgainstSurface() {
        return true;
    }*/ //TODO
}
