package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.*;
import java.util.function.Function;

// Edited from Telepathic Grunt's base code

public class BarakoaVillageStructure extends MowzieStructure {
    public BarakoaVillageStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return null;
    }
    /*public BarakoaVillageStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
    {
        super(config);
    }

    @Override
    public String getStructureName()
    {
        return MowziesMobs.MODID + ":barakoa_village";
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public IStartFactory getStartFactory()
    {
        return BarakoaVillageStructure.Start::new;
    }

    protected int getSeedModifier()
    {
        return 123555789;
    }

    @Override
    public ConfigHandler.GenerationConfig getGenerationConfig() {
        return ConfigHandler.MOBS.BARAKO.generationConfig;
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(structureIn, chunkX, chunkZ, boundsIn, referenceIn, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos centerPos = new BlockPos(x, 1, z);

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

//            System.out.println("Barakoa village at " + centerPos.getX() + " " + centerPos.getY() + " " + centerPos.getZ());
        }

        private boolean startHouse(ChunkGenerator<?> generator, TemplateManager templateManagerIn, BlockPos housePos) {
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

        private BlockPos posToSurface(ChunkGenerator<?> generator, BlockPos pos) {
            int surfaceY = generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
            return new BlockPos(pos.getX(), surfaceY, pos.getZ());
        }
    }*/ // TODO
}
