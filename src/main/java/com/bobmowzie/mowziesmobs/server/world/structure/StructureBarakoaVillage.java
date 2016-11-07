package com.bobmowzie.mowziesmobs.server.world.structure;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockPaintedAcacia;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.ilexiconn.llibrary.server.structure.StructureBuilder;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import org.lwjgl.Sys;

import java.util.List;
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

    public static void generateThrone(World world, Random rand, BlockPos pos, EnumFacing dir) {
        StructureBuilder structure = new StructureBuilder().startComponent()
                .fillCube(0, 0, -3, 1, 1, 7, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(-12, 0, -3, 1, 1, 7, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(-11, 0, -3, 11, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-11, 0, 3, 11, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-6, 0, -2, 6, 1, 5, Blocks.HAY_BLOCK.getDefaultState().withProperty(BlockHay.AXIS, EnumFacing.Axis.Z))
                .fillCube(-6, 0, 0, 6, 1, 1, Blocks.HAY_BLOCK)
                .fillCube(-6, 0, -1, 6, 1, 1, Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.RED))
                .fillCube(-6, 0, 1, 6, 1, 1, Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.RED))
                .fillCube(-11, 0, -2, 5, 1, 5, Blocks.HAY_BLOCK)
                .setBlock(-12, 0, -3, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-12, 0, 3, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA))

                .fillCube(-5, 1, -3, 5, 1, 1, Blocks.OAK_FENCE)
                .fillCube(-5, 1, 3, 5, 1, 1, Blocks.OAK_FENCE)
                .fillCube(-5, 2, -3, 5, 1, 1, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP))
                .fillCube(-5, 2, 3, 5, 1, 1, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP))
                .fillCube(0, 0, -3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-2, 0, -3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-4, 0, -3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-6, 0, -3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(0, 0, 3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-2, 0, 3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-4, 0, 3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-6, 0, 3, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-5, 3, -3, 5, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-5, 3, 3, 5, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .setBlock(-6, 3, 3, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST))
                .setBlock(0, 3, 3, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-6, 3, -3, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST))
                .setBlock(0, 3, -3, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-1, 4, -3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-1, 4, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-5, 4, -3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-5, 4, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
                .fillCube(-4, 4, -3, 3, 1, 1, BlockHandler.INSTANCE.paintedAcaciaSlab)
                .fillCube(-4, 4, 3, 3, 1, 1, BlockHandler.INSTANCE.paintedAcaciaSlab)

                .cube(-11, 2, -2, 5, 1, 5, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))
                .cube(-11, 1, -2, 5, 1, 5, Blocks.OAK_FENCE)
                .fillCube(-5, 1, -1, 1, 1, 3, BlockHandler.INSTANCE.paintedAcaciaSlab)
                .fillCube(-6, 1, -1, 1, 1, 3, BlockHandler.INSTANCE.paintedAcacia)
                .fillCube(-7, 2, -1, 1, 1, 3, BlockHandler.INSTANCE.paintedAcaciaSlab)
                .fillCube(-10, 2, -1, 3, 1, 3, BlockHandler.INSTANCE.paintedAcacia)
                .setBlock(-5, 1, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-6, 1, 0, Blocks.PLANKS.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-7, 2, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))
                .setBlock(-8, 2, 0, Blocks.PLANKS.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))

                .fillCube(-7, 0, -2, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-7, 0, 2, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-11, 0, -2, 1, 6, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-11, 0, 2, 1, 6, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-7, 0, -2, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-7, 0, 2, 1, 3, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-11, 0, -2, 1, 6, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-11, 0, 2, 1, 6, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-10, 3, -2, 3, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-10, 3, 2, 3, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-11, 3, -1, 1, 1, 3, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(-10, 4, -2, 2, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-10, 4, 2, 2, 1, 1, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z))
                .fillCube(-11, 4, -1, 1, 2, 3, BlockHandler.INSTANCE.paintedAcacia)
                .fillCube(-11, 6, -1, 1, 1, 3, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X))
                .fillCube(-11, 7, -1, 1, 1, 3, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .setBlock(-11, 8, 0, Blocks.GOLD_BLOCK)
                .setBlock(-11, 9, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))
                .fillCube(-10, 3, -1, 2, 1, 3, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y))
                .fillCube(-10, 4, -1, 2, 1, 3, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))

                .setBlock(-7, 3, -2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-7, 3, 2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-8, 4, -2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-8, 4, 2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-10, 5, -2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-10, 5, 2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST))
                .setBlock(-11, 6, -2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH))
                .setBlock(-11, 6, 2, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH))
                .setBlock(-11, 8, -1, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH))
                .setBlock(-11, 8, 1, Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH))

                .setBlock(-6, 2, -2, Blocks.OAK_FENCE)
                .setBlock(-6, 2, 2, Blocks.OAK_FENCE)
                .setBlock(-9, 5, -2, Blocks.OAK_FENCE)
                .setBlock(-9, 5, 2, Blocks.OAK_FENCE)
                .setBlock(-6, 3, -2, Blocks.TORCH)
                .setBlock(-6, 3, 2, Blocks.TORCH)
                .setBlock(-9, 6, -2, Blocks.TORCH)
                .setBlock(-9, 6, 2, Blocks.TORCH)
                ;
        structure.endComponent();
        structure.rotate(dir, EnumFacing.UP).generate(world, pos, rand);

        BlockPos[] skullPosL;
        BlockPos[] skullPosR;
        BlockPos[] polePos;
        int skullRot;
        EntityBarako barako;

        if (dir == EnumFacing.NORTH) {
            skullPosL = new BlockPos[]{pos.add(-3, 2, 1), pos.add(-3, 2, 3), pos.add(-3, 2, 5)};
            skullPosR = new BlockPos[]{pos.add(3, 2, 1), pos.add(3, 2, 3), pos.add(3, 2, 5)};
            polePos = new BlockPos[]{pos.add(-3, -1, 0), pos.add(-3, -1, 2), pos.add(-3, -1, 4), pos.add(-3, -1, 6), pos.add(3, -1, 0), pos.add(3, -1, 2), pos.add(3, -1, 4), pos.add(3, -1, 6), pos.add(3, -1, 8), pos.add(-3, -1, 8), pos.add(3, -1, 10), pos.add(-3, -1, 10), pos.add(3, -1, 12), pos.add(-3, -1, 12), pos.add(-1, -1, 12), pos.add(1, -1, 12)};
            skullRot = 4;
            barako = new EntityBarako(world, 3);
            barako.setPosition(pos.getX() + 0.5, pos.getY() + 4.5, pos.getZ() + 9.5);
        }
        else if (dir == EnumFacing.SOUTH) {
            skullPosL = new BlockPos[]{pos.add(-3, 2, -1), pos.add(-3, 2, -3), pos.add(-3, 2, -5)};
            skullPosR = new BlockPos[]{pos.add(3, 2, -1), pos.add(3, 2, -3), pos.add(3, 2, -5)};
            polePos = new BlockPos[]{pos.add(-3, -1, 0), pos.add(-3, -1, -2), pos.add(-3, -1, -4), pos.add(-3, -1, -6), pos.add(3, -1, 0), pos.add(3, -1, -2), pos.add(3, -1, -4), pos.add(3, -1, -6), pos.add(3, -1, -8), pos.add(-3, -1, -8), pos.add(3, -1, -10), pos.add(-3, -1, -10), pos.add(3, -1, -12), pos.add(-3, -1, -12), pos.add(-1, -1, -12), pos.add(1, -1, -12)};
            skullRot = 4;
            barako = new EntityBarako(world, 1);
            barako.setPosition(pos.getX() + 0.5, pos.getY() + 4.5, pos.getZ() - 8.5);
        }
        else if (dir == EnumFacing.EAST) {
            skullPosL = new BlockPos[]{pos.add(-1, 2, -3), pos.add(-3, 2, -3), pos.add(-5, 2, -3)};
            skullPosR = new BlockPos[]{pos.add(-1, 2, 3), pos.add(-3, 2, 3), pos.add(-5, 2, 3)};
            polePos = new BlockPos[]{pos.add(0, -1, -3), pos.add(-2, -1, -3), pos.add(-4, -1, -3), pos.add(-6, -1, -3), pos.add(0, -1, 3), pos.add(-2, -1, 3), pos.add(-4, -1, 3), pos.add(-6, -1, 3), pos.add(-8, -1, 3), pos.add(-8, -1, -3), pos.add(-10, -1, 3), pos.add(-10, -1, -3), pos.add(-12, -1, 3), pos.add(-12, -1, -3), pos.add(-12, -1, -1), pos.add(-12, -1, 1)};
            skullRot = 8;
            barako = new EntityBarako(world, 0);
            barako.setPosition(pos.getX() - 8.5, pos.getY() + 4.5, pos.getZ() + 0.5);
        }
        else {
            skullPosL = new BlockPos[]{pos.add(1, 2, -3), pos.add(3, 2, -3), pos.add(5, 2, -3)};
            skullPosR = new BlockPos[]{pos.add(1, 2, 3), pos.add(3, 2, 3), pos.add(5, 2, 3)};
            polePos = new BlockPos[]{pos.add(0, -1, -3), pos.add(2, -1, -3), pos.add(4, -1, -3), pos.add(6, -1, -3), pos.add(0, -1, 3), pos.add(2, -1, 3), pos.add(4, -1, 3), pos.add(6, -1, 3), pos.add(8, -1, 3), pos.add(8, -1, -3), pos.add(10, -1, 3), pos.add(10, -1, -3), pos.add(12, -1, 3), pos.add(12, -1, -3), pos.add(12, -1, -1), pos.add(12, -1, 1)};
            skullRot = 8;
            barako = new EntityBarako(world, 2);
            barako.setPosition(pos.getX() + 9.5, pos.getY() + 4.5, pos.getZ() + 0.5);
        }

        for (BlockPos skull : skullPosL) {
            ((TileEntitySkull)world.getTileEntity(skull)).setSkullRotation(skullRot);
        }
        for (BlockPos skull : skullPosR) {
            ((TileEntitySkull)world.getTileEntity(skull)).setSkullRotation(skullRot - 8);
        }
        for (BlockPos pole : polePos) {
            for (int i = 0; i < 20; i++) {
                BlockPos dPos = pole.add(0, -i, 0);
                if (!world.getBlockState(dPos).isFullBlock()) {
                    world.setBlockState(dPos, Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA));
                } else {
                    break;
                }
            }
        }
        world.spawnEntityInWorld(barako);
    }

    public static void generateFirepit(World world, Random rand, BlockPos pos) {
        StructureBuilder structure = new StructureBuilder().startComponent()
                .setBlock(0, 0, 0, Blocks.NETHERRACK)
                .setBlock(1, 1, 0, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE))
                .setBlock(-1, 1, 0, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE))
                .setBlock(0, 1, 1, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE))
                .setBlock(0, 1, -1, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE))
                .setBlock(0, 1, 0, Blocks.FIRE)
                ;
        BlockPos[] poses = new BlockPos[]{pos.add(1, 4, -4), pos.add(1, 4, 4), pos.add(-1, 4, -4), pos.add(-1, 4, 4), pos.add(4, 4, 1), pos.add(-4, 4, 1), pos.add(4, 4, -1), pos.add(-4, 4, -1), pos.add(-3, 4, 3), pos.add(-3, 4, -3), pos.add(3, 4, -3), pos.add(3, 4, 3)};
        for (int i = 0; i < poses.length; i++) {
            for (int dy = 0; dy <= 8; dy++) {
                if (world.getBlockState(poses[i].add(0, -dy, 0)).isFullBlock()) {
                    world.setBlockState(poses[i].add(0, -dy + 1, 0), Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA));
                    break;
                }
            }
        }

        structure.endComponent();
        structure.generate(world, pos, rand);
    }

    public static void generateSkulls(World world, Random rand, BlockPos pos, EnumFacing dir) {
        StructureBuilder structure = new StructureBuilder().startComponent()
                .fillCube(-2, 0, -1, 5, 1, 3, Blocks.HAY_BLOCK)
                .fillCube(-1, 0, -2, 3, 1, 5, Blocks.HAY_BLOCK)
                ;
        structure.endComponent();
        structure.generate(world, pos, rand);

        BlockPos[] poslist1;
        if (dir==EnumFacing.NORTH) {
            poslist1 = new BlockPos[] {pos.add(-2, 0, -1), pos.add(-2, 0, 0), pos.add(-2, 0, 1), pos.add(-1, 0, 2),  pos.add(0, 0, 2),  pos.add(1, 0, 2), pos.add(2, 0, 1), pos.add(2, 0, 0), pos.add(2, 0, -1)};
        }
        else if (dir==EnumFacing.SOUTH) {
            poslist1 = new BlockPos[] {pos.add(-2, 0, -1), pos.add(-2, 0, 0), pos.add(-2, 0, 1), pos.add(-1, 0, -2),  pos.add(0, 0, -2),  pos.add(1, 0, -2), pos.add(2, 0, 1), pos.add(2, 0, 0), pos.add(2, 0, -1)};
        }
        else if (dir==EnumFacing.EAST) {
            poslist1 = new BlockPos[] {pos.add(-1, 0, -2), pos.add(0, 0, -2), pos.add(1, 0, -2), pos.add(2, 0, -1),  pos.add(2, 0, 0),  pos.add(2, 0, 1), pos.add(1, 0, 2), pos.add(0, 0, 2), pos.add(-1, 0, 2)};
        }
        else {
            poslist1 = new BlockPos[] {pos.add(-1, 0, -2), pos.add(0, 0, -2), pos.add(1, 0, -2), pos.add(-2, 0, -1),  pos.add(-2, 0, 0),  pos.add(-2, 0, 1), pos.add(1, 0, 2), pos.add(0, 0, 2), pos.add(-1, 0, 2)};
        }
        BlockPos[] poslist2 = {pos.add(-1, 0, -1), pos.add(-1, 0, 0), pos.add(-1, 0, 1), pos.add(0, 0, -1), pos.add(0, 0, 1), pos.add(1, 0, -1), pos.add(1, 0, 0), pos.add(1, 0, 1)};

        boolean lastOnFence = false;
        for (int i = 0; i < poslist1.length; i++) {
            if (!lastOnFence && rand.nextBoolean()) {
                generateSkull(world, rand, poslist1[i]);
                lastOnFence = true;
            }
            else {
                world.setBlockState(poslist1[i].add(0,1,0), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                ((TileEntitySkull)world.getTileEntity(poslist1[i].add(0,1,0))).setSkullRotation(rand.nextInt(21) - 10);
                lastOnFence = false;
            }
        }
        for (int i = 0; i < poslist2.length; i++) {
            if (rand.nextInt(5) == 0) {
                world.setBlockState(poslist2[i].add(0, 1, 0), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
                ((TileEntitySkull) world.getTileEntity(poslist2[i].add(0, 1, 0))).setSkullRotation(rand.nextInt(21) - 10);
            }
        }
    }

    public static void generateSkull(World world, Random rand, BlockPos pos) {
        world.setBlockState(pos.add(0,1,0), Blocks.OAK_FENCE.getDefaultState());
        world.setBlockState(pos.add(0,2,0), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
        ((TileEntitySkull)world.getTileEntity(pos.add(0,2,0))).setSkullRotation(rand.nextInt(21) - 10);
    }

    public static void generateTorch(World world, BlockPos pos) {
        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), Blocks.OAK_FENCE.getDefaultState());
        world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 2, pos.getZ()), Blocks.TORCH.getDefaultState());
    }

    public static void generateVillage(World world, Random rand, int x, int z, int chance) {
        System.out.println("Beginning generation");
        if (chance <= 0) {
            return;
        }
        if (rand.nextInt(chance) == 0) {
            System.out.println("Passes chance test");
            BlockPos pos = new BlockPos(x, 0, z);
            int y = findGenHeight(world, pos);
            if (y == -1) return;
            System.out.println("Found height at " + y);
            pos = new BlockPos(pos.getX(), y, pos.getZ());
            generateFirepit(world, rand, pos);
            System.out.println("Generated firepit at " + pos.toString());
            int initDir = rand.nextInt(4);
            for (int i = 0; i < 4; i++) {
                EnumFacing throneFacing = EnumFacing.HORIZONTALS[(initDir + i) % 4];
                int throneX = x + 9 * (throneFacing == EnumFacing.WEST ? 1:0) - 9 * (throneFacing == EnumFacing.EAST ? 1:0);
                int throneZ = z + 9 * (throneFacing == EnumFacing.NORTH ? 1:0) - 9 * (throneFacing == EnumFacing.SOUTH ? 1:0);
                y = findGenHeight(world, new BlockPos(throneX, y, throneZ));
                if (y != -1) {
                    generateThrone(world, rand, new BlockPos(throneX, y - 1, throneZ), throneFacing);
                    break;
                }
            }
            System.out.println("Generating houses");
            for (int i = 0; i < 360; i++) {
                if (rand.nextInt(30) != 0) continue;
                System.out.println("Passes chance test");
                int distance = rand.nextInt(13) + 7;
                BlockPos housePos = new BlockPos(pos.getX() + distance * Math.sin(Math.toRadians(i)), 0, pos.getZ() + distance * Math.cos(Math.toRadians(i)));
                y = findGenHeight(world, housePos);
                System.out.println("Attempting at " + housePos.add(0, y, 0).toString());
                AxisAlignedBB box = new AxisAlignedBB(housePos.add(-3, y + 3, -3), housePos.add(3, y + 9, -3));
                if (world.checkBlockCollision(box)) generateHouse(world, rand, housePos.add(0, y + rand.nextInt(2), 0), EnumFacing.HORIZONTALS[rand.nextInt(4)]);
                else System.out.println("No space");
            }
        }
    }

    private static int findGenHeight(World world, BlockPos pos) {
        for (int y = 30; y > 0; y--) {
            if (!(world.getBlockState(pos.add(0, y, 0)).isFullBlock())) continue;
            if (world.getBlockState(pos.add(0, y, 0)) != Blocks.GRASS.getDefaultState()) break;
            return y;
        }
        System.out.println("Failed to find height");
        return -1;
    }
}
