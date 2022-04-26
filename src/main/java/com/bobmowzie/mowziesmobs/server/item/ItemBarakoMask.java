package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Created by BobMowzie on 8/15/2016.
 */
public class ItemBarakoMask extends MowzieArmorItem implements BarakoaMask {
    private static final SolVisageMaterial SOL_VISAGE_MATERIAL = new SolVisageMaterial();

    public ItemBarakoMask(Item.Properties properties) {
        super(SOL_VISAGE_MATERIAL, EquipmentSlot.HEAD, properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get()) return super.isValidRepairItem(toRepair, repair);
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Override
    public boolean canBeDepleted() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get();
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get() ? super.getDamage(stack): 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get() ? super.getDamage(stack): 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get()) super.setDamage(stack, damage);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
        SolVisageModel<?> model = MowziesMobs.PROXY.getSolVisageModel();
        model.hat.visible = armorSlot == EquipmentSlot.HEAD;

        if (_default != null) {
            model.young = _default.young;
            model.crouching = _default.crouching;
            model.riding = _default.riding;
            model.rightArmPose = _default.rightArmPose;
            model.leftArmPose = _default.leftArmPose;
        }

        return (A) model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/barako_mask.png").toString();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig;
    }

    private static class SolVisageMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlot equipmentSlotType) {
            return ArmorMaterials.GOLD.getDefenseForSlot(equipmentSlotType);
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReduction.get();
        }

        @Override
        public int getEnchantmentValue() {
            return ArmorMaterials.GOLD.getEnchantmentValue();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.GOLD.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return ArmorMaterials.GOLD.getRepairIngredient();
        }

        @Override
        public String getName() {
            return "sol_visage";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughness.get().floatValue();
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterials.GOLD.getKnockbackResistance();
        }
    }
}
