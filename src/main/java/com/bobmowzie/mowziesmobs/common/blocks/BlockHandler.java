package com.bobmowzie.mowziesmobs.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public enum BlockHandler {
    INSTANCE;

    public Block blockPaintedAcacia;
    public Block blockPaintedAcaciaSlab;

    public void onInit() {
        blockPaintedAcacia = new BlockPaintedAcacia();
        blockPaintedAcaciaSlab = new BlockPaintedAcaciaSlab();

        GameRegistry.registerBlock(blockPaintedAcacia, "paintedAcacia");
        GameRegistry.registerBlock(blockPaintedAcaciaSlab, "paintedAcaciaSlab");
    }
}
