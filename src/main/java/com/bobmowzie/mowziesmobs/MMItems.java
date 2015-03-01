package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.item.ItemFoliaathSeed;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.item.Item;

public class MMItems implements IContentProvider
{
    public static Item itemFoliaathSeed;

    public void init()
    {
        registerItems();
    }

    public void registerItems()
    {
        itemFoliaathSeed = new ItemFoliaathSeed();
        GameRegistry.registerItem(itemFoliaathSeed, "foliaathseed");
    }
}
