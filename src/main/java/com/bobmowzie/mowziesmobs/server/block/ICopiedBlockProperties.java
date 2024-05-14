package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.world.level.block.Block;

public interface ICopiedBlockProperties {
    public Block getBaseBlock();

    public void setBaseBlock(Block block);
}
