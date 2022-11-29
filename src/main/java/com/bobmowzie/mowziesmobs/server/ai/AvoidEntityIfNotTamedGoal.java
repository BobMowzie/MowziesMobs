package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.TameableEntity;

public class AvoidEntityIfNotTamedGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public AvoidEntityIfNotTamedGoal(CreatureEntity entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !(entity instanceof TameableEntity && ((TameableEntity) entity).isTamed());
    }
}
