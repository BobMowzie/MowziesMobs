package com.bobmowzie.mowziesmobs.server.compat;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;


//Original aspect registry code from the Twilight Forest mod
public class Thaumcraft {
    public static void init() {
        try {
            registerTCObjectTag("Foliaath", new AspectList()
                    .add(Aspect.PLANT, 25)
                    .add(Aspect.TRAP, 13)
            );
            registerTCObjectTag("Baby Foliaath", new AspectList()
                    .add(Aspect.PLANT, 15)
                    .add(Aspect.TRAP, 7)
            );
            registerTCObjectTag("Ferrous Wroughtnaut", new AspectList()
                    .add(Aspect.PROTECT, 40)
                    .add(Aspect.METAL, 35)
                    .add(Aspect.MAN, 6)
            );
            registerTCObjectTag("Barakoa", new AspectList()
                    .add(Aspect.AVERSION, 17)
                    .add(Aspect.MAN, 5)
                    .add(Aspect.ENTROPY, 5)
            );
            registerTCObjectTag("Barakoana", new AspectList()
                    .add(Aspect.AVERSION, 20)
                    .add(Aspect.MAN, 5)
                    .add(Aspect.ORDER, 5)
            );
            registerTCObjectTag("Barako", new AspectList()
                    .add(Aspect.FIRE, 30)
                    .add(Aspect.LIGHT, 30)
                    .add(Aspect.DESIRE, 26)
                    .add(Aspect.MAGIC, 13)
                    .add(Aspect.MAN, 5)
            );
            registerTCObjectTag("Frostmaw", new AspectList()
                    .add(Aspect.BEAST, 37)
                    .add(Aspect.COLD, 25)
            );
            registerTCObjectTag("Grottol", new AspectList()
                    .add(Aspect.CRYSTAL, 30)
                    .add(Aspect.DESIRE, 30)
                    .add(Aspect.EARTH, 17)
            );
            registerTCObjectTag(BlockHandler.PAINTED_ACACIA, 0, new AspectList()
                    .add(Aspect.PLANT, 3)
                    .add(Aspect.CRAFT, 3)
            );
            registerTCObjectTag(BlockHandler.PAINTED_ACACIA_SLAB, 0, new AspectList()
                    .add(Aspect.PLANT, 1)
                    .add(Aspect.CRAFT, 1)
            );
            registerTCObjectTag(BlockHandler.PAINTED_ACACIA_DOUBLE_SLAB, 0, new AspectList()
                    .add(Aspect.PLANT, 2)
                    .add(Aspect.CRAFT, 2)
            );
            registerTCObjectTag(ItemHandler.ICE_CRYSTAL, 0, new AspectList()
                    .add(Aspect.COLD, 30)
                    .add(Aspect.CRYSTAL, 23)
                    .add(Aspect.MAGIC, 17)
                    .add(Aspect.DESIRE, 9)
            );
            registerTCObjectTag(ItemHandler.FOLIAATH_SEED, 0, new AspectList()
                    .add(Aspect.PLANT, 5)
                    .add(Aspect.LIFE, 1)
                    .add(Aspect.TRAP, 1)
            );
            registerTCObjectTag(ItemHandler.WROUGHT_AXE, 0, new AspectList()
                    .add(Aspect.METAL, 33)
                    .add(Aspect.AVERSION, 12)
                    .add(Aspect.EARTH, 10)
            );
            registerTCObjectTag(ItemHandler.WROUGHT_HELMET, 0, new AspectList()
                    .add(Aspect.METAL, 33)
                    .add(Aspect.PROTECT, 25)
            );
            registerTCObjectTag(ItemHandler.BARAKOA_MASK_BLISS, 0, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.FLIGHT, 7)
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.MAN, 3)
                    .add(Aspect.SOUL, 1)
            );
            registerTCObjectTag(ItemHandler.BARAKOA_MASK_FEAR, 0, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.MOTION, 7)
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.MAN, 3)
                    .add(Aspect.SOUL, 1)
            );
            registerTCObjectTag(ItemHandler.BARAKOA_MASK_FURY, 0, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.AVERSION, 7)
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.MAN, 3)
                    .add(Aspect.SOUL, 1)
            );
            registerTCObjectTag(ItemHandler.BARAKOA_MASK_MISERY, 0, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.PROTECT, 13)
                    .add(Aspect.MAN, 3)
                    .add(Aspect.SOUL, 1)
            );
            registerTCObjectTag(ItemHandler.BARAKOA_MASK_RAGE, 0, new AspectList()
                    .add(Aspect.PLANT, 12)
                    .add(Aspect.TOOL, 7)
                    .add(Aspect.PROTECT, 5)
                    .add(Aspect.MAN, 3)
                    .add(Aspect.SOUL, 1)
            );
            registerTCObjectTag(ItemHandler.BARAKO_MASK, 0, new AspectList()
                    .add(Aspect.DESIRE, 20)
                    .add(Aspect.LIGHT, 7)
                    .add(Aspect.FIRE, 7)
                    .add(Aspect.METAL, 5)
                    .add(Aspect.PROTECT, 12)
                    .add(Aspect.MAN, 3)
                    .add(Aspect.SOUL, 1)
            );
            registerTCObjectTag(ItemHandler.SPEAR, 0, new AspectList()
                    .add(Aspect.AVERSION, 8)
                    .add(Aspect.BEAST, 5)
                    .add(Aspect.PLANT, 5)
            );
            registerTCObjectTag(ItemHandler.BLOWGUN, 0, new AspectList()
                    .add(Aspect.AVERSION, 8)
                    .add(Aspect.PLANT, 7)
            );
            registerTCObjectTag(ItemHandler.DART, 0, new AspectList()
                    .add(Aspect.AVERSION, 5)
                    .add(Aspect.FLIGHT, 3)
                    .add(Aspect.DEATH, 3)
                    .add(Aspect.PLANT, 3)
            );
            registerTCObjectTag(ItemHandler.EARTH_TALISMAN, 0, new AspectList()
                    .add(Aspect.EARTH, 30)
                    .add(Aspect.MAGIC, 20)
                    .add(Aspect.AVERSION, 10)
            );
            registerTCObjectTag(ItemHandler.CAPTURED_GROTTOL, 0, new AspectList()
                    .add(Aspect.CRYSTAL, 30)
                    .add(Aspect.DESIRE, 30)
                    .add(Aspect.EARTH, 17)
                    .add(Aspect.TRAP, 12)
            );

        } catch (Exception e) {

        }
    }

    // Register a block with Thaumcraft aspects
    private static void registerTCObjectTag(Block block, int meta, AspectList list) {
        if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
        ThaumcraftApi.registerObjectTag(new ItemStack(block, 1, meta), list);
    }

    // Register blocks with Thaumcraft aspects
    private static void registerTCObjectTag(Block block, int[] metas, AspectList list) {
        for (int meta : metas)
            registerTCObjectTag(block, meta, list);
    }

    // Register an item with Thaumcraft aspects
    private static void registerTCObjectTag(Item item, int meta, AspectList list) {
        if (meta == -1) meta = OreDictionary.WILDCARD_VALUE;
        ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, meta), list);
    }

    // Register items with Thaumcraft aspects
    private static void registerTCObjectTag(Item item, int[] metas, AspectList list) {
        for (int meta : metas)
            registerTCObjectTag(item, meta, list);
    }

    // Register an item with Thaumcraft aspects
    private static void registerTCObjectTag(String entityName, AspectList list) {
        ThaumcraftApi.registerEntityTag(entityName, list);
    }
}
