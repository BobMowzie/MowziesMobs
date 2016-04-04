package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.ItemMMSpawnEgg;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public enum ItemHandler {
    INSTANCE;

    public Item foliaath_seed;
    public Item test_structure;
    public Item mob_remover;
    public Item wrought_axe;
    public Item wrought_helmet;
    public ItemBarakoaMask[] barakoa_masks;
    public Item dart;
    public Item spear;
    public Item blowgun;
    public Item spawn_egg;

    public void onInit() {
        foliaath_seed = new ItemFoliaathSeed();
        test_structure = new ItemTestStructure();
        mob_remover = new ItemMobRemover();
        wrought_axe = new ItemWroughtAxe();
        wrought_helmet = new ItemWroughtHelm();
        barakoa_masks = new ItemBarakoaMask[ItemBarakoaMask.BarakoaMaskType.VALUES.length];
        for (int i = 0; i < ItemBarakoaMask.BarakoaMaskType.VALUES.length; i++) {
            barakoa_masks[i] = new ItemBarakoaMask(ItemBarakoaMask.BarakoaMaskType.VALUES[i]);
        }
        dart = new ItemDart();
        spear = new ItemSpear();
        blowgun = new ItemBlowgun();
        spawn_egg = new ItemMMSpawnEgg();

        GameRegistry.registerItem(spawn_egg, "spawnEgg");

        GameRegistry.registerItem(foliaath_seed, "foliaathseed");
        GameRegistry.registerItem(wrought_axe, "wroughtaxe");
        GameRegistry.registerItem(wrought_helmet, "wroughthelm");
        for (ItemBarakoaMask itemBarakoaMask : barakoa_masks) {
            GameRegistry.registerItem(itemBarakoaMask, itemBarakoaMask.getType().getUnlocalizedName());
        }
        GameRegistry.registerItem(dart, "dart");
        GameRegistry.registerItem(spear, "spear");
        GameRegistry.registerItem(blowgun, "blowgun");

        GameRegistry.registerItem(mob_remover, "mobremover");
        GameRegistry.registerItem(test_structure, "teststructure");
    }
}
