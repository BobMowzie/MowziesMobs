package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.client.render.item.RenderEarthboreGauntlet;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemEarthboreGauntlet extends MowzieToolItem implements GeoItem {
    public String controllerName = "controller";
    public String controllerIdleName = "controller_idle";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenLoop("open");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");
    private static final String ATTACK_ANIM_NAME = "attack";

    public ItemEarthboreGauntlet(Properties properties) {
        super(-2 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackDamageValue, -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig.attackSpeedValue, Tiers.STONE, BlockTags.MINEABLE_WITH_PICKAXE, properties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new RenderEarthboreGauntlet();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
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
        tooltip.add(Component.translatable(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Component.translatable(getDescriptionId() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.breakable.get()) {
            tooltip.add(Component.translatable(getDescriptionId() + ".text.4").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, controllerIdleName, 3, this::predicateIdle));
        controllers.add(new AnimationController<>(this, controllerName, 3, state -> PlayState.STOP)
                .triggerableAnim(ATTACK_ANIM_NAME, ATTACK_ANIM));
    }

    public <P extends Item & GeoItem> PlayState predicateIdle(AnimationState<P> event) {
        event.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(entity);
        if (abilityCapability != null && abilityCapability.getActiveAbility() == null) {
            if (entity.getUseItem() != stack) {
                if (entity.level() instanceof ServerLevel) {
                    triggerAnim(entity, GeoItem.getOrAssignId(stack, (ServerLevel) entity.level()), controllerName, ATTACK_ANIM_NAME);
                }
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.toolConfig;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
