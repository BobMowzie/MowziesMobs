package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWroughtHelm extends ArmorItem {
    private static int d = ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorData.damageReduction;
    private static ArmorMaterial ARMOR_WROUGHT_HELM = EnumHelper.addArmorMaterial("WROUGHT_HELM", "iron", 15, new int[] {d, d, d, d}, ArmorMaterial.IRON.getEnchantability(), ArmorMaterial.IRON.getSoundEvent(), ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorData.toughness);

    public ItemWroughtHelm(Item.Properties properties) {
        super(ARMOR_WROUGHT_HELM, 2, EquipmentSlotType.HEAD, properties);
    }

    // Dirty trick to get our item to render as the item model
    @Override
    public EquipmentSlotType getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (!ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable) return false;
        ItemStack mat = Items.IRON_INGOT.getDefaultInstance();
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat,repair,false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable ? super.getDamage(stack): 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable) super.setDamage(stack, damage);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
