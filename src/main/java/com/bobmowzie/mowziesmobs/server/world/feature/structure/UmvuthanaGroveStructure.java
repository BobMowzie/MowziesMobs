package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.ConfiguredFeatureHandler;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

// Edited from Telepathic Grunt's base code

public class UmvuthanaGroveStructure extends MowzieStructure<NoneFeatureConfiguration> {
    public UmvuthanaGroveStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig, ConfiguredFeatureHandler.UMVUTHI_BIOMES, UmvuthanaGroveStructure::generatePieces);
    }

    private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> pieceGenerator) {
        Rotation rotation = Rotation.values()[pieceGenerator.random().nextInt(Rotation.values().length)];

        //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
        int x = (pieceGenerator.chunkPos().x << 4) + 7;
        int z = (pieceGenerator.chunkPos().z << 4) + 7;
        BlockPos centerPos = new BlockPos(x, 1, z);

        ChunkGenerator generator = pieceGenerator.chunkGenerator();
        LevelHeightAccessor heightLimitView = pieceGenerator.heightAccessor();
        WorldgenRandom random = pieceGenerator.random();

        int surfaceY = generator.getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView);
        int oceanFloorY = generator.getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, heightLimitView);
        if (oceanFloorY < surfaceY) return;

        //Firepit
        BlockPos firepitPos = posToSurface(generator, centerPos, heightLimitView);
        builder.addPiece(new UmvuthanaGrovePieces.FirepitPiece(pieceGenerator.structureTemplateManager(), Rotation.values()[random.nextInt(Rotation.values().length)], firepitPos));

        //Throne
        BlockPos offset = new BlockPos(0, 0, 9);
        offset = offset.rotate(rotation);
        BlockPos thronePos = posToSurface(generator, centerPos.offset(offset), heightLimitView);
        UmvuthanaGrovePieces.addPiece(UmvuthanaGrovePieces.THRONE, pieceGenerator.structureTemplateManager(), thronePos, rotation, builder, pieceGenerator.random());

        //Platforms
        int numHouses = random.nextInt(2) + 2;
        for (int i = 1; i <= numHouses; i++) {
            for (int j = 0; j < 30; j++) {
                float distance = random.nextInt(8) + 13;
                int angle = random.nextInt(360);
                BlockPos housePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                housePos = posToSurface(generator, housePos, heightLimitView);
                int houseOceanFloorY = generator.getBaseHeight(housePos.getX(), housePos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, heightLimitView);
                if (houseOceanFloorY < housePos.getY()) continue;
                if (startPlatform(generator, pieceGenerator.structureTemplateManager(), builder, housePos, pieceGenerator.random())) break;
            }
        }

        //Trees
        int numTrees = random.nextInt(3) + 2;
        for (int i = 1; i <= numTrees; i++) {
            for (int j = 0; j < 30; j++) {
                float distance = random.nextInt(14) + 13;
                int angle = random.nextInt(360);
                BlockPos treePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                treePos = posToSurface(generator, treePos, heightLimitView);
                int treeOceanFloorY = generator.getBaseHeight(treePos.getX(), treePos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, heightLimitView);
                if (treeOceanFloorY < treePos.getY()) continue;
                int whichTree = random.nextInt(UmvuthanaGrovePieces.TREES.length);
                StructurePiece tree = UmvuthanaGrovePieces.addPieceCheckBounds(UmvuthanaGrovePieces.TREES[whichTree], pieceGenerator.structureTemplateManager(), treePos, Rotation.values()[random.nextInt(Rotation.values().length)], builder, pieceGenerator.random());
                if (tree != null) break;
            }
        }

        //Small firepits
        int numFirepits = random.nextInt(3) + 2;
        for (int i = 1; i <= numFirepits; i++) {
            int distance;
            int angle;
            for (int j = 1; j <= 10; j++) {
                distance = random.nextInt(15) + 8;
                angle = random.nextInt(360);
                BlockPos pitPos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                pitPos = posToSurface(generator, pitPos, heightLimitView);
                int pitOceanFloorY = generator.getBaseHeight(pitPos.getX(), pitPos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, heightLimitView);
                if (pitOceanFloorY < pitPos.getY()) continue;
                ResourceLocation whichPit = UmvuthanaGrovePieces.FIREPIT_SMALL[random.nextInt(UmvuthanaGrovePieces.FIREPIT_SMALL.length)];
                StructurePiece piece = UmvuthanaGrovePieces.addPieceCheckBounds(whichPit, pieceGenerator.structureTemplateManager(), pitPos, Rotation.values()[random.nextInt(Rotation.values().length)], builder, pieceGenerator.random());
                if (piece != null) {
                    break;
                }
            }
        }

        //Stakes
        int numStakes = random.nextInt(10) + 7;
        for (int i = 1; i <= numStakes; i++) {
            int distance;
            int angle;
            for (int j = 1; j <= 10; j++) {
                distance = random.nextInt(15) + 8;
                angle = random.nextInt(360);
                BlockPos stakePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                stakePos = posToSurface(generator, stakePos, heightLimitView);
                int stakeOceanFloorY = generator.getBaseHeight(stakePos.getX(), stakePos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG, heightLimitView);
                if (stakeOceanFloorY < stakePos.getY()) continue;
                ResourceLocation whichSpike = UmvuthanaGrovePieces.SPIKES[random.nextInt(UmvuthanaGrovePieces.SPIKES.length)];
                StructurePiece piece = UmvuthanaGrovePieces.addPieceCheckBounds(whichSpike, pieceGenerator.structureTemplateManager(), stakePos, Rotation.values()[random.nextInt(Rotation.values().length)], builder, pieceGenerator.random());
                if (piece != null) {
                    break;
                }
            }
        }
    }

    private static boolean startPlatform(ChunkGenerator generator, StructureTemplateManager templateManagerIn, StructurePiecesBuilder builder, BlockPos housePos, WorldgenRandom random) {
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        StructurePiece newPlatform = UmvuthanaGrovePieces.addPlatform(templateManagerIn, housePos, rotation, builder, random);
        return newPlatform != null;
    }

    private static BlockPos posToSurface(ChunkGenerator generator, BlockPos pos, LevelHeightAccessor heightAccessor) {
        int surfaceY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
        return new BlockPos(pos.getX(), surfaceY - 1, pos.getZ());
    }
}
