package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.block.BlockTest;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.block.Block;

public class MMBlocks implements IContentProvider
{
    public static Block blockTest;

    public void init()
    {
        blockTest = new BlockTest();
        GameRegistry.registerBlock(blockTest, "blockTest");
    }
}
