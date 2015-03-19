package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import cpw.mods.fml.common.registry.GameRegistry;

public class MMTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileBabyFoliaath.class, "babyfoliaath");
    }
}
