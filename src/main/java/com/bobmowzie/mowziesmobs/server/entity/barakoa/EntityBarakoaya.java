package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;

public class EntityBarakoaya extends EntityBarakoa {
    public EntityBarakoaya(World world) {
        super(world);
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, 0, true, true, null));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
