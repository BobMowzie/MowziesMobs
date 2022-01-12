package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.resources.*;
import net.minecraft.resources.Hand;
import net.minecraft.resources.SoundEvents;
import net.minecraft.resources.text.ITextComponent;
import net.minecraft.resources.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWroughtAxe extends MowzieAxeItem {

    public ItemWroughtAxe(Item.Properties properties) {
        super(ItemTier.IRON, -3 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(), -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed.get().floatValue(), properties);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStackMaterial) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && super.getIsRepairable(itemStack, itemStackMaterial);
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
    public boolean hitEntity(ItemStack heldItemStack, LivingEntity entityHit, LivingEntity attacker) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get()) heldItemStack.damageItem(2, attacker, p -> p.sendBreakAnimation(Hand.MAIN_HAND));
        if (!entityHit.world.isClientSide) {
            entityHit.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, Player player, Hand hand) {
        if (hand == Hand.MAIN_HAND && player.getCooledAttackStrength(0.5F) == 1.0f) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null && playerCapability.getUntilAxeSwing() <= 0) {
                boolean verticalAttack = player.isSneaking() && player.isOnGround();
                if (verticalAttack)
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SLAM_ABILITY);
                else
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SWING_ABILITY);
                playerCapability.setVerticalSwing(verticalAttack);
                playerCapability.setUntilAxeSwing(30);
                player.setActiveHand(hand);
                if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && !player.abilities.isCreativeMode) player.getHeldItem(hand).damageItem(2, player, p -> p.sendBreakAnimation(hand));
            }
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig;
    }
}
