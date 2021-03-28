package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class MowzieStructure extends ScatteredStructure<NoFeatureConfig> {
    public MowzieStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
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
        List<? extends String> dimensionNames = getGenerationConfig().dimensions.get();
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
    }
}
