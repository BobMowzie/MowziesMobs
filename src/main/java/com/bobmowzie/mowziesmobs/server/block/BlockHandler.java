package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
