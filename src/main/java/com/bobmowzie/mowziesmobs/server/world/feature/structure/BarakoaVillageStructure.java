package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.*;

// Edited from Telepathic Grunt's base code
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

public class BarakoaVillageStructure extends MowzieStructure {
    public BarakoaVillageStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.COMMON.MOBS.BARAKO.generationConfig;
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return BarakoaVillageStructure.Start::new;
    }

    @Override
    public boolean avoidStructures() {
        return true;
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration>  {
        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, int chunkX, int chunkZ, BoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator generator, StructureManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoneFeatureConfiguration config) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos centerPos = new BlockPos(x, 1, z);

            int surfaceY = generator.getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
            int oceanFloorY = generator.getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.OCEAN_FLOOR_WG);
            if (oceanFloorY < surfaceY) return;

            //Firepit
            pieces.add(new BarakoaVillagePieces.FirepitPiece(this.random, x - 4, z - 4));

            //Throne
            BlockPos offset = new BlockPos(0, 0, 9);
            offset = offset.rotate(rotation);
            BlockPos thronePos = posToSurface(generator, centerPos.offset(offset));
            BarakoaVillagePieces.addPiece(BarakoaVillagePieces.THRONE, templateManagerIn, thronePos, rotation, this.pieces, this.random);

            //Houses
            int numHouses = random.nextInt(4) + 3;
            for (int i = 1; i <= numHouses; i++) {
                for (int j = 0; j < 30; j++) {
                    float distance = random.nextInt(8) + 10;
                    int angle = random.nextInt(360);
                    BlockPos housePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                    housePos = posToSurface(generator, housePos);
                    housePos = housePos.relative(Direction.UP, random.nextInt(2));
                    if (startHouse(generator, templateManagerIn, housePos)) break;
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
                    altarPos = posToSurface(generator, altarPos);
                    StructurePiece altar = new BarakoaVillagePieces.AltarPiece(random, altarPos.getX(), altarPos.getY(), altarPos.getZ());
                    boolean intersects = false;
                    for (StructurePiece piece : pieces) {
                        if (altar.getBoundingBox().intersects(piece.getBoundingBox())) {
                            intersects = true;
                            break;
                        }
                    }
                    if (!intersects) {
                        pieces.add(altar);
                        break;
                    }
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
                    stakePos = posToSurface(generator, stakePos);
                    StructurePiece stake = new BarakoaVillagePieces.StakePiece(random, stakePos.getX(), stakePos.getY(), stakePos.getZ());
                    boolean intersects = false;
                    for (StructurePiece piece : pieces) {
                        if (stake.getBoundingBox().intersects(piece.getBoundingBox())) {
                            intersects = true;
                            break;
                        }
                    }
                    if (!intersects) {
                        pieces.add(stake);
                        break;
                    }
                }
            }

            //Sets the bounds of the structure.
            this.calculateBoundingBox();

//            MowziesMobs.LOGGER.log(Level.DEBUG, "Barako at " +
//                    this.components.get(0).getBoundingBox().minX + " " +
//                    this.components.get(0).getBoundingBox().minY + " " +
//                    this.components.get(0).getBoundingBox().minZ);
        }

        private boolean startHouse(ChunkGenerator generator, StructureManager templateManagerIn, BlockPos housePos) {
            Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
            StructurePiece newHouse = BarakoaVillagePieces.addHouse(templateManagerIn, housePos, rotation, this.pieces, this.random);
            if (newHouse != null) {
                Rotation roofRotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
                StructurePiece roof = BarakoaVillagePieces.addPiece(BarakoaVillagePieces.ROOF, templateManagerIn, housePos, roofRotation, this.pieces, this.random);

                int sideHouseDir = random.nextInt(6) + 1;
                if (sideHouseDir <= 2) {
                    Rotation sideHouseRotation = sideHouseDir == 1 ? rotation.getRotated(Rotation.CLOCKWISE_90) : rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
                    if (BarakoaVillagePieces.addPieceCheckBounds(BarakoaVillagePieces.HOUSE_SIDE, templateManagerIn, housePos, sideHouseRotation, this.pieces, this.random, Arrays.asList(newHouse, roof)) == null) {
                        sideHouseRotation = sideHouseRotation.getRotated(Rotation.CLOCKWISE_180);
                        BarakoaVillagePieces.addPieceCheckBounds(BarakoaVillagePieces.HOUSE_SIDE, templateManagerIn, housePos, sideHouseRotation, this.pieces, this.random, Arrays.asList(newHouse, roof));
                    }
                }
                return true;
            }
            return false;
        }

        private BlockPos posToSurface(ChunkGenerator generator, BlockPos pos) {
            int surfaceY = generator.getBaseHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
            return new BlockPos(pos.getX(), surfaceY - 1, pos.getZ());
        }
    }
}
