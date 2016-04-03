package com.bobmowzie.mowziesmobs.common.creativetab;

import com.bobmowzie.mowziesmobs.common.item.ItemHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public enum CreativeTabHandler {
    INSTANCE;

    public CreativeTabs generic;

    public void onInit() {
        generic = new CreativeTabs("mowziesmobs.generic") {
            @Override
            @SideOnly(Side.CLIENT)
            public Item getTabIconItem() {
                return ItemHandler.INSTANCE.itemBarakoaMasks[0];
            }
        };
    }
}
