package com.bobmowzie.mowziesmobs.server.entity.tribe;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;

public class EntityTribeVillager extends EntityTribesman {
    public EntityTribeVillager(World world) {
        super(world);
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, true));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
