package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

import java.util.EnumSet;
import java.util.List;

public class AnimationRadiusAttack<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    private float radius;
    private float damageMultiplier;
    private float knockBackMultiplier;
    private int damageFrame;
    private boolean pureKnockback;

    public AnimationRadiusAttack(T entity, Animation animation, float radius, float damageMultiplier, float knockBackMultiplier, int damageFrame, boolean pureKnockback) {
        super(entity, animation);
        this.radius = radius;
        this.damageMultiplier = damageMultiplier;
        this.knockBackMultiplier = knockBackMultiplier;
        this.damageFrame = damageFrame;
        this.pureKnockback = pureKnockback;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() == damageFrame) {
            List<LivingEntity> hit = entity.getEntityLivingBaseNearby(radius, 2 * radius, radius, radius);
            for (LivingEntity aHit : hit) {
                if (entity instanceof EntityBarako && aHit instanceof LeaderSunstrikeImmune) {
                    continue;
                }
                entity.attackEntityAsMob(aHit, damageMultiplier, knockBackMultiplier);
                if (pureKnockback) {
                    double angle = entity.getAngleBetweenEntities(entity, aHit);
                    double x = knockBackMultiplier * Math.cos(Math.toRadians(angle - 90));
                    double z = knockBackMultiplier * Math.sin(Math.toRadians(angle - 90));
                    aHit.setMotion(x, aHit.getMotion().y, z);
                }
            }
        }
    }
}