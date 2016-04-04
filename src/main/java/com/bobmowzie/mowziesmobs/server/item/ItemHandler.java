package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.ItemMMSpawnEgg;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public enum ItemHandler {
    INSTANCE;

    public Item itemFoliaathSeed;
    public Item itemTestStructure;
    public Item itemMobRemover;
    public Item itemWroughtAxe;
    public Item itemWroughtHelm;
    public ItemBarakoaMask[] itemBarakoaMasks;
    public Item itemDart;
    public Item itemSpear;
    public Item itemBlowgun;
    public Item itemSpawnEgg;

    public void onInit() {
        itemFoliaathSeed = new ItemFoliaathSeed();
        itemTestStructure = new ItemTestStructure();
        itemMobRemover = new ItemMobRemover();
        itemWroughtAxe = new ItemWroughtAxe();
        itemWroughtHelm = new ItemWroughtHelm();
        itemBarakoaMasks = new ItemBarakoaMask[ItemBarakoaMask.BarakoaMaskType.VALUES.length];
        for (int i = 0; i < ItemBarakoaMask.BarakoaMaskType.VALUES.length; i++) {
            itemBarakoaMasks[i] = new ItemBarakoaMask(ItemBarakoaMask.BarakoaMaskType.VALUES[i]);
        }
        itemDart = new ItemDart();
        itemSpear = new ItemSpear();
        itemBlowgun = new ItemBlowgun();
        itemSpawnEgg = new ItemMMSpawnEgg();

        GameRegistry.registerItem(itemSpawnEgg, "spawnEgg");

        GameRegistry.registerItem(itemFoliaathSeed, "foliaathseed");
        GameRegistry.registerItem(itemWroughtAxe, "wroughtaxe");
        GameRegistry.registerItem(itemWroughtHelm, "wroughthelm");
        for (ItemBarakoaMask itemBarakoaMask : itemBarakoaMasks) {
            GameRegistry.registerItem(itemBarakoaMask, itemBarakoaMask.getType().getUnlocalizedName());
        }
        GameRegistry.registerItem(itemDart, "dart");
        GameRegistry.registerItem(itemSpear, "spear");
        GameRegistry.registerItem(itemBlowgun, "blowgun");

        GameRegistry.registerItem(itemMobRemover, "mobremover");
        GameRegistry.registerItem(itemTestStructure, "teststructure");
    }
}
