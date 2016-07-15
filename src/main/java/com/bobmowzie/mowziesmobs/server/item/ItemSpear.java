package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.item.ItemSword;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

public class ItemSpear extends ItemSword {
    public ItemSpear() {
        super(ToolMaterial.STONE);
        setUnlocalizedName("spear");
        setTextureName(MowziesMobs.MODID + ":spear");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
    }
}
