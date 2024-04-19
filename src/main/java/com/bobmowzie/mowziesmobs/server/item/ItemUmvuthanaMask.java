package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.item.RenderSolVisageArmor;
import com.bobmowzie.mowziesmobs.client.render.item.RenderUmvuthanaMaskItem;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaCraneToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaFollowerToPlayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemUmvuthanaMask extends MowzieArmorItem implements UmvuthanaMask, GeoItem {
    private final MaskType type;
    private static final UmvuthanaMaskMaterial UMVUTHANA_MASK_MATERIAL = new UmvuthanaMaskMaterial();

    public String controllerName = "controller";
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ItemUmvuthanaMask(MaskType type, Item.Properties properties) {
        super(UMVUTHANA_MASK_MATERIAL, Type.HELMET, properties);
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
        if (headStack.getItem() instanceof ItemSolVisage) {
            if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get() && !player.isCreative()) headStack.hurtAndBreak(2, player, p -> p.broadcastBreakEvent(hand));
            boolean didSpawn = spawnUmvuthana(type, stack, player,(float)stack.getDamageValue() / (float)stack.getMaxDamage());
            if (didSpawn) {
                if (!player.isCreative()) stack.shrink(1);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }
        return super.use(world, player, hand);
    }

    private boolean spawnUmvuthana(MaskType mask, ItemStack stack, Player player, float durability) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getPackSize() < 10) {
            player.playSound(MMSounds.ENTITY_UMVUTHI_BELLY.get(), 1.5f, 1);
            player.playSound(MMSounds.ENTITY_UMVUTHANA_BLOWDART.get(), 1.5f, 0.5f);
            double angle = player.getYHeadRot();
            if (angle < 0) {
                angle = angle + 360;
            }
            EntityUmvuthanaFollowerToPlayer umvuthana;
            if (mask == MaskType.FAITH) umvuthana = new EntityUmvuthanaCraneToPlayer(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER.get(), player.level(), player);
            else umvuthana = new EntityUmvuthanaFollowerToPlayer(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER.get(), player.level(), player);
//            property.addPackMember(umvuthana);
            if (!player.level().isClientSide) {
                if (mask != MaskType.FAITH) {
                    int weapon;
                    if (mask != MaskType.FURY) weapon = umvuthana.randomizeWeapon();
                    else weapon = 0;
                    umvuthana.setWeapon(weapon);
                }
                umvuthana.absMoveTo(player.getX() + 1 * Math.sin(-angle * (Math.PI / 180)), player.getY() + 1.5, player.getZ() + 1 * Math.cos(-angle * (Math.PI / 180)), (float) angle, 0);
                umvuthana.setActive(false);
                umvuthana.active = false;
                player.level().addFreshEntity(umvuthana);
                double vx = 0.5 * Math.sin(-angle * Math.PI / 180);
                double vy = 0.5;
                double vz = 0.5 * Math.cos(-angle * Math.PI / 180);
                umvuthana.setDeltaMovement(vx, vy, vz);
                umvuthana.setHealth((1.0f - durability) * umvuthana.getMaxHealth());
                umvuthana.setMask(mask);
                umvuthana.setStoredMask(stack.copy());
                if (stack.hasCustomHoverName())
                    umvuthana.setCustomName(stack.getHoverName());
            }
            return true;
        }
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (equipmentSlot == EquipmentSlot.HEAD) armorRenderer.prepForRender(entityLiving, itemStack, equipmentSlot, original);
                return armorRenderer;
            }

            private final BlockEntityWithoutLevelRenderer itemRenderer = new RenderUmvuthanaMaskItem();
            private final GeoArmorRenderer<?> armorRenderer = new RenderSolVisageArmor();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return itemRenderer;
            }
        });
    }

    public MaskType getMaskType() {
        return type;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String s = ChatFormatting.stripFormatting(stack.getHoverName().getString());
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/umvuthana_mask_" + this.type.name + ".png").toString();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig;
    }

    public <P extends Item & GeoItem> PlayState predicate(AnimationState<P> event) {
        Entity entity = event.getData(DataTickets.ENTITY);
        if (entity instanceof EntityUmvuthana) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("umvuthana"));
        }
        else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("player"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, controllerName, 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private static class UmvuthanaMaskMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForType(Type equipmentSlotType) {
            return ArmorMaterials.LEATHER.getDurabilityForType(equipmentSlotType);
        }

        @Override
        public int getDefenseForType(Type equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReductionValue;
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
            return "umvuthana_mask";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughnessValue;
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterials.LEATHER.getKnockbackResistance();
        }
    }
}
