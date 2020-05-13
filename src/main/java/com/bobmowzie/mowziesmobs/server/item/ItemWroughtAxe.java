package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;

import java.util.List;

import javax.annotation.Nullable;

public class ItemWroughtAxe extends ItemAxe {
    public ItemWroughtAxe() {
        super(Item.ToolMaterial.IRON);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setTranslationKey("wroughtAxe");
        setRegistryName("wrought_axe");
        attackDamage = attackDamage * MowziesMobs.CONFIG.attackScaleWroughtAxe;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStackMaterial) {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack heldItemStack, EntityLivingBase player, EntityLivingBase entityHit) {
        if (!player.world.isRemote) {
            player.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player != null && hand == EnumHand.MAIN_HAND) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
            if (property != null && property.untilAxeSwing <= 0) {
                boolean verticalAttack = player.isSneaking() && player.onGround;
                EntityAxeAttack axeAttack = new EntityAxeAttack(world, player, verticalAttack);
                axeAttack.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                if (!world.isRemote) world.spawnEntity(axeAttack);
                property.verticalSwing = verticalAttack;
                property.untilAxeSwing = MowziePlayerProperties.SWING_COOLDOWN;
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState block, BlockPos pos, EntityLivingBase destroyer) {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, IBlockState block) {
        return 1.0F;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        ItemHandler.addItemText(this, tooltip);
    }
}
