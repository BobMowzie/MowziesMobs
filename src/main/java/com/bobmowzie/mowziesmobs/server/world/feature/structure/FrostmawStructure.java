package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.ConfiguredFeatureHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class FrostmawStructure extends MowzieStructure {
    public FrostmawStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig, TagHandler.NO_FROSTMAW, FrostmawStructure::generatePieces);
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
