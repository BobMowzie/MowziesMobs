package com.bobmowzie.mowziesmobs.entity;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityWroughtnaut extends MMEntityBase {
    public EntityWroughtnaut(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(4, new EntityAIWander(this, 0.2F));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 20;
        this.setSize(3F, 4F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (getAttackTarget() != null)
        {
            System.out.println(true);
            getNavigator().tryMoveToEntityLiving(this.getAttackTarget(), 0.26D);
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}