package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.ConfiguredFeatureHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

// Edited from Telepathic Grunt's base code

public class BarakoaVillageStructure extends MowzieStructure<NoneFeatureConfiguration> {
    public BarakoaVillageStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, ConfigHandler.COMMON.MOBS.BARAKO.generationConfig, ConfiguredFeatureHandler.BARAKO_BIOMES, BarakoaVillageStructure::generatePieces);
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
        builder.addPiece(new BarakoaVillagePieces.FirepitPiece(pieceGenerator.structureManager(), Rotation.values()[random.nextInt(Rotation.values().length)], firepitPos));

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
                ResourceLocation whichSpike = BarakoaVillagePieces.SPIKES[random.nextInt(BarakoaVillagePieces.SPIKES.length)];
                StructurePiece piece = BarakoaVillagePieces.addPiece(whichSpike, pieceGenerator.structureManager(), stakePos, Rotation.values()[random.nextInt(Rotation.values().length)], builder, pieceGenerator.random());
                if (piece != null) {
                    break;
                }
            }
        }
    }

    private static boolean startHouse(ChunkGenerator generator, StructureManager templateManagerIn, StructurePiecesBuilder builder, BlockPos housePos, WorldgenRandom random) {
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        StructurePiece newHouse = BarakoaVillagePieces.addPlatform(templateManagerIn, housePos, rotation, builder, random);
        return newHouse != null;
    }

    private static BlockPos posToSurface(ChunkGenerator generator, BlockPos pos, LevelHeightAccessor heightAccessor) {
        int surfaceY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
        return new BlockPos(pos.getX(), surfaceY - 1, pos.getZ());
    }
}
