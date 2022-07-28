package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

// Edited from Telepathic Grunt's base code

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
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryAccess, ChunkGenerator generator, StructureManager templateManagerIn, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor heightLimitView) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;
            int surfaceY = generator.getBaseHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG, heightLimitView);
            BlockPos pos = new BlockPos(x, surfaceY, z);

            //Now adds the structure pieces to this.components with all details such as where each part goes
            //so that the structure can be added to the world by worldgen.
            WroughtnautChamberPieces.start(templateManagerIn, pos, rotation, this.pieces, this.random);

            //Sets the bounds of the structure.
            this.getBoundingBox();
            /*
            this.boundingBox = BoundingBox.getUnknownBox();
            boundingBox.x0 = (chunkX << 4) + 7;
            boundingBox.z0 = (chunkZ << 4) + 7;
            boundingBox.y0 = surfaceY;
            boundingBox.x1 = boundingBox.x0 + 1;
            boundingBox.z1 = boundingBox.z0 + 1;
            boundingBox.y1 = boundingBox.y0 + 1;*/

            // I use to debug and quickly find out if the structure is spawning or not and where it is.
            // This is returning the coordinates of the center starting piece.
//            MowziesMobs.LOGGER.log(Level.DEBUG, "Wroughtnaut at " +
//                    this.components.get(0).getBoundingBox().minX + " " +
//                    this.components.get(0).getBoundingBox().minY + " " +
//                    this.components.get(0).getBoundingBox().minZ);
        }
    }
}
