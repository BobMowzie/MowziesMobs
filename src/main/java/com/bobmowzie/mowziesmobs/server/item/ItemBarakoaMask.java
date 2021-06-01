package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.armor.BarakoaMaskModel;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.util.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBarakoaMask extends MowzieArmorItem implements BarakoaMask {
    private final MaskType type;
    private static final BarakoaMaskMaterial BARAKOA_MASK_MATERIAL = new BarakoaMaskMaterial();

    public ItemBarakoaMask(MaskType type, Item.Properties properties) {
        super(BARAKOA_MASK_MATERIAL, EquipmentSlotType.HEAD, properties);
        this.type = type;
    }

    public Effect getPotion() {
        return type.potion;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ItemStack headStack = player.inventory.armorInventory.get(3);
        if (headStack.getItem() instanceof ItemBarakoMask) {
            if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get() && !player.isCreative()) headStack.damageItem(2, player, p -> p.sendBreakAnimation(hand));
            boolean didSpawn = spawnBarakoa(type, stack, player,(float)stack.getDamage() / (float)stack.getMaxDamage());
            if (didSpawn && !player.isCreative()) {
                stack.shrink(1);
            }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        else return super.onItemRightClick(world, player, hand);
    }

    private boolean spawnBarakoa(MaskType mask, ItemStack stack, PlayerEntity player, float durability) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability.getPackSize() < 10) {
            player.playSound(MMSounds.ENTITY_BARAKO_BELLY.get(), 1.5f, 1);
            player.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART.get(), 1.5f, 0.5f);
            double angle = player.getRotationYawHead();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityBarakoanToPlayer barakoa = new EntityBarakoanToPlayer(EntityHandler.BARAKOAN_TO_PLAYER, player.world, player);
            barakoa.setMask(mask);
            barakoa.setStoredMask(stack);
//            property.addPackMember(barakoa);
            if (!player.world.isRemote) {
                if (mask == MaskType.FAITH) {
                    barakoa.initFaithMask();
                }
                else {
                    int weapon;
                    if (mask != MaskType.FURY) weapon = barakoa.randomizeWeapon();
                    else weapon = 0;
                    barakoa.setWeapon(weapon);
                }
                barakoa.setPositionAndRotation(player.getPosX() + 1 * Math.sin(-angle * (Math.PI / 180)), player.getPosY() + 1.5, player.getPosZ() + 1 * Math.cos(-angle * (Math.PI / 180)), (float) angle, 0);
                barakoa.setActive(false);
                barakoa.active = false;
                player.world.addEntity(barakoa);
                double vx = 0.5 * Math.sin(-angle * Math.PI / 180);
                double vy = 0.5;
                double vz = 0.5 * Math.cos(-angle * Math.PI / 180);
                barakoa.setMotion(vx, vy, vz);
                barakoa.setHealth((1.0f - durability) * barakoa.getMaxHealth());
            }
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        BarakoaMaskModel<?> model = new BarakoaMaskModel<>();
        model.bipedHeadwear.showModel = armorSlot == EquipmentSlotType.HEAD;

        if (_default != null) {
            model.isChild = _default.isChild;
            model.isSneak = _default.isSneak;
            model.isSitting = _default.isSitting;
            model.rightArmPose = _default.rightArmPose;
            model.leftArmPose = _default.leftArmPose;
        }

        return (A) model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + this.type.name + ".png").toString();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorConfig;
    }

    private static class BarakoaMaskMaterial implements IArmorMaterial {

        @Override
        public int getDurability(EquipmentSlotType equipmentSlotType) {
            return ArmorMaterial.LEATHER.getDurability(equipmentSlotType);
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorConfig.damageReduction.get();
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterial.LEATHER.getEnchantability();
        }

        @Override
        public SoundEvent getSoundEvent() {
            return ArmorMaterial.LEATHER.getSoundEvent();
        }

        @Override
        public Ingredient getRepairMaterial() {
            return null;
        }

        @Override
        public String getName() {
            return "barakoa_mask";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorConfig.toughness.get().floatValue();
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterial.LEATHER.getKnockbackResistance();
        }
    }
}
