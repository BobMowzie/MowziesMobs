package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Direction;
import net.minecraft.resources.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.math.MutableBoundingBox;
import net.minecraft.resources.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.*;

// Edited from Telepathic Grunt's base code
public class BarakoaVillageStructure extends MowzieStructure {
    public BarakoaVillageStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.COMMON.MOBS.BARAKO.generationConfig;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return BarakoaVillageStructure.Start::new;
    }

    @Override
    public boolean avoidStructures() {
        return true;
    }

    public static class Start extends StructureStart<NoFeatureConfig>  {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos centerPos = new BlockPos(x, 1, z);

            int surfaceY = generator.getHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
            int oceanFloorY = generator.getHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.OCEAN_FLOOR_WG);
            if (oceanFloorY < surfaceY) return;

            //Firepit
            components.add(new BarakoaVillagePieces.FirepitPiece(this.rand, x - 4, z - 4));

            //Throne
            BlockPos offset = new BlockPos(0, 0, 9);
            offset = offset.rotate(rotation);
            BlockPos thronePos = posToSurface(generator, centerPos.add(offset));
            BarakoaVillagePieces.addPiece(BarakoaVillagePieces.THRONE, templateManagerIn, thronePos, rotation, this.components, this.rand);

            //Houses
            int numHouses = rand.nextInt(4) + 3;
            for (int i = 1; i <= numHouses; i++) {
                for (int j = 0; j < 30; j++) {
                    float distance = rand.nextInt(8) + 10;
                    int angle = rand.nextInt(360);
                    BlockPos housePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                    housePos = posToSurface(generator, housePos);
                    housePos = housePos.offset(Direction.UP, rand.nextInt(2));
                    if (startHouse(generator, templateManagerIn, housePos)) break;
                }
            }

            //Altar
            int numAltars = rand.nextInt(3) + 2;
            for (int i = 1; i <= numAltars; i++) {
                int distance;
                int angle;
                for (int j = 1; j <= 10; j++) {
                    distance = rand.nextInt(15) + 5;
                    angle = rand.nextInt(360);
                    BlockPos altarPos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                    altarPos = posToSurface(generator, altarPos);
                    StructurePiece altar = new BarakoaVillagePieces.AltarPiece(rand, altarPos.getX(), altarPos.getY(), altarPos.getZ());
                    boolean intersects = false;
                    for (StructurePiece piece : components) {
                        if (altar.getBoundingBox().intersectsWith(piece.getBoundingBox())) {
                            intersects = true;
                            break;
                        }
                    }
                    if (!intersects) {
                        components.add(altar);
                        break;
                    }
                }
            }

            //Stakes
            int numStakes = rand.nextInt(12) + 5;
            for (int i = 1; i <= numStakes; i++) {
                int distance;
                int angle;
                for (int j = 1; j <= 10; j++) {
                    distance = rand.nextInt(15) + 5;
                    angle = rand.nextInt(360);
                    BlockPos stakePos = new BlockPos(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                    stakePos = posToSurface(generator, stakePos);
                    StructurePiece stake = new BarakoaVillagePieces.StakePiece(rand, stakePos.getX(), stakePos.getY(), stakePos.getZ());
                    boolean intersects = false;
                    for (StructurePiece piece : components) {
                        if (stake.getBoundingBox().intersectsWith(piece.getBoundingBox())) {
                            intersects = true;
                            break;
                        }
                    }
                    if (!intersects) {
                        components.add(stake);
                        break;
                    }
                }
            }

            //Sets the bounds of the structure.
            this.recalculateStructureSize();

//            MowziesMobs.LOGGER.log(Level.DEBUG, "Barako at " +
//                    this.components.get(0).getBoundingBox().minX + " " +
//                    this.components.get(0).getBoundingBox().minY + " " +
//                    this.components.get(0).getBoundingBox().minZ);
        }

        private boolean startHouse(ChunkGenerator generator, TemplateManager templateManagerIn, BlockPos housePos) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            StructurePiece newHouse = BarakoaVillagePieces.addHouse(templateManagerIn, housePos, rotation, this.components, this.rand);
            if (newHouse != null) {
                Rotation roofRotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
                StructurePiece roof = BarakoaVillagePieces.addPiece(BarakoaVillagePieces.ROOF, templateManagerIn, housePos, roofRotation, this.components, this.rand);

                int sideHouseDir = rand.nextInt(6) + 1;
                if (sideHouseDir <= 2) {
                    Rotation sideHouseRotation = sideHouseDir == 1 ? rotation.add(Rotation.CLOCKWISE_90) : rotation.add(Rotation.COUNTERCLOCKWISE_90);
                    if (BarakoaVillagePieces.addPieceCheckBounds(BarakoaVillagePieces.HOUSE_SIDE, templateManagerIn, housePos, sideHouseRotation, this.components, this.rand, Arrays.asList(newHouse, roof)) == null) {
                        sideHouseRotation = sideHouseRotation.add(Rotation.CLOCKWISE_180);
                        BarakoaVillagePieces.addPieceCheckBounds(BarakoaVillagePieces.HOUSE_SIDE, templateManagerIn, housePos, sideHouseRotation, this.components, this.rand, Arrays.asList(newHouse, roof));
                    }
                }
                return true;
            }
            return false;
        }

        private BlockPos posToSurface(ChunkGenerator generator, BlockPos pos) {
            int surfaceY = generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
            return new BlockPos(pos.getX(), surfaceY - 1, pos.getZ());
        }
    }
}
