package com.bobmowzie.mowziesmobs.common.gen.structure;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 8/29/15.
 */
public class StructureBase {
    public static void replaceBlocks(Block toReplace, Block replacement, int x, int y, int z, int length, int width, int height, World world) {
        for (int x_ = x; x_ <= x + length; x++) {
            for (int z_ = x; z_ <= z + width; z++) {
                for (int y_ = y; y_ <= y + height; y++) {
                    if (world.getBlock(x, y, z) == toReplace) world.setBlock(x, y, z, replacement);
                }
            }
        }
    }
}
