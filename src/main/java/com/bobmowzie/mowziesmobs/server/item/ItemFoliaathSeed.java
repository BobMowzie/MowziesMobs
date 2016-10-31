package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;

import java.util.List;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed() {
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setUnlocalizedName("foliaathSeed");
        setRegistryName("foliaath_seed");
    }

    public Entity spawnCreature(World world, EntityLiving entity, double x, double y, double z) {
        if (entity != null) {
            entity.setLocationAndAngles(x + 0.5, y, z + 0.5, world.rand.nextFloat() * 360 - 180, 0);
            entity.rotationYawHead = entity.rotationYaw;
            entity.renderYawOffset = entity.rotationYaw;
            entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
            if (!entity.getCanSpawnHere()) {
                return null;
            }
            world.spawnEntityInWorld(entity);
        }
        return entity;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        }
        IBlockState block = world.getBlockState(pos);
        Entity entity = spawnCreature(world, new EntityBabyFoliaath(world), pos.getX(), pos.getY(), pos.getZ());
        if (entity != null) {
            if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
            }
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize--;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        ItemHandler.addItemText(this, tooltip);
    }
}
