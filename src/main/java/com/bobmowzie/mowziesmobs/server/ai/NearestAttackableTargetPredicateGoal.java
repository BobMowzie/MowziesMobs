package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.world.entity.EntityPredicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;


public class NearestAttackableTargetPredicateGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public NearestAttackableTargetPredicateGoal(MobEntity goalOwnerIn, Class targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, EntityPredicate predicate) {
        super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, null);
        this.targetEntitySelector = predicate;
    }
}
