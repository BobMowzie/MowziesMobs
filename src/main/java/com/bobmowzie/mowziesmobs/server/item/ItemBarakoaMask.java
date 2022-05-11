package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.armor.BarakoaMaskModel;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoanToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoayaToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemBarakoaMask extends MowzieArmorItem implements BarakoaMask {
    private final MaskType type;
    private static final BarakoaMaskMaterial BARAKOA_MASK_MATERIAL = new BarakoaMaskMaterial();

    public ItemBarakoaMask(MaskType type, Item.Properties properties) {
        super(BARAKOA_MASK_MATERIAL, EquipmentSlot.HEAD, properties);
        this.type = type;
    }

    public MobEffect getPotion() {
        return type.potion;
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack materialItemStack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack headStack = player.getInventory().armor.get(3);
        if (headStack.getItem() instanceof ItemBarakoMask) {
            if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get() && !player.isCreative()) headStack.hurtAndBreak(2, player, p -> p.broadcastBreakEvent(hand));
            boolean didSpawn = spawnBarakoa(type, stack, player,(float)stack.getDamageValue() / (float)stack.getMaxDamage());
            if (didSpawn) {
                if (!player.isCreative()) stack.shrink(1);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }
        return super.use(world, player, hand);
    }

    private boolean spawnBarakoa(MaskType mask, ItemStack stack, Player player, float durability) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getPackSize() < 10) {
            player.playSound(MMSounds.ENTITY_BARAKO_BELLY.get(), 1.5f, 1);
            player.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART.get(), 1.5f, 0.5f);
            double angle = player.getYHeadRot();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityBarakoanToPlayer barakoa;
            if (mask == MaskType.FAITH) barakoa = new EntityBarakoayaToPlayer(EntityHandler.BARAKOAYA_TO_PLAYER.get(), player.level, player);
            else barakoa = new EntityBarakoanToPlayer(EntityHandler.BARAKOAN_TO_PLAYER.get(), player.level, player);
//            property.addPackMember(barakoa);
            if (!player.level.isClientSide) {
                if (mask != MaskType.FAITH) {
                    int weapon;
                    if (mask != MaskType.FURY) weapon = barakoa.randomizeWeapon();
                    else weapon = 0;
                    barakoa.setWeapon(weapon);
                }
                barakoa.absMoveTo(player.getX() + 1 * Math.sin(-angle * (Math.PI / 180)), player.getY() + 1.5, player.getZ() + 1 * Math.cos(-angle * (Math.PI / 180)), (float) angle, 0);
                barakoa.setActive(false);
                barakoa.active = false;
                player.level.addFreshEntity(barakoa);
                double vx = 0.5 * Math.sin(-angle * Math.PI / 180);
                double vy = 0.5;
                double vz = 0.5 * Math.cos(-angle * Math.PI / 180);
                barakoa.setDeltaMovement(vx, vy, vz);
                barakoa.setHealth((1.0f - durability) * barakoa.getMaxHealth());
                barakoa.setMask(mask);
                barakoa.setStoredMask(stack.copy());
                if (stack.hasCustomHoverName())
                    barakoa.setCustomName(stack.getHoverName());
            }
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
        BarakoaMaskModel<?> model = MowziesMobs.PROXY.getBarakoaMaskModel();
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
        String s = ChatFormatting.stripFormatting(stack.getHoverName().getString());
        boolean wadoo = stack.hasCustomHoverName() && s != null && s.equals("Wadoo");
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa_" + this.type.name + (wadoo ? "_wadoo" : "") + ".png").toString();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorConfig;
    }

    private static class BarakoaMaskMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlot equipmentSlotType) {
            return ArmorMaterials.LEATHER.getDurabilityForSlot(equipmentSlotType);
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.BARAKOA_MASK.armorConfig.damageReduction.get();
        }

        @Override
        public int getEnchantmentValue() {
            return ArmorMaterials.LEATHER.getEnchantmentValue();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.LEATHER.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
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
            return ArmorMaterials.LEATHER.getKnockbackResistance();
        }
    }
}
