package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;


public class NearestAttackableTargetPredicateGoal extends NearestAttackableTargetGoal {

    public NearestAttackableTargetPredicateGoal(MobEntity goalOwnerIn, Class targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, EntityPredicate predicate) {
        super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, null);
        this.targetEntitySelector = predicate;
    }
}
