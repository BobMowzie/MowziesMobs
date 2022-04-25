package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class ItemCapturedGrottol extends Item {
    public ItemCapturedGrottol(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        InteractionHand hand = context.getHand();
        Level world = context.getLevel();
        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.FAIL;
        }
        BlockPos location = pos.relative(facing);
        ItemStack stack = player.getItemInHand(hand);
        if (!player.mayUseItemAt(location, facing, stack)) {
            return InteractionResult.FAIL;
        }
        if (!world.isClientSide) {
            EntityGrottol grottol = new EntityGrottol(EntityHandler.GROTTOL.get(), world);
            CompoundTag compound = stack.getTagElement("EntityTag");
            if (compound != null) {
                setData(grottol, compound);
            }
            grottol.moveTo(location, 0, 0);
            lookAtPlayer(grottol, player);
            grottol.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(location), MobSpawnType.MOB_SUMMONED, null, null);
            world.addFreshEntity(grottol);
            if (!player.abilities.instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void setData(EntityGrottol grottol, CompoundTag compound) {
        CompoundTag data = grottol.serializeNBT();
        UUID id = grottol.getUUID();
        data.merge(compound);
        grottol.deserializeNBT(data);
        grottol.setUUID(id);
    }

    private void lookAtPlayer(EntityGrottol grottol, Player player) {
        LookControl helper = new LookControl(grottol);
        helper.setLookAt(player, 180, 90);
        helper.tick();
        /*GoalSelector ai = grottol.goalSelector;
        Set<GoalSelector.EntityAITaskEntry> tasks = Sets.newLinkedHashSet(ai..taskEntries);
        ai.taskEntries.removeIf(entry -> !(entry.action instanceof LookAtGoal));
        grottol.getRNG().setSeed("IS MATh RElatEd tO ScIENCe?".hashCode());
        ai.onUpdateTasks();
        grottol.getRNG().setSeed(new Random().nextLong());
        ai.taskEntries.clear();
        ai.taskEntries.addAll(tasks);*/
    }

    public ItemStack create(EntityGrottol grottol) {
        ItemStack stack = new ItemStack(this);
        stack.addTagElement("EntityTag", grottol.serializeNBT());
        return stack;
    }
}
