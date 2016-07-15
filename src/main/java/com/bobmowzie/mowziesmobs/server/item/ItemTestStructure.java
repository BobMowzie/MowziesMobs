package com.bobmowzie.mowziesmobs.server.item;

import java.util.Random;

import net.ilexiconn.llibrary.server.structure.StructureBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTestStructure extends Item {
    public ItemTestStructure() {
        this.setUnlocalizedName("testStructure");
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Random rand = new Random();
        if (!world.isRemote) {
            StructureBuilder structure = new StructureBuilder().startComponent()
                    .cube(0 - 9, 0, 0, 19, 7, 19, Blocks.stone)
                    .fillCube(1 - 9, 1, 1, 17, 5, 17, Blocks.air)
                    .fillCube(8 - 9, 1, 0, 3, 5, 1, Blocks.air)
                    .cube(7 - 9, 0, 0, 5, 7, 1, Blocks.cobblestone)
                    .cube(7 - 9, 6, 0, 5, 1, 16, Blocks.cobblestone)
                    .cube(3 - 9, 6, 3, 13, 1, 5, Blocks.cobblestone)
                    .cube(3 - 9, 6, 7, 13, 1, 5, Blocks.cobblestone)
                    .cube(3 - 9, 6, 11, 13, 1, 5, Blocks.cobblestone)
                    .cube(7 - 9, 0, 0, 5, 1, 16, Blocks.cobblestone)
                    .cube(3 - 9, 0, 3, 13, 1, 5, Blocks.cobblestone)
                    .cube(3 - 9, 0, 7, 13, 1, 5, Blocks.cobblestone)
                    .cube(3 - 9, 0, 11, 13, 1, 5, Blocks.cobblestone)
                    .fillCube(1 - 9, 1, 1, 6, 5, 2, Blocks.stone)
                    .fillCube(12 - 9, 1, 1, 6, 5, 2, Blocks.stone)
                    .fillCube(7 - 9, 1, 3, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(11 - 9, 1, 3, 1, 5, 1, Blocks.cobblestone)
                    .setBlock(8 - 9, 5, 0, BlockState.create(Blocks.stone_stairs, 5))
                    .setBlock(10 - 9, 5, 0, BlockState.create(Blocks.stone_stairs, 4))
                    .setBlock(7 - 9, 5, 2, BlockState.create(Blocks.stone_stairs, 6))
                    .setBlock(11 - 9, 5, 2, BlockState.create(Blocks.stone_stairs, 6))
                    .setBlock(7 - 9, 5, 1, BlockState.create(Blocks.stone_stairs, 7))
                    .setBlock(11 - 9, 5, 1, BlockState.create(Blocks.stone_stairs, 7))
                    .setBlock(8 - 9, 5, 3, BlockState.create(Blocks.stone_stairs, 5))
                    .setBlock(10 - 9, 5, 3, BlockState.create(Blocks.stone_stairs, 4))
                    .setBlock(7 - 9, 5, 4, BlockState.create(Blocks.stone_stairs, 7))
                    .setBlock(11 - 9, 5, 4, BlockState.create(Blocks.stone_stairs, 7))
                    .setBlock(6 - 9, 5, 3, BlockState.create(Blocks.stone_stairs, 4))
                    .setBlock(12 - 9, 5, 3, BlockState.create(Blocks.stone_stairs, 5))
                    .fillCube(3 - 9, 1, 3, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(3 - 9, 1, 7, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(3 - 9, 1, 11, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(3 - 9, 1, 15, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(7 - 9, 1, 15, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(11 - 9, 1, 15, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(15 - 9, 1, 15, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(15 - 9, 1, 3, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(15 - 9, 1, 7, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(15 - 9, 1, 11, 1, 5, 1, Blocks.cobblestone)
                    .fillCube(1 - 9, 1, 3, 2, 5, 15, Blocks.stone)
                    .fillCube(16 - 9, 1, 3, 2, 5, 15, Blocks.stone)
                    .fillCube(3 - 9, 1, 16, 13, 5, 2, Blocks.stone)
                    .fillCube(8 - 9, 0, 1, 3, 1, 2, Blocks.double_stone_slab)
                    .setBlock(4 - 9, 5, 3, BlockState.create(Blocks.stone_stairs, 5))
                    .setBlock(3 - 9, 5, 4, BlockState.create(Blocks.stone_stairs, 7))
                    .setBlock(14 - 9, 5, 3, BlockState.create(Blocks.stone_stairs, 4))
                    .setBlock(15 - 9, 5, 4, BlockState.create(Blocks.stone_stairs, 7))
                    .fillCube(5 - 9, 2, 1, 1, 1, 2, Blocks.air)
                    .setBlock(5 - 9, 2, 1, BlockState.create(Blocks.torch, 5))
                    .setBlock(5 - 9, 2, 2, Blocks.iron_bars)
                    .fillCube(13 - 9, 2, 1, 1, 1, 2, Blocks.air)
                    .setBlock(13 - 9, 2, 1, BlockState.create(Blocks.torch, 5))
                    .setBlock(13 - 9, 2, 2, Blocks.iron_bars);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    structure.fillCube(4 - 9 + i * 4, 0, 4 + j * 4, 3, 1, 3, Blocks.double_stone_slab);
                    if (i == 0) { //left
                        structure.setBlock(3 - 9, 5, 6 + j * 4, BlockState.create(Blocks.stone_stairs, 6));
                        structure.setBlock(4 - 9, 5, 7 + j * 4, BlockState.create(Blocks.stone_stairs, 5));
                        if (j != 2) {
                            structure.setBlock(3 - 9, 5, 8 + j * 4, BlockState.create(Blocks.stone_stairs, 7));
                        }
                        structure.fillCube(1 - 9, 2, 5 + 4 * j, 2, 1, 1, Blocks.air);
                        structure.setBlock(1 - 9, 2, 5 + 4 * j, BlockState.create(Blocks.torch, 5));
                        structure.setBlock(2 - 9, 2, 5 + 4 * j, Blocks.iron_bars);
                    } else if (i == 1) { //right
                        structure.fillCube(16 - 9, 2, 5 + 4 * j, 2, 1, 1, Blocks.air);
                        structure.setBlock(17 - 9, 2, 5 + 4 * j, BlockState.create(Blocks.torch, 5));
                        structure.setBlock(16 - 9, 2, 5 + 4 * j, Blocks.iron_bars);
                    } else { //back
                        structure.fillCube(5 - 9 + 4 * j, 2, 16, 1, 1, 2, Blocks.air);
                        structure.setBlock(5 - 9 + 4 * j, 2, 17, BlockState.create(Blocks.torch, 5));
                        structure.setBlock(5 - 9 + 4 * j, 2, 16, Blocks.iron_bars);
                    }
                }
            }
            structure.endComponent();
            structure.generate(world, x, y, z, rand);
            return true;
        }
        return false;
    }

    @Override
    public void registerIcons(IIconRegister register) {

    }
}