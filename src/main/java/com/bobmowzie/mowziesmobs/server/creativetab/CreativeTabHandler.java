package com.bobmowzie.mowziesmobs.server.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;

public enum CreativeTabHandler {
    INSTANCE;

    public CreativeTabs creativeTab;

    public void onInit() {
        creativeTab = new CreativeTabs("mowziesmobs.creativeTab") {
            @Override
            @SideOnly(Side.CLIENT)
            public Item getTabIconItem() {
                return ItemHandler.INSTANCE.barakoaMasks[0];
            }
        };
    }
}
