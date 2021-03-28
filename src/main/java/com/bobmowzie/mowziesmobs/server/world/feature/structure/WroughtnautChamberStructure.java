package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

// Edited from Telepathic Grunt's base code

public class WroughtnautChamberStructure extends MowzieStructure {
    public WroughtnautChamberStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return WroughtnautChamberStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public boolean checkHeightLimitAgainstSurface() {
        return false;
    }

    public static class Start extends StructureStart<NoFeatureConfig>  {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            BlockPos pos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            WroughtnautChamberPieces.start(templateManagerIn, pos, rotation, this.components, this.rand);

            //Sets the bounds of the structure.
//            this.recalculateStructureSize();
            this.bounds = MutableBoundingBox.getNewBoundingBox();
            bounds.minX = (chunkX << 4) + 7;
            bounds.minZ = (chunkZ << 4) + 7;
            bounds.minY = surfaceY;
            bounds.maxX = bounds.minX + 1;
            bounds.maxZ = bounds.minZ + 1;
            bounds.maxY = bounds.minY + 1;

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
//            MowziesMobs.LOGGER.log(Level.DEBUG, "Wroughtnaut at " +
//                    this.components.get(0).getBoundingBox().minX + " " +
//                    this.components.get(0).getBoundingBox().minY + " " +
//                    this.components.get(0).getBoundingBox().minZ);
        }
    }
}
