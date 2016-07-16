package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum BlockHandler {
    INSTANCE;

    public Block paintedAcacia;
    public BlockSlab paintedAcaciaSlab;
    public BlockSlab paintedAcaciaDoubleSlab;

    public void onInit() {
        paintedAcacia = new BlockPaintedAcacia();
        paintedAcaciaSlab = new BlockPaintedAcaciaSlab.Half();
        paintedAcaciaDoubleSlab = new BlockPaintedAcaciaSlab.Double();

        // using just register won't register ItemBlocks for us, so it would have to be done manually :/
        GameRegistry.registerBlock(paintedAcacia);
        GameRegistry.register(paintedAcaciaSlab);
        GameRegistry.register(
            new ItemSlab(paintedAcaciaSlab, paintedAcaciaSlab, paintedAcaciaDoubleSlab)
            .setUnlocalizedName("paintedAcaciaSlab")
            .setRegistryName(paintedAcaciaSlab.getRegistryName())
        );
        GameRegistry.register(paintedAcaciaDoubleSlab);
    }
}
