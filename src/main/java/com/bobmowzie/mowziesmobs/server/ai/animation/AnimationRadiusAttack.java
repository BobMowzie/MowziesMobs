package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import java.util.List;

public class AnimationRadiusAttack<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    private float radius;
    private int damage;
    private float knockBack;
    private int damageFrame;
    private boolean pureKnockback;

    public AnimationRadiusAttack(T entity, Animation animation, float radius, int damage, float knockBack, int damageFrame, boolean pureKnockback) {
        super(entity, animation);
        this.radius = radius;
        this.damage = damage;
        this.knockBack = knockBack;
        this.damageFrame = damageFrame;
        this.pureKnockback = pureKnockback;
        setMutexBits(8);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entity.getAnimationTick() == damageFrame) {
            List<EntityLivingBase> hit = entity.getEntityLivingBaseNearby(radius, 2 * radius, radius, radius);
            for (EntityLivingBase aHit : hit) {
                if (entity instanceof EntityBarako && aHit instanceof LeaderSunstrikeImmune) {
                    continue;
                }
                aHit.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
                if (pureKnockback) {
                    double angle = entity.getAngleBetweenEntities(entity, aHit);
                    aHit.motionX = knockBack * Math.cos(Math.toRadians(angle - 90));
                    aHit.motionZ = knockBack * Math.sin(Math.toRadians(angle - 90));
                }
                else {
                    aHit.motionX *= knockBack;
                    aHit.motionZ *= knockBack;
                }
            }
        }
    }
}