package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;

import java.util.List;

public class ItemWroughtAxe extends ItemSword {
    public ItemWroughtAxe() {
        super(Item.ToolMaterial.IRON);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("wroughtAxe");
        setRegistryName("wrought_axe");
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
            if (property.untilAxeSwing <= 0) {
                if (player.isSneaking() && player.onGround) {
                    EntityAxeAttack axeAttack = new EntityAxeAttack(world, player, true);
                    world.spawnEntity(axeAttack);
                }
                else {
                    EntityAxeAttack axeAttack = new EntityAxeAttack(world, player, false);
                    world.spawnEntity(axeAttack);
                }
                property.untilAxeSwing = property.SWING_COOLDOWN;
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState block, BlockPos pos, EntityLivingBase destroyer) {
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack itemStack, IBlockState block) {
        return 1.0F;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        ItemHandler.addItemText(this, tooltip);
    }
}
