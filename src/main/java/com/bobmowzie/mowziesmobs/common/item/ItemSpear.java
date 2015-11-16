package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import net.minecraft.item.ItemSword;

/**
 * Created by jnad325 on 11/15/15.
 */
public class ItemSpear extends ItemSword {
    public ItemSpear() {
        super(ToolMaterial.STONE);
        setUnlocalizedName("spear");
        setTextureName(MowziesMobs.MODID + ":spear");
        setCreativeTab(MMTabs.generic);
    }
}
