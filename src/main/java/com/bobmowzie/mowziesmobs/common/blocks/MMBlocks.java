package com.bobmowzie.mowziesmobs.common.blocks;

import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.block.Block;

/**
 * Created by jnad325 on 8/19/15.
 */
public class MMBlocks implements IContentHandler {
    public static Block blockPaintedAcacia;
    public static Block blockPaintedAcaciaSlab;

    public void init()
    {
        blockPaintedAcacia = new BlockPaintedAcacia();
        blockPaintedAcaciaSlab = new BlockPaintedAcaciaSlab();
    }

    public void gameRegistry() throws Exception
    {
//        GameRegistry.registerBlock(blockPaintedAcacia, "paintedAcacia");
//        GameRegistry.registerBlock(blockPaintedAcaciaSlab, "paintedAcaciaSlab");
    }
}
