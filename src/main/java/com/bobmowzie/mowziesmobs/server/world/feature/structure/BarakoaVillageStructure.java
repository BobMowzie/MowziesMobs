package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Arrays;

// Edited from Telepathic Grunt's base code

public class BarakoaVillageStructure extends MowzieStructure {
    public BarakoaVillageStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, ConfigHandler.COMMON.MOBS.BARAKO.generationConfig, TagHandler.NO_BARAKOA_VILLAGE, BarakoaVillageStructure::generatePieces);
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
        builder.addPiece(new BarakoaVillagePieces.FirepitPiece(random, x - 4, z - 4));

        //Throne
        BlockPos offset = new BlockPos(0, 0, 9);
        offset = offset.rotate(rotation);
        BlockPos thronePos = posToSurface(generator, centerPos.offset(offset), heightLimitView);
        BarakoaVillagePieces.addPiece(BarakoaVillagePieces.THRONE, pieceGenerator.structureManager(), thronePos, rotation, builder, pieceGenerator.random());

        //Houses
        int numHouses = random.nextInt(4) + 3;
        for (int i = 1; i <= numHouses; i++) {
            for (int j = 0; j < 30; j++) {
                float distance = random.nextInt(8) + 10;
                int angle = random.nextInt(360);
                BlockPos housePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                housePos = posToSurface(generator, housePos, heightLimitView);
                housePos = housePos.relative(Direction.UP, random.nextInt(2));
                if (startHouse(generator, pieceGenerator.structureManager(), builder, housePos, pieceGenerator.random())) break;
            }
        }

        //Altar
        int numAltars = random.nextInt(3) + 2;
        for (int i = 1; i <= numAltars; i++) {
            int distance;
            int angle;
            for (int j = 1; j <= 10; j++) {
                distance = random.nextInt(15) + 5;
                angle = random.nextInt(360);
                BlockPos altarPos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                altarPos = posToSurface(generator, altarPos, heightLimitView);
                StructurePiece altar = new BarakoaVillagePieces.AltarPiece(altarPos.getX(), altarPos.getY(), altarPos.getZ(), Direction.NORTH);
                if (builder.findCollisionPiece(altar.getBoundingBox()) != null) {
                    continue;
                }
                builder.addPiece(altar);
            }
        }

        //Stakes
        int numStakes = random.nextInt(12) + 5;
        for (int i = 1; i <= numStakes; i++) {
            int distance;
            int angle;
            for (int j = 1; j <= 10; j++) {
                distance = random.nextInt(15) + 5;
                angle = random.nextInt(360);
                BlockPos stakePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                stakePos = posToSurface(generator, stakePos, heightLimitView);
                StructurePiece stake = new BarakoaVillagePieces.StakePiece(random, stakePos.getX(), stakePos.getY(), stakePos.getZ());
                if (builder.findCollisionPiece(stake.getBoundingBox()) != null) {
                    continue;
                }
                builder.addPiece(stake);
                break;
            }
        }
    }

    private static boolean startHouse(ChunkGenerator generator, StructureManager templateManagerIn, StructurePiecesBuilder builder, BlockPos housePos, WorldgenRandom random) {
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        StructurePiece newHouse = BarakoaVillagePieces.addHouse(templateManagerIn, housePos, rotation, builder, random);
        if (newHouse != null) {
            Rotation roofRotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            StructurePiece roof = BarakoaVillagePieces.addPiece(BarakoaVillagePieces.ROOF, templateManagerIn, housePos, roofRotation, builder, random);

            int sideHouseDir = random.nextInt(6) + 1;
            if (sideHouseDir <= 2) {
                Rotation sideHouseRotation = sideHouseDir == 1 ? rotation.getRotated(Rotation.CLOCKWISE_90) : rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
                if (BarakoaVillagePieces.addPieceCheckBounds(BarakoaVillagePieces.HOUSE_SIDE, templateManagerIn, housePos, sideHouseRotation, builder, random, Arrays.asList(newHouse, roof)) == null) {
                    sideHouseRotation = sideHouseRotation.getRotated(Rotation.CLOCKWISE_180);
                    BarakoaVillagePieces.addPieceCheckBounds(BarakoaVillagePieces.HOUSE_SIDE, templateManagerIn, housePos, sideHouseRotation, builder, random, Arrays.asList(newHouse, roof));
                }
            }
            return true;
        }
        return false;
    }

    private static BlockPos posToSurface(ChunkGenerator generator, BlockPos pos, LevelHeightAccessor heightAccessor) {
        int surfaceY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
        return new BlockPos(pos.getX(), surfaceY - 1, pos.getZ());
    }
}
