package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.apache.logging.log4j.Level;

// Edited from Telepathic Grunt's base code

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class WroughtnautChamberStructure extends MowzieStructure {
    public WroughtnautChamberStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig;
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return WroughtnautChamberStructure.Start::new;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public boolean checkHeightLimitAgainstSurface() {
        return false;
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration>  {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, int chunkX, int chunkZ, BoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoneFeatureConfiguration config) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG);
            BlockPos pos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            WroughtnautChamberPieces.start(templateManagerIn, pos, rotation, this.pieces, this.random);

            //Sets the bounds of the structure.
//            this.recalculateStructureSize();
            this.boundingBox = BoundingBox.getUnknownBox();
            boundingBox.x0 = (chunkX << 4) + 7;
            boundingBox.z0 = (chunkZ << 4) + 7;
            boundingBox.y0 = surfaceY;
            boundingBox.x1 = boundingBox.x0 + 1;
            boundingBox.z1 = boundingBox.z0 + 1;
            boundingBox.y1 = boundingBox.y0 + 1;

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
//            MowziesMobs.LOGGER.log(Level.DEBUG, "Wroughtnaut at " +
//                    this.components.get(0).getBoundingBox().minX + " " +
//                    this.components.get(0).getBoundingBox().minY + " " +
//                    this.components.get(0).getBoundingBox().minZ);
        }
    }
}
