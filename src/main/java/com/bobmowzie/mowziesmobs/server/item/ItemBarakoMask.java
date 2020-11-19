package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Josh on 8/15/2016.
 */
public class ItemBarakoMask extends ArmorItem implements BarakoaMask {
    private static SolVisageMaterial SOL_VISAGE_MATERIAL = new SolVisageMaterial();

    public ItemBarakoMask(Item.Properties properties) {
        super(SOL_VISAGE_MATERIAL, EquipmentSlotType.HEAD, properties);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable) return super.getIsRepairable(toRepair, repair);
        return false;
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

    private static class SolVisageMaterial implements IArmorMaterial {

        @Override
        public int getDurability(EquipmentSlotType equipmentSlotType) {
            return ArmorMaterial.GOLD.getDamageReductionAmount(equipmentSlotType);
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType equipmentSlotType) {
            return ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.armorData.damageReduction;
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterial.GOLD.getEnchantability();
        }

        @Override
        public SoundEvent getSoundEvent() {
            return ArmorMaterial.GOLD.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return ArmorMaterial.GOLD.getRepairMaterial();
        }

        @Override
        public String getName() {
            return "sol_visage";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.TOOLS_AND_ABILITIES.SOL_VISAGE.armorData.toughness;
        }
    }
}
