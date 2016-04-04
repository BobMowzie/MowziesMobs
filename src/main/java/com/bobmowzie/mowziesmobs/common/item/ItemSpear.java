package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.CreativeTabHandler;
import net.minecraft.item.ItemSword;

public class ItemSpear extends ItemSword {
    public ItemSpear() {
        super(ToolMaterial.STONE);
        setUnlocalizedName("spear");
        setTextureName(MowziesMobs.MODID + ":spear");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
    }
}
