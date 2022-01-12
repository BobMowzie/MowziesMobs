package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.item.ItemGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum CreativeTabHandler {
    INSTANCE;

    public ItemGroup creativeTab;

    public void onInit() {
        creativeTab = new ItemGroup("mowziesmobs.creativeTab") {
            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack createIcon() {
                return new ItemStack(ItemHandler.LOGO);
            }
        };
    }
}
