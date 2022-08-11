package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class FrostmawStructure extends MowzieStructure {
    public FrostmawStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig, FrostmawStructure::generatePieces);
    }

    private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> pieceGenerator) {
        int x = pieceGenerator.chunkPos().getMinBlockX();
        int z = pieceGenerator.chunkPos().getMinBlockZ();
        int y = pieceGenerator.chunkGenerator().getFirstFreeHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, pieceGenerator.heightAccessor());
        BlockPos blockpos = new BlockPos(x, y, z);
        Rotation rotation = Rotation.getRandom(pieceGenerator.random());
        FrostmawPieces.addPieces(pieceGenerator.structureManager(), blockpos, rotation, builder, pieceGenerator.random());
    }
}
