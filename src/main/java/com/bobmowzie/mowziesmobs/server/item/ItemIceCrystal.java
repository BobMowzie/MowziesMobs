package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemIceCrystal extends Item {
    public ItemIceCrystal(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(playerIn, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            playerIn.setActiveHand(handIn);
            if (stack.getDamage() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get()) {
                if (!playerCapability.isUsingIceBreath()) {
                    playerCapability.setIcebreath(new EntityIceBreath(EntityHandler.ICE_BREATH, worldIn, playerIn));
                    playerCapability.getIcebreath().setPositionAndRotation(playerIn.getPosX(), playerIn.getPosY() + playerIn.getEyeHeight() - 0.5f, playerIn.getPosZ(), playerIn.rotationYaw, playerIn.rotationPitch);
                    if (!worldIn.isRemote) worldIn.addEntity(playerCapability.getIcebreath());
                    playerCapability.setUsingIceBreath(true);
                }
                stack.damageItem(5, playerIn, p -> p.sendBreakAnimation(handIn));
                showDurabilityBar(playerIn.getHeldItem(handIn));
                playerIn.setActiveHand(handIn);
                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
            } else {
                EntityIceBreath iceBreath = playerCapability.getIcebreath();
                if (iceBreath != null) iceBreath.remove();
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
            if (playerCapability != null && playerCapability.isUsingIceBreath() && playerCapability.getIcebreath() != null) {
                playerCapability.setUsingIceBreath(false);
                playerCapability.getIcebreath().remove();
            }
        }
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get()) {
            tooltip.add(new TranslationTextComponent(getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }
}
