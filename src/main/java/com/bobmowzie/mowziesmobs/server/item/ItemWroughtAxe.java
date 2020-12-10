package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.item.AxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWroughtAxe extends AxeItem {

    public ItemWroughtAxe(Item.Properties properties) {
        super(ItemTier.IRON, -1 + ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get(), -4f + ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed.get(), properties);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStackMaterial) {
        return ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && super.getIsRepairable(itemStack, itemStackMaterial);
    }

    @Override
    public boolean hitEntity(ItemStack heldItemStack, LivingEntity entityHit, LivingEntity attacker) {
        if (ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get()) heldItemStack.damageItem(2, attacker, p -> p.sendBreakAnimation(Hand.MAIN_HAND));
        if (!entityHit.world.isRemote) {
            entityHit.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability.getUntilAxeSwing() <= 0) {
                boolean verticalAttack = player.isSneaking() && player.onGround;
                EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK, world, player, verticalAttack);
                axeAttack.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                world.addEntity(axeAttack);
                playerCapability.setVerticalSwing(verticalAttack);
                playerCapability.setUntilAxeSwing(PlayerCapability.SWING_COOLDOWN);
                if (ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get() && !player.abilities.isCreativeMode) player.getHeldItem(hand).damageItem(2, player, p -> p.sendBreakAnimation(hand));
            }
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0"));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1"));
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.2"));
    }
}
