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
    public static ItemBarakoaMask[] itemBarakoaMasks;
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
        BarakoaMask[] masks = BarakoaMask.values();
        itemBarakoaMasks = new ItemBarakoaMask[masks.length];
        for (int i = 0; i < masks.length; itemBarakoaMasks[i] = new ItemBarakoaMask(masks[i++]));
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
        for (int i = 0; i < itemBarakoaMasks.length; i++)
        {
            GameRegistry.registerItem(itemBarakoaMasks[i], itemBarakoaMasks[i].getType().getUnlocalizedName());
        }
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
