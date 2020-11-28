package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import com.bobmowzie.mowziesmobs.server.world.structure.StructureWroughtnautRoom;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.BiomeManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// Edited from Telepathic Grunt's base code

public class WroughtnautChamberStructure extends ScatteredStructure<NoFeatureConfig> {
    public WroughtnautChamberStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
    {
        super(config);
    }

    @Override
    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return 32;
    }

    @Override
    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return 8;
    }

    @Override
    public String getStructureName()
    {
        return MowziesMobs.MODID + ":wroughtnaut_chamber";
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public Structure.IStartFactory getStartFactory()
    {
        return WroughtnautChamberStructure.Start::new;
    }

    protected int getSeedModifier()
    {
        return 123444789;
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        List<String> dimensionNames = Arrays.asList(ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationData.dimensions);
        ResourceLocation currDimensionName = worldIn.getDimension().getType().getRegistryName();
        if (currDimensionName == null || !dimensionNames.contains(currDimensionName.toString())) {
            return false;
        }

        return super.place(worldIn, generator, rand, pos, config);
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            //Check out vanilla's WoodlandMansionStructure for how they offset the x and z
            //so that they get the y value of the land at the mansion's entrance, no matter
            //which direction the mansion is rotated.
            //
            //However, for most purposes, getting the y value of land with the default x and z is good enough.
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos blockpos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            WroughtnautChamberPieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

            //Sets the bounds of the structure.
            this.recalculateStructureSize();

//            System.out.println("Wroughtnaut chamber at " + blockpos.getX() + " " + blockpos.getY() + " " + blockpos.getZ());
        }
    }
}
