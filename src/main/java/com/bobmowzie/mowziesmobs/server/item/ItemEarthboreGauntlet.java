package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.google.common.collect.Sets;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemEarthboreGauntlet extends MowzieToolItem implements IAnimatable, ISyncable {
    public static final int ANIM_REST = 0;
    public static final int ANIM_OPEN = 1;
    public static final int ANIM_FIST = 2;
    public String controllerName = "controller";
    public String controllerIdleName = "controller_idle";
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemEarthboreGauntlet(Properties properties) {
        super(-2 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackDamage.get().floatValue(), -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackSpeed.get().floatValue(), Tiers.STONE, Sets.newHashSet(), properties);
        GeckoLibNetwork.registerSyncable(this);
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(playerIn);
        if (abilityCapability != null) {
            playerIn.startUsingItem(handIn);
            if (stack.getDamageValue() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.breakable.get()) {
                if (!worldIn.isClientSide()) AbilityHandler.INSTANCE.sendAbilityMessage(playerIn, AbilityHandler.TUNNELING_ABILITY);
                showDurabilityBar(playerIn.getItemInHand(handIn));
                playerIn.startUsingItem(handIn);
                return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
            }
            else {
                abilityCapability.getAbilityMap().get(AbilityHandler.TUNNELING_ABILITY).end();
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.durability.get();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.breakable.get()) {
            tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.4").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, controllerIdleName, 3, this::predicateIdle));
        animationData.addAnimationController(new AnimationController<>(this, controllerName, 3, this::predicate));
    }

    public <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    public <P extends Item & IAnimatable> PlayState predicateIdle(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(entity);
        if (abilityCapability != null && abilityCapability.getActiveAbility() == null) {
            if (entity.getUseItem() != stack) {
                playAnimation(entity, stack, ANIM_FIST);
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {
        // Always use GeckoLibUtil to get AnimationControllers when you don't have
        // access to an AnimationEvent
        final AnimationController<?> controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);
        controller.markNeedsReload();
        if (state == ANIM_REST) {
            controller.clearAnimationCache();
            controller.setAnimation(new AnimationBuilder().addAnimation("idle", true));
        } else if (state == ANIM_OPEN) {
            controller.clearAnimationCache();
            controller.setAnimation(new AnimationBuilder().addAnimation("open", true));
        } else if (state == ANIM_FIST) {
            controller.clearAnimationCache();
            controller.setAnimation(new AnimationBuilder().addAnimation("attack", false));
        }
    }

    public void playAnimation(LivingEntity entity, InteractionHand hand, int state) {
        ItemStack stack = entity.getItemInHand(hand);
        playAnimation(entity, stack, state);
    }

    public void playAnimation(LivingEntity entity, ItemStack stack, int state) {
        if (!entity.level.isClientSide) {
            int id = GeckoLibUtil.guaranteeIDForStack(stack, (ServerLevel)entity.level);
            PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> {
                return entity;
            });
            GeckoLibNetwork.syncAnimation(target, this, id, state);
        }
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig;
    }
}
