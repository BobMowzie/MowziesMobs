package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.item.AxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemWroughtAxe extends AxeItem {

    public ItemWroughtAxe(Item.Properties properties) {
        super(ItemTier.IRON, -1 + ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolData.attackDamage, -4f + ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolData.attackSpeed, properties);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStackMaterial) {
        return ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable && super.getIsRepairable(itemStack, itemStackMaterial);
    }

    @Override
    public boolean hitEntity(ItemStack heldItemStack, LivingEntity entityHit, LivingEntity attacker) {
        if (ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable) heldItemStack.damageItem(2, attacker, p -> p.sendBreakAnimation(Hand.MAIN_HAND));
        if (!entityHit.world.isRemote) {
            entityHit.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player != null && hand == Hand.MAIN_HAND) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
            if (property != null && property.untilAxeSwing <= 0) {
                boolean verticalAttack = player.isSneaking() && player.onGround;
                EntityAxeAttack axeAttack = new EntityAxeAttack(world, player, verticalAttack);
                axeAttack.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                if (!world.isRemote) world.addEntity(axeAttack);
                property.verticalSwing = verticalAttack;
                property.untilAxeSwing = MowziePlayerProperties.SWING_COOLDOWN;
                if (ConfigHandler.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable && !player.abilities.isCreativeMode) player.getHeldItem(hand).damageItem(2, player, p -> p.sendBreakAnimation(hand));
            }
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }
}
