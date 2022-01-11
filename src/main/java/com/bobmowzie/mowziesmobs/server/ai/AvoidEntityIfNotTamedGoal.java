package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.world.entity.CreatureEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.passive.TameableEntity;

public class AvoidEntityIfNotTamedGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public AvoidEntityIfNotTamedGoal(CreatureEntity entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean shouldExecute() {
        boolean isTamed;
        isTamed = entity instanceof TameableEntity && ((TameableEntity) entity).isTamed();
        return super.shouldExecute() && !isTamed;
    }
}
