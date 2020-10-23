package com.bobmowzie.mowziesmobs.server.world.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import com.ilexiconn.llibrary.server.structure.StructureBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

/**
 * Created by Josh on 10/8/2016.
 */
public class StructureWroughtnautRoom {
    public static void generate(World world, BlockPos pos, Random rand, Direction dir) {
        StructureBuilder structure = new StructureBuilder().startComponent()
                .cube(0 - 9, 0, 0, 19, 7, 19, Blocks.STONE)
                .fillCube(1 - 9, 1, 1, 17, 5, 17, Blocks.AIR)
                .fillCube(8 - 9, 1, 0, 3, 5, 1, Blocks.AIR)
                .cube(7 - 9, 0, 0, 5, 7, 1, Blocks.COBBLESTONE)
                .cube(7 - 9, 6, 0, 5, 1, 16, Blocks.COBBLESTONE)
                .cube(3 - 9, 6, 3, 13, 1, 5, Blocks.COBBLESTONE)
                .cube(3 - 9, 6, 7, 13, 1, 5, Blocks.COBBLESTONE)
                .cube(3 - 9, 6, 11, 13, 1, 5, Blocks.COBBLESTONE)
                .cube(7 - 9, 0, 0, 5, 1, 16, Blocks.COBBLESTONE)
                .cube(3 - 9, 0, 3, 13, 1, 5, Blocks.COBBLESTONE)
                .cube(3 - 9, 0, 7, 13, 1, 5, Blocks.COBBLESTONE)
                .cube(3 - 9, 0, 11, 13, 1, 5, Blocks.COBBLESTONE)
                .fillCube(1 - 9, 1, 1, 6, 5, 2, Blocks.STONE)
                .fillCube(12 - 9, 1, 1, 6, 5, 2, Blocks.STONE)
                .fillCube(7 - 9, 1, 3, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(11 - 9, 1, 3, 1, 5, 1, Blocks.COBBLESTONE)
                .setBlock(8 - 9, 5, 0, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(10 - 9, 5, 0, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(7 - 9, 5, 2, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(11 - 9, 5, 2, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(7 - 9, 5, 1, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(11 - 9, 5, 1,Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(8 - 9, 5, 3, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(10 - 9, 5, 3, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(7 - 9, 5, 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(11 - 9, 5, 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(6 - 9, 5, 3, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(12 - 9, 5, 3, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP))
                .fillCube(3 - 9, 1, 3, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(3 - 9, 1, 7, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(3 - 9, 1, 11, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(3 - 9, 1, 15, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(7 - 9, 1, 15, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(11 - 9, 1, 15, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(15 - 9, 1, 15, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(15 - 9, 1, 3, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(15 - 9, 1, 7, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(15 - 9, 1, 11, 1, 5, 1, Blocks.COBBLESTONE)
                .fillCube(1 - 9, 1, 3, 2, 5, 15, Blocks.STONE)
                .fillCube(16 - 9, 1, 3, 2, 5, 15, Blocks.STONE)
                .fillCube(3 - 9, 1, 16, 13, 5, 2, Blocks.STONE)
                .fillCube(8 - 9, 0, 1, 3, 1, 2, Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE))
                .setBlock(4 - 9, 5, 3, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(3 - 9, 5, 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP))
                .setBlock(14 - 9, 5, 3, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP))
                .setBlock(15 - 9, 5, 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP))
                .fillCube(5 - 9, 2, 1, 1, 1, 2, Blocks.AIR)
                .setBlock(5 - 9, 2, 1, Blocks.TORCH)
                .setBlock(5 - 9, 2, 2, Blocks.IRON_BARS)
                .fillCube(13 - 9, 2, 1, 1, 1, 2, Blocks.AIR)
                .setBlock(13 - 9, 2, 1, Blocks.TORCH)
                .setBlock(13 - 9, 2, 2, Blocks.IRON_BARS);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                structure.fillCube(4 - 9 + i * 4, 0, 4 + j * 4, 3, 1, 3, Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE));
                if (i == 0) { //left
                    structure.setBlock(3 - 9, 5, 6 + j * 4,Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(4 - 9, 5, 7 + j * 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP));
                    if (j != 2) {
                        structure.setBlock(3 - 9, 5, 8 + j * 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP));
                    }
                    structure.fillCube(1 - 9, 2, 5 + 4 * j, 2, 1, 1, Blocks.AIR);
                    structure.setBlock(1 - 9, 2, 5 + 4 * j, Blocks.TORCH);
                    structure.setBlock(2 - 9, 2, 5 + 4 * j, Blocks.IRON_BARS);
                } else if (i == 1) { //right
                    structure.setBlock(15 - 9, 5, 6 + j * 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(14 - 9, 5, 7 + j * 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP));
                    if (j != 2) {
                        structure.setBlock(15 - 9, 5, 8 + j * 4, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP));
                    }
                    structure.fillCube(16 - 9, 2, 5 + 4 * j, 2, 1, 1, Blocks.AIR);
                    structure.setBlock(17 - 9, 2, 5 + 4 * j, Blocks.TORCH);
                    structure.setBlock(16 - 9, 2, 5 + 4 * j, Blocks.IRON_BARS);
                } else { //back
                    structure.setBlock(12 - 9, 5, 15, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(11 - 9, 5, 14, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(10 - 9, 5, 15, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(8 - 9, 5, 15, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(7 - 9, 5, 14, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP));
                    structure.setBlock(6 - 9, 5, 15, Blocks.STONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP));
                    structure.fillCube(5 - 9 + 4 * j, 2, 16, 1, 1, 2, Blocks.AIR);
                    structure.setBlock(5 - 9 + 4 * j, 2, 17, Blocks.TORCH);
                    structure.setBlock(5 - 9 + 4 * j, 2, 16, Blocks.IRON_BARS);
                }
            }
        }
        structure.endComponent();
        structure.rotate(dir, Direction.UP).generate(world, pos, rand);
        EntityWroughtnaut wroughtnaut = new EntityWroughtnaut(EntityHandler.WROUGHTNAUT, world);
        if (dir == Direction.NORTH) wroughtnaut.setPositionAndRotation(pos.getX()+9.5, pos.getY()+1, pos.getZ()+0.5, 90, 0);
        else if (dir == Direction.EAST) wroughtnaut.setPositionAndRotation(pos.getX()+0.5, pos.getY()+1, pos.getZ()+9.5, 180, 0);
        else if (dir == Direction.SOUTH) wroughtnaut.setPositionAndRotation(pos.getX()-8.5, pos.getY()+1, pos.getZ()+0.5, 270, 0);
        else wroughtnaut.setPositionAndRotation(pos.getX()-0.5, pos.getY()+1, pos.getZ()-9.5, 0, 0);
        if (!world.isRemote) world.addEntity(wroughtnaut);
        wroughtnaut.onInitialSpawn(world, world.getDifficultyForLocation(wroughtnaut.getPosition()), SpawnReason.STRUCTURE, null, null);

//        System.out.println("Wroughtnaut chamber at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
    }

    public static void tryWroughtChamber(World world, Random random, int x, int z) {
        if (!world.getWorldInfo().isMapFeaturesEnabled()) return;

        Biome biome = world.getBiome(new BlockPos(x, 50, z));
        if (!SpawnHandler.FERROUS_WROUGHTNAUT_BIOMES.contains(biome)) return;
        boolean flag = false;
        /*for (int dimensionAllowed : ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationData.dimensions) {
            if (dimensionAllowed == world.getDimension()) {
                flag = true;
                break;
            }
        }*/
        if (!flag) return;

        int xzCheckDistance = 10;
        if (random.nextFloat() > ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationData.generationChance) return;

        int heightMax = (int) ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationData.heightMax;
        int heightMin = (int) ConfigHandler.MOBS.FERROUS_WROUGHTNAUT.generationData.heightMin;
        if (heightMax == -1) heightMax = world.getHeight();
        if (heightMin == -1) heightMin = 0;
        for (int y = heightMax; y >= heightMin; y--) {
            if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isAir(world.getBlockState(new BlockPos(x, y, z)), world, new BlockPos(x, y, z))) {
                for (int y2 = 1; y2 <= 30; y2++) {
                    BlockPos p0 = new BlockPos(x, y - y2, z);
                    if (world.getBlockState(p0).isNormalCube(world, p0)) {
                        int y4 = 0;
                        int y5 = 0;
                        for (int x2 = 0; x2 <= xzCheckDistance; x2++) {
                            BlockPos p1 = new BlockPos(x - x2, y - y2 + y4 + 1, z);
                            if (world.getBlockState(p1).isNormalCube(world, p1)) {
                                Boolean wall = true;
                                for (int y3 = 1; y3 <= 4; y3++) {
                                    BlockPos p2 = new BlockPos(x - x2, y - y2 + y4 + 1 + y3, z);
                                    if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                        wall = false;
                                        y4 += y3;
                                        break;
                                    }
                                }
                                if (wall) {
                                    BlockPos p2 = new BlockPos(x - x2, y - y2 + y4, z);
                                    if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                        StructureWroughtnautRoom.generate(world, new BlockPos(x - x2, y - y2 + y4, z), random, Direction.SOUTH);
                                    }
                                    return;
                                }
                            }
                            p1 = new BlockPos(x + x2, y - y2 + y5 + 1, z);
                            if (world.getBlockState(p1).isNormalCube(world, p1)) {
                                Boolean wall = true;
                                for (int y3 = 1; y3 <= 4; y3++) {
                                    BlockPos p2 = new BlockPos(x + x2, y - y2 + y5 + 1 + y3, z);
                                    if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                        wall = false;
                                        y5 += y3;
                                        break;
                                    }
                                }
                                if (wall) {
                                    BlockPos p2 = new BlockPos(x + x2, y - y2 + y5, z);
                                    if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                        StructureWroughtnautRoom.generate(world, new BlockPos(x + x2, y - y2 + y5, z), random, Direction.NORTH);
                                    }
                                    return;
                                }
                            }
                        }
                        y4 = 0;
                        y5 = 0;
                        for (int z2 = 0; z2 <= xzCheckDistance; z2++) {
                            BlockPos p1 = new BlockPos(x, y - y2 + y4 + 1, z - z2);
                            if (world.getBlockState(p1).isOpaqueCube(world, p1)) {
                                Boolean wall = true;
                                for (int y3 = 1; y3 <= 4; y3++) {
                                    BlockPos p2 = new BlockPos(x, y - y2 + y4 + 1 + y3, z - z2);
                                    if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                        wall = false;
                                        y4 += y3;
                                        break;
                                    }
                                }
                                if (wall) {
                                    BlockPos p2 = new BlockPos(x, y - y2 + y4, z - z2);
                                    if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                        StructureWroughtnautRoom.generate(world, new BlockPos(x, y - y2 + y4, z - z2), random, Direction.WEST);
                                    }
                                    return;
                                }
                            }
                            p1 = new BlockPos(x, y - y2 + y5 + 1, z + z2);
                            if (world.getBlockState(p1).isNormalCube(world, p1)) {
                                Boolean wall = true;
                                for (int y3 = 1; y3 <= 4; y3++) {
                                    BlockPos p2 = new BlockPos(x, y - y2 + y5 + 1 + y3, z + z2);
                                    if (!world.getBlockState(p2).isNormalCube(world, p2)) {
                                        wall = false;
                                        y5 += y3;
                                        break;
                                    }
                                }
                                if (wall) {
                                    BlockPos p2 = new BlockPos(x, y - y2 + y5, z + z2);
                                    if (world.getBlockState(p2).isNormalCube(world, p2)) {
                                        StructureWroughtnautRoom.generate(world, new BlockPos(x, y - y2 + y5, z + z2), random, Direction.EAST);
                                    }
                                    return;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }
}
