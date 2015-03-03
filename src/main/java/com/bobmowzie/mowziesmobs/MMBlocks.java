package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.block.BlockBabyFoliaath;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.block.Block;

public class MMBlocks implements IContentProvider
{
    public static Block blockBabyFoliaath;

    public void init()
    {
        blockBabyFoliaath = new BlockBabyFoliaath();
        GameRegistry.registerBlock(blockBabyFoliaath, "blockBabyFoliaath");
    }
}
