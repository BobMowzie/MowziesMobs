package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class WroughtnautChamberStructure extends MowzieStructure {
    public WroughtnautChamberStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), WroughtnautChamberStructure::generatePieces));
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig;
    }

    private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> pieceGenerator) {
        /*TODO
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

        // I use to debug and quickly find out if the structure is spawning or not and where it is.
        // This is returning the coordinates of the center starting piece.
//            MowziesMobs.LOGGER.log(Level.DEBUG, "Wroughtnaut at " +
//                    this.components.get(0).getBoundingBox().minX + " " +
//                    this.components.get(0).getBoundingBox().minY + " " +
//                    this.components.get(0).getBoundingBox().minZ);
         */
    }
}
