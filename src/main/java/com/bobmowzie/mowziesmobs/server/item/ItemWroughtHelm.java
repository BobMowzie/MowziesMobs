package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWroughtHelm extends ItemArmor {
    public ItemWroughtHelm() {
        super(ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setTranslationKey("wroughtHelmet");
        setRegistryName("wrought_helmet");
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
