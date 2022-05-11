package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

public class ItemWroughtAxe extends MowzieAxeItem {

    public ItemWroughtAxe(Item.Properties properties) {
        super(Tiers.IRON, -3 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(), -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed.get().floatValue(), properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStackMaterial) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && super.isValidRepairItem(itemStack, itemStackMaterial);
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        return playerCapability == null || (!playerCapability.getAxeCanAttack() && playerCapability.getUntilAxeSwing() > 0);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(entity, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        return playerCapability != null && playerCapability.getUntilAxeSwing() > 0;
    }

    @Override
    public boolean hurtEnemy(ItemStack heldItemStack, LivingEntity entityHit, LivingEntity attacker) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get()) heldItemStack.hurtAndBreak(2, attacker, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        if (!entityHit.level.isClientSide) {
            entityHit.playSound(SoundEvents.ANVIL_LAND, 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public boolean canBeDepleted() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND && player.getAttackStrengthScale(0.5F) == 1.0f) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null && playerCapability.getUntilAxeSwing() <= 0) {
                boolean verticalAttack = player.isShiftKeyDown() && player.isOnGround();
                if (verticalAttack)
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SLAM_ABILITY);
                else
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SWING_ABILITY);
                playerCapability.setVerticalSwing(verticalAttack);
                playerCapability.setUntilAxeSwing(30);
                player.startUsingItem(hand);
                if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && !player.getAbilities().instabuild) player.getItemInHand(hand).hurtAndBreak(2, player, p -> p.broadcastBreakEvent(hand));
            }
            return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig;
    }
}
