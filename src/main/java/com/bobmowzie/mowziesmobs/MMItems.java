package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.item.ItemFoliaathSeed;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class MMItems
{
    public static Item itemFoliaathSeed;

    public static void init()
    {
        registerItems();
    }

    public static void registerItems()
    {
        itemFoliaathSeed = new ItemFoliaathSeed();
        GameRegistry.registerItem(itemFoliaathSeed, "foliaathseed");
    }
}
