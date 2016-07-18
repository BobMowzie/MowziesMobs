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
    public ItemBarakoaMask[] barakoaMasks;
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
        ItemBarakoaMask.Type[] types = ItemBarakoaMask.Type.values();
        barakoaMasks = new ItemBarakoaMask[types.length];
        for (int i = 0; i < types.length; i++) {
            barakoaMasks[i] = new ItemBarakoaMask(types[i]);
        }
        dart = new ItemDart();
        spear = new ItemSpear();
        blowgun = new ItemBlowgun();
        spawnEgg = new ItemSpawnEgg();

        GameRegistry.register(spawnEgg);

        GameRegistry.register(foliaathSeed);
        GameRegistry.register(wroughtAxe);
        GameRegistry.register(wroughtHelmet);
        for (ItemBarakoaMask itemBarakoaMask : barakoaMasks) {
            GameRegistry.register(itemBarakoaMask);
        }
        GameRegistry.register(dart);
        GameRegistry.register(spear);
        GameRegistry.register(blowgun);

        GameRegistry.register(mobRemover);
        //GameRegistry.register(test_structure);
    }
}
