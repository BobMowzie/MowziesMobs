package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;


public class NearestAttackableTargetPredicateGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public NearestAttackableTargetPredicateGoal(Mob goalOwnerIn, Class targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, TargetingConditions predicate) {
        super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, null);
        this.targetConditions = predicate;
    }
}
