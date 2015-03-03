package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llib.content.IContentProvider;

public class MMTileEntities implements IContentProvider
{
    public void init()
    {
        GameRegistry.registerTileEntity(TileBabyFoliaath.class, "babyfoliaath");
    }
}
