package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum CreativeTabHandler {
    INSTANCE;

    public CreativeModeTab creativeTab;

    public void onInit() {
        creativeTab = new CreativeModeTab("mowziesmobs.creativeTab") {
            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                return new ItemStack(ItemHandler.LOGO.get());
            }
        };
    }
}
