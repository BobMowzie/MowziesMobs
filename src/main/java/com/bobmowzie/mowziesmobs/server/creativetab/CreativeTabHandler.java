package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public enum CreativeTabHandler {
    INSTANCE;

    public CreativeTabs creativeTab;

    public void onInit() {
        creativeTab = new CreativeTabs("mowziesmobs.creativeTab") {
            @Override
            @SideOnly(Side.CLIENT)
            public Item getTabIconItem() {
                return ItemHandler.INSTANCE.barakoa_masks[0];
            }
        };
    }
}
