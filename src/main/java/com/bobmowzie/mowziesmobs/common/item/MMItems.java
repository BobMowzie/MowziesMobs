package com.bobmowzie.mowziesmobs.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.item.Item;

public class MMItems implements IContentHandler
{
    public static Item itemFoliaathSeed;

    public void init()
    {
        itemFoliaathSeed = new ItemFoliaathSeed();
    }

    public void gameRegistry() throws Exception
    {
        GameRegistry.registerItem(itemFoliaathSeed, "foliaathseed");
    }
}
