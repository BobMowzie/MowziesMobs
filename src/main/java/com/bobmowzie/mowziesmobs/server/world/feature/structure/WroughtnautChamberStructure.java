package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;

// Edited from Telepathic Grunt's base code

public class WroughtnautChamberStructure extends MowzieStructure {
    public WroughtnautChamberStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return null;
    }
    /*public WroughtnautChamberStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
    {
        super(config);
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
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationConfig;
    }

    @Override
    public boolean checkHeightLimitAgainstSurface() {
        return false;
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
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
            int surfaceY = generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos pos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            WroughtnautChamberPieces.start(templateManagerIn, pos, rotation, this.components, this.rand);
//            System.out.println("Wroughtnaut chamber at " + pos.getX() + " " + pos.getY() + " " + pos.getZ());

            //Sets the bounds of the structure.
//            this.recalculateStructureSize();
            this.bounds = MutableBoundingBox.getNewBoundingBox();
            bounds.minX = (chunkX << 4) + 7;
            bounds.minZ = (chunkZ << 4) + 7;
            bounds.minY = surfaceY;
            bounds.maxX = bounds.minX + 1;
            bounds.maxZ = bounds.minZ + 1;
            bounds.maxY = bounds.minY + 1;
        }
    }*/ // TODO
}
