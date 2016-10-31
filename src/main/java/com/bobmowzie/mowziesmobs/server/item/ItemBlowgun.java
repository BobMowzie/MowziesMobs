package com.bobmowzie.mowziesmobs.server.item;

import java.util.List;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class ItemBlowgun extends ItemBow {
    public ItemBlowgun() {
        super();
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("blowgun");
        setRegistryName("blowgun");
    }

    @Override
    protected boolean isArrow(ItemStack stack) {
        return stack != null && stack.getItem() == ItemHandler.INSTANCE.dart;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        ItemHandler.addItemText(this, tooltip);
    }
}
