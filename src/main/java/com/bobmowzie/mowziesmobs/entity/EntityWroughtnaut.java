package com.bobmowzie.mowziesmobs.entity;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityWroughtnaut extends MMEntityBase {
    public EntityWroughtnaut(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(4, new EntityAIWander(this, 0.3F));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        experienceValue = 20;
        this.setSize(0.5F, 2.5F);
    }
}