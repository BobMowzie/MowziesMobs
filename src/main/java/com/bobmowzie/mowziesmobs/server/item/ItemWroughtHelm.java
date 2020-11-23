package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWroughtHelm extends ArmorItem {
    private static final WroughtHelmMaterial ARMOR_WROUGHT_HELM = new WroughtHelmMaterial();

    public ItemWroughtHelm(Item.Properties properties) {
        super(ARMOR_WROUGHT_HELM, EquipmentSlotType.HEAD, properties);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable) return super.getIsRepairable(toRepair, repair);
        return false;
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

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        WroughtHelmModel<?> model = new WroughtHelmModel<>();
        model.bipedHeadwear.showModel = armorSlot == EquipmentSlotType.HEAD;

        model.isChild = _default.isChild;
        model.isSneak = _default.isSneak;
        model.isSitting = _default.isSitting;
        model.rightArmPose = _default.rightArmPose;
        model.leftArmPose = _default.leftArmPose;

        return (A) model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/wrought_helmet.png").toString();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0"));
    }

    private static class WroughtHelmMaterial implements IArmorMaterial {

        @Override
        public int getDurability(EquipmentSlotType equipmentSlotType) {
            return ArmorMaterial.IRON.getDurability(equipmentSlotType);
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType equipmentSlotType) {
            return ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorData.damageReduction;
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterial.IRON.getEnchantability();
        }

        @Override
        public SoundEvent getSoundEvent() {
            return ArmorMaterial.IRON.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return ArmorMaterial.IRON.getRepairMaterial();
        }

        @Override
        public String getName() {
            return "wrought_helm";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorData.toughness;
        }
    }
}
