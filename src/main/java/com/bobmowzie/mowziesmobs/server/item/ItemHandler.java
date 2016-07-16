package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntityEggInfo;

public enum ItemHandler {
    INSTANCE;

    public Item foliaathSeed;
    public Item testStructure;
    public Item mobRemover;
    public Item wroughtAxe;
    public Item wroughtHelmet;
    public ItemBarakoaMask[] barakoa_masks;
    public Item dart;
    public Item spear;
    public Item blowgun;
    public Item spawnEgg;

    public void onInit() {
        foliaathSeed = new ItemFoliaathSeed();
        testStructure = new ItemTestStructure();
        mobRemover = new ItemMobRemover();
        wroughtAxe = new ItemWroughtAxe();
        wroughtHelmet = new ItemWroughtHelm();
        barakoa_masks = new ItemBarakoaMask[ItemBarakoaMask.BarakoaMaskType.values().length];
        for (int i = 0; i < ItemBarakoaMask.BarakoaMaskType.values().length; i++) {
            barakoa_masks[i] = new ItemBarakoaMask(ItemBarakoaMask.BarakoaMaskType.values()[i]);
        }
        dart = new ItemDart();
        spear = new ItemSpear();
        blowgun = new ItemBlowgun();
        spawnEgg = new ItemSpawnEgg();

        GameRegistry.register(spawnEgg);

        GameRegistry.register(foliaathSeed);
        GameRegistry.register(wroughtAxe);
        GameRegistry.register(wroughtHelmet);
        for (ItemBarakoaMask itemBarakoaMask : barakoa_masks) {
            GameRegistry.register(itemBarakoaMask);
        }
        GameRegistry.register(dart);
        GameRegistry.register(spear);
        GameRegistry.register(blowgun);

        GameRegistry.register(mobRemover);
        //GameRegistry.register(test_structure);
    }
}
