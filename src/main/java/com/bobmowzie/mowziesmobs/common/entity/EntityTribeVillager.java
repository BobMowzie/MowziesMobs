package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.ai.BarakoaAttackTargetAI;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityTribeVillager extends EntityTribesman {
    public EntityTribeVillager(World world) {
        super(world);
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(3, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, true));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
