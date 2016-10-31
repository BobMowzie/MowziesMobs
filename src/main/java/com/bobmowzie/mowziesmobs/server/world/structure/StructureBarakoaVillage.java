package com.bobmowzie.mowziesmobs.server.world.structure;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockPaintedAcacia;
import net.ilexiconn.llibrary.server.structure.StructureBuilder;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Josh on 10/9/2016.
 */
public class StructureBarakoaVillage {

    public static void generateHouse(World world, Random rand, BlockPos pos, EnumFacing dir) {
        StructureBuilder structure = new StructureBuilder().startComponent()
                .fillCube(-1, 3, -1, 3, 1, 3, Blocks.HAY_BLOCK)
                .fillCube(-2, 3, -3, 1, 1, 7, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(2, 3, -3, 1, 1, 7, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(-3, 3, -2, 7, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-3, 3, 2, 7, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-2, 6, -3, 1, 1, 7, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(2, 6, -3, 1, 1, 7, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(-3, 6, -2, 7, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-3, 6, 2, 7, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .cube(-2, 4, -2, 5, 2, 5, BlockHandler.INSTANCE.paintedAcacia)
                .fillCube(-1, 4, -1, 3, 3, 3, Blocks.AIR)
                .fillCube(-2, 2, -2, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-2, 2, 2, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(2, 2, -2, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(2, 2, 2, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))

                .setBlock(-2, 4, 0, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(-2, 5, 0, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(2, 4, 0, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(2, 5, 0, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(0, 4, -2, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(0, 5, -2, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(0, 4, 2, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(0, 5, 2, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .fillCube(-2, 2, -1, 5, 1, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .fillCube(-1, 2, -2, 3, 1, 5, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(-3, 2, -2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(-2, 2, -3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(3, 2, 2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(2, 2, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(-3, 2, 2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(-2, 2, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(3, 2, -2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(2, 2, -3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .fillCube(2, 4, 3, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(3, 4, 2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(2, 4, -3, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(3, 4, -2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(-2, 4, 3, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(-3, 4, 2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(-2, 4, -3, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(-3, 4, -2, 1, 2, 1, Blocks.OAK_FENCE)

                .fillCube(-2, 7, -2, 5, 1, 5, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(-1, 7, -1, 3, 1, 3, Blocks.HAY_BLOCK)
                .fillCube(-2, 8, -2, 5, 1, 4, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(1, 9, -1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(0, 9, 0, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(1, 9, 0, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(1, 9, 0, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-2, 8, -2, Blocks.AIR)
                .setBlock(-1, 8, -2, Blocks.AIR)
                .fillCube(3, 7, -2, 1, 1, 5, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(3, 8, 0, 1, 1, 2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(3, 5, 1, 1, 2, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(3, 6, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(2, 7, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(1, 6, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-1, 7, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-2, 7, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(2, 8, 2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-3, 6, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(-3, 7, -2, 1, 1, 5, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-3, 6, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-3, 6, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-2, 7, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(-1, 6, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(0, 6, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(0, 7, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(1, 7, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(2, 7, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(3, 6, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(3, 5, -3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
        structure.endComponent();
        int dirRand = rand.nextInt(3);
        EnumFacing roofDir = dirRand == 0 ? EnumFacing.NORTH : (dirRand == 1 ? EnumFacing.EAST : (dirRand == 2 ? EnumFacing.SOUTH : EnumFacing.WEST));
        structure.rotate(roofDir, EnumFacing.UP).generate(world, pos, rand);

        //fences
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(2, -i + 1, 2);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(-2, -i + 1, -2);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(-2, -i + 1, 2);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(2, -i + 1, -2);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }

        //stairs
        if (dir == EnumFacing.SOUTH) {
            world.setBlockState(pos.add(0, 2, 2), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
            world.setBlockState(pos.add(0, 3, 2), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            world.setBlockState(pos.add(0, 4, 2), Blocks.AIR.getDefaultState());
            world.setBlockState(pos.add(0, 5, 2), Blocks.AIR.getDefaultState());
            for (int i = 1; i < 20; i++) {
                BlockPos stairPos = new BlockPos(pos.getX(), pos.getY() + 3 - i, pos.getZ() + 2 + i);
                if (!world.getBlockState(stairPos).isFullBlock()) {
                    world.setBlockState(stairPos, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
                    world.setBlockState(stairPos.down(), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
                } else {
                    break;
                }
                int sideHouseDir = rand.nextInt(6) + 1;
                if (sideHouseDir <= 2) {
                    EnumFacing sideHouseFacing = (sideHouseDir == 1) ? EnumFacing.EAST : EnumFacing.WEST;
                    generateSideHouse(world, rand, pos, sideHouseFacing);
                }
            }
        }
        else if (dir == EnumFacing.EAST) {
            world.setBlockState(pos.add(2, 2, 0), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
            world.setBlockState(pos.add(2, 3, 0), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            world.setBlockState(pos.add(2, 4, 0), Blocks.AIR.getDefaultState());
            world.setBlockState(pos.add(2, 5, 0), Blocks.AIR.getDefaultState());
            for (int i = 1; i < 20; i++) {
                BlockPos stairPos = new BlockPos(pos.getX() + 2 + i, pos.getY() + 3 - i, pos.getZ());
                if (!world.getBlockState(stairPos).isFullBlock()) {
                    world.setBlockState(stairPos, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
                    world.setBlockState(stairPos.down(), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
                } else {
                    break;
                }
                int sideHouseDir = rand.nextInt(6) + 1;
                if (sideHouseDir <= 2) {
                    EnumFacing sideHouseFacing = (sideHouseDir == 1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    generateSideHouse(world, rand, pos, sideHouseFacing);
                }
            }
        }
        else if (dir == EnumFacing.NORTH) {
            world.setBlockState(pos.add(0, 2, -2), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
            world.setBlockState(pos.add(0, 3, -2), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            world.setBlockState(pos.add(0, 4, -2), Blocks.AIR.getDefaultState());
            world.setBlockState(pos.add(0, 5, -2), Blocks.AIR.getDefaultState());
            for (int i = 1; i < 20; i++) {
                BlockPos stairPos = new BlockPos(pos.getX(), pos.getY() + 3 - i, pos.getZ() - 2 - i);
                if (!world.getBlockState(stairPos).isFullBlock()) {
                    world.setBlockState(stairPos, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
                    world.setBlockState(stairPos.down(), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
                } else {
                    break;
                }
                int sideHouseDir = rand.nextInt(6) + 1;
                if (sideHouseDir <= 2) {
                    EnumFacing sideHouseFacing = (sideHouseDir == 1) ? EnumFacing.EAST : EnumFacing.WEST;
                    generateSideHouse(world, rand, pos, sideHouseFacing);
                }
            }
        }
        else {
            world.setBlockState(pos.add(-2, 2, 0), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
            world.setBlockState(pos.add(-2, 3, 0), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
            world.setBlockState(pos.add(-2, 4, 0), Blocks.AIR.getDefaultState());
            world.setBlockState(pos.add(-2, 5, 0), Blocks.AIR.getDefaultState());
            for (int i = 1; i < 20; i++) {
                BlockPos stairPos = new BlockPos(pos.getX() - 2 - i, pos.getY() + 3 - i, pos.getZ());
                if (!world.getBlockState(stairPos).isFullBlock()) {
                    world.setBlockState(stairPos, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
                    world.setBlockState(stairPos.down(), Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
                } else {
                    break;
                }
                int sideHouseDir = rand.nextInt(6) + 1;
                if (sideHouseDir <= 2) {
                    EnumFacing sideHouseFacing = (sideHouseDir == 1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    generateSideHouse(world, rand, pos, sideHouseFacing);
                }
            }
        }

        //Interior
        int tableCorner = rand.nextInt(6);
        int tableContent = rand.nextInt(4);
        if (tableCorner == 0) {
            world.setBlockState(pos.add(-1, 4, 1), Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
            if (tableContent <= 1) {
                world.setBlockState(pos.add(-1, 5, 1), Blocks.TORCH.getDefaultState());
            }
            if (tableContent == 2) {
                world.setBlockState(pos.add(-1, 5, 1), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                ((TileEntitySkull)world.getTileEntity(pos.add(-1, 5, 1))).setSkullRotation(2);
            }
        }
        if (tableCorner == 1) {
            world.setBlockState(pos.add(1, 4, 1), Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
            if (tableContent <= 1) {
                world.setBlockState(pos.add(1, 5, 1), Blocks.TORCH.getDefaultState());
            }
            if (tableContent == 2) {
                world.setBlockState(pos.add(1, 5, 1), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                ((TileEntitySkull)world.getTileEntity(pos.add(1, 5, 1))).setSkullRotation(14);
            }
        }
        if (tableCorner == 2) {
            world.setBlockState(pos.add(-1, 4, -1), Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
            if (tableContent <= 1) {
                world.setBlockState(pos.add(-1, 5, -1), Blocks.TORCH.getDefaultState());
            }
            if (tableContent == 2) {
                world.setBlockState(pos.add(-1, 5, -1), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                ((TileEntitySkull)world.getTileEntity(pos.add(-1, 5, -1))).setSkullRotation(6);
            }
        }
        if (tableCorner == 3) {
            world.setBlockState(pos.add(1, 4, -1), Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
            if (tableContent <= 1) {
                world.setBlockState(pos.add(1, 5, -1), Blocks.TORCH.getDefaultState());
            }
            if (tableContent == 2) {
                world.setBlockState(pos.add(1, 5, -1), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                ((TileEntitySkull)world.getTileEntity(pos.add(1, 5, -1))).setSkullRotation(10);
            }
        }

        int bedCorner = rand.nextInt(6);
        int bedDirection = rand.nextInt(2);
        if (bedCorner == tableCorner) {
            bedCorner = 6;
        }
        if (bedCorner == 0) {
            world.setBlockState(pos.add(-1, 4, 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            if (bedDirection == 0) {
                world.setBlockState(pos.add(0, 4, 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            } else {
                world.setBlockState(pos.add(-1, 4, 0), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            }
        }
        if (bedCorner == 1) {
            world.setBlockState(pos.add(1, 4, 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            if (bedDirection == 0) {
                world.setBlockState(pos.add(0, 4, 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            } else {
                world.setBlockState(pos.add(1, 4, 0), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            }
        }
        if (bedCorner == 2) {
            world.setBlockState(pos.add(1, 4, -1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            if (bedDirection == 0) {
                world.setBlockState(pos.add(0, 4, -1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            } else {
                world.setBlockState(pos.add(1, 4, 0), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            }
        }
        if (bedCorner == 3) {
            world.setBlockState(pos.add(-1, 4, -1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            if (bedDirection == 0) {
                world.setBlockState(pos.add(0, 4, -1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            } else {
                world.setBlockState(pos.add(-1, 4, 0), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.YELLOW));
            }
        }
    }


    public static void generateSideHouse(World world, Random rand, BlockPos pos, EnumFacing dir) {
        StructureBuilder structure = new StructureBuilder().startComponent()
                .setBlock(7, 3, 0, Blocks.HAY_BLOCK)
                .setBlock(7, 6, 0, Blocks.HAY_BLOCK)
                .fillCube(6, 4, 0, 2, 2, 1, Blocks.AIR)
                .fillCube(8, 3, -2, 1, 1, 5, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(6, 3, -2, 1, 1, 5, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(5, 3, -1, 5, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(5, 3, 1, 5, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(8, 6, -2, 1, 1, 5, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(6, 6, -2, 1, 1, 5, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(5, 6, -1, 5, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(5, 6, 1, 5, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(8, 2, -1, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(8, 2, 1, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(6, 2, -1, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(6, 2, 1, 1, 5, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))

                .setBlock(8, 4, 0, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(8, 5, 0, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(7, 4, 1, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(7, 5, 1, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(7, 4, -1, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(7, 5, -1, BlockHandler.INSTANCE.paintedAcaciaSlab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))

                .fillCube(6, 2, 0, 3, 1, 1, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .fillCube(7, 2, -1, 1, 1, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(9, 2, -1, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(9, 2, 1, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(8, 2, -2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(8, 2, 2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(6, 2, -2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(6, 2, 2, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(5, 2, 1, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(5, 2, -1, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .fillCube(6, 4, -2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(6, 4, 2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(5, 4, -1, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(5, 4, 1, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(8, 4, 2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(8, 4, -2, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(9, 4, 1, 1, 2, 1, Blocks.OAK_FENCE)
                .fillCube(9, 4, -1, 1, 2, 1, Blocks.OAK_FENCE)

                .fillCube(5, 7, -1, 5, 1, 3, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(6, 7, -2, 3, 1, 5, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(5, 6, -2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(5, 6, 2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(9, 6, -2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(9, 6, 2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(6, 8, 0, 2, 1, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(7, 8, 1, 2, 1, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(6, 8, -1, 3, 1, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(5, 4, -2, 1, 2, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .fillCube(9, 5, 0, 1, 2, 1, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(7, 6, 2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))
                .setBlock(9, 5, 2, Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE))

                .setBlock(5, 3, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(4, 3, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM))
                .setBlock(3, 3, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
                .setBlock(2, 4, 0, Blocks.AIR)
                .setBlock(2, 5, 0, Blocks.AIR)
                ;
        structure.endComponent();
        structure.rotate(dir, EnumFacing.UP).generate(world, pos, rand);

        if (dir == EnumFacing.NORTH) pos = pos.add(0, 1, -9);
        else if (dir == EnumFacing.SOUTH) pos = pos.add(0, 1, 5);
        else if (dir == EnumFacing.EAST) pos = pos.add(7, 1, -2);
        else pos = pos.add(-7, 1, -2);

        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(1, -i, 1);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(-1, -i, 1);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(1, -i, 3);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            BlockPos fencePos = pos.add(-1, -i, 3);
            if (!world.getBlockState(fencePos).isFullBlock()) {
                world.setBlockState(fencePos, Blocks.OAK_FENCE.getDefaultState());
            } else {
                break;
            }
        }
    }

    public static void generateSkull(World world, Random rand, BlockPos pos) {
        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), Blocks.OAK_FENCE.getDefaultState());
        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ()), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.SOUTH));
    }

    public static void generateTorch(World world, BlockPos pos) {
        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), Blocks.OAK_FENCE.getDefaultState());
        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ()), Blocks.TORCH.getDefaultState());
    }
}
