package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.ItemMMSpawnEgg;

import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.item.Item;

public class MMItems implements IContentHandler
{
    public static Item itemFoliaathSeed;
    public static Item itemTestStructure;
    public static Item itemMobRemover;
    public static Item itemWroughtAxe;
    public static Item itemWroughtHelm;
    public static Item itemBarakoaMask1;
    public static Item itemBarakoaMask2;
    public static Item itemBarakoaMask3;
    public static Item itemBarakoaMask4;
    public static Item itemBarakoaMask5;
    public static Item itemDart;
    public static Item itemSpear;
    public static Item itemBlowgun;
    public static Item itemSpawnEgg;

    public void init()
    {
        itemFoliaathSeed = new ItemFoliaathSeed();
        itemTestStructure = new ItemTestStructure();
        itemMobRemover = new ItemMobRemover();
        itemWroughtAxe = new ItemWroughtAxe();
        itemWroughtHelm = new ItemWroughtHelm();
        itemBarakoaMask1 = new ItemBarakoaMask(1);
        itemBarakoaMask2 = new ItemBarakoaMask(2);
        itemBarakoaMask3 = new ItemBarakoaMask(3);
        itemBarakoaMask4 = new ItemBarakoaMask(4);
        itemBarakoaMask5 = new ItemBarakoaMask(5);
        itemDart = new ItemDart();
        itemSpear = new ItemSpear();
        itemBlowgun = new ItemBlowgun();
        itemSpawnEgg = new ItemMMSpawnEgg();
    }

    public void gameRegistry() throws Exception
    {
        GameRegistry.registerItem(itemFoliaathSeed, "foliaathseed");
        GameRegistry.registerItem(itemWroughtAxe, "wroughtaxe");
        GameRegistry.registerItem(itemMobRemover, "mobremover");
        GameRegistry.registerItem(itemWroughtHelm, "wroughthelm");
        GameRegistry.registerItem(itemBarakoaMask1, "barakoaMask1");
        GameRegistry.registerItem(itemBarakoaMask2, "barakoaMask2");
        GameRegistry.registerItem(itemBarakoaMask3, "barakoaMask3");
        GameRegistry.registerItem(itemBarakoaMask4, "barakoaMask4");
        GameRegistry.registerItem(itemBarakoaMask5, "barakoaMask5");
        GameRegistry.registerItem(itemDart, "dart");
        GameRegistry.registerItem(itemSpear, "spear");
        GameRegistry.registerItem(itemBlowgun, "blowgun");
        GameRegistry.registerItem(itemSpawnEgg, "spawnEgg");

        if (MowziesMobs.isDebugging())
        {
            GameRegistry.registerItem(itemTestStructure, "teststructure");
        }
    }
}
