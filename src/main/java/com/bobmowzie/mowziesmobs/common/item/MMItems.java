package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.item.Item;

public class MMItems implements IContentHandler
{
    public static Item itemFoliaathSeed;
    public static Item itemTestStructure;
    public static Item itemMobRemover;
    public static Item itemWroughtAxe;

    public void init()
    {
        itemFoliaathSeed = new ItemFoliaathSeed();
        itemTestStructure = new ItemTestStructure();
        itemMobRemover = new ItemMobRemover();
        itemWroughtAxe = new ItemWroughtAxe();
    }

    public void gameRegistry() throws Exception
    {
        GameRegistry.registerItem(itemFoliaathSeed, "foliaathseed");
        GameRegistry.registerItem(itemWroughtAxe, "wroughtaxe");
        if (MowziesMobs.isDebugging())
        {
            GameRegistry.registerItem(itemTestStructure, "teststructure");
            GameRegistry.registerItem(itemMobRemover, "mobremover");
        }
    }
}
