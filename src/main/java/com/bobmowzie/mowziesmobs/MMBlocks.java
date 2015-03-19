package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.block.BlockBabyFoliaath;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class MMBlocks
{
    public static Block blockBabyFoliaath;

    public static void init()
    {
        blockBabyFoliaath = new BlockBabyFoliaath();
        GameRegistry.registerBlock(blockBabyFoliaath, "blockBabyFoliaath");
    }
}
