package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

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
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get() ? super.getMaxDamage(stack): 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get()) super.setDamage(stack, damage);
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
            return ArmorMaterials.GOLD.getDurabilityForSlot(equipmentSlotType);
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

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ItemBarakoMask.ArmorRender.INSTANCE);
    }

    private static final class ArmorRender implements IItemRenderProperties {
        private static final ItemBarakoMask.ArmorRender INSTANCE = new ItemBarakoMask.ArmorRender();
        private static HumanoidModel<?> MODEL;

        @Override
        public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
            if (MODEL == null) {
                EntityModelSet models = Minecraft.getInstance().getEntityModels();
                ModelPart root = models.bakeLayer(LayerHandler.SOL_VISAGE_LAYER);
                MODEL = new SolVisageModel<>(root);
            }
            return MODEL;
        }
    }
}
