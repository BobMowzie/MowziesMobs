package com.bobmowzie.mowziesmobs.server.item;

import java.util.Set;
import java.util.UUID;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCapturedGrottol extends Item {
    public ItemCapturedGrottol() {
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        BlockPos location = pos.offset(facing);
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(location, facing, stack)) {
            return EnumActionResult.FAIL;
        }
        if (!world.isRemote) {
            EntityGrottol grottol = new EntityGrottol(world);
            NBTTagCompound compound = stack.getSubCompound("EntityTag");
            if (compound != null) {
                setData(grottol, compound);
            }
            grottol.moveToBlockPosAndAngles(location, 0, 0);
            lookAtPlayer(grottol, player);
            grottol.onInitialSpawn(world.getDifficultyForLocation(location), null);
            world.spawnEntity(grottol);
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

    private void setData(EntityGrottol grottol, NBTTagCompound compound) {
        NBTTagCompound data = grottol.writeToNBT(new NBTTagCompound());
        UUID id = grottol.getUniqueID();
        data.merge(compound);
        grottol.readFromNBT(data);
        grottol.setUniqueId(id);
    }

    private void lookAtPlayer(EntityGrottol grottol, EntityPlayer player) {
        EntityLookHelper helper = new EntityLookHelper(grottol);
        helper.setLookPositionWithEntity(player, 180, 90);
        helper.onUpdateLook();
        EntityAITasks ai = grottol.tasks;
        Set<EntityAITasks.EntityAITaskEntry> tasks = Sets.newLinkedHashSet(ai.taskEntries);
        ai.taskEntries.removeIf(entry -> !(entry.action instanceof EntityAIWatchClosest));
        grottol.getRNG().setSeed("IS MATh RElatEd tO ScIENCe?".hashCode());
        ai.onUpdateTasks();
        ai.taskEntries.clear();
        ai.taskEntries.addAll(tasks);
    }

    public ItemStack create(EntityGrottol grottol) {
        ItemStack stack = new ItemStack(this);
        stack.setTagInfo("EntityTag", grottol.writeToNBT(new NBTTagCompound()));
        return stack;
    }
}
