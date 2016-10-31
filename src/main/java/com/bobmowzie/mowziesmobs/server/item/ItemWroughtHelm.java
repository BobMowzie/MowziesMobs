package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

import java.util.List;

public class ItemWroughtHelm extends ItemArmor {
    public ItemWroughtHelm() {
        super(ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("wroughtHelm");
        setRegistryName("wrought_helm");
    }

    // Dirty trick to get our item to render as the item model
    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {}

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("Never breaks");
    }
}
