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

/**
 * Created by Josh on 8/15/2016.
 */
public class ItemBarakoMask extends ArmorItem implements BarakoaMask {
    private static int d = ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.armorData.damageReduction;
    private static ArmorMaterial ARMOR_SOL_VISAGE = EnumHelper.addArmorMaterial("SOL_VISAGE", "gold", 7, new int[]{d, d, d, d}, ArmorMaterial.GOLD.getEnchantability(), ArmorMaterial.GOLD.getSoundEvent(), ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.armorData.toughness);

    public ItemBarakoMask(Item.Properties properties) {
        super(ARMOR_SOL_VISAGE, 2, EquipmentSlotType.HEAD, properties);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (!ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable) return false;
        ItemStack mat = Items.GOLD_INGOT.getDefaultInstance();
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat,repair,false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return null;
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable ? super.getDamage(stack): 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable) super.setDamage(stack, damage);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
