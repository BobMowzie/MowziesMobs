package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import java.util.List;

public class AnimationFWNAttackAI extends AnimationAttackAI<EntityWroughtnaut> {
    private float arc;

    public AnimationFWNAttackAI(EntityWroughtnaut entity, Animation animation, String sound, float knockback, float range, float arc) {
        super(entity, animation, sound, "", knockback, range, 0, 0);
        this.arc = arc;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        animatingEntity.playSound("mowziesmobs:wroughtnautPreSwing1", 1.5F, 1F);
    }

    @Override
    public void updateTask() {
        animatingEntity.motionX = 0;
        animatingEntity.motionZ = 0;
        if (animatingEntity.getAnimationTick() < (getAnimation().getDuration() / 2 + 2) && entityTarget != null) {
            animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30, 30);
        }
        if (animatingEntity.getAnimationTick() == 6) {
            animatingEntity.playSound("mowziesmobs:wroughtnautCreak", 0.5F, 1);
        } else if (animatingEntity.getAnimationTick() == (getAnimation().getDuration() / 2)) {
            animatingEntity.playSound(attackSound, 1.2F, 1);
        } else if (animatingEntity.getAnimationTick() == (getAnimation().getDuration() / 2 + 2)) {
            animatingEntity.playSound("mowziesmobs:wroughtnautSwing1", 1.5F, 1);
            List<EntityLivingBase> entitiesHit = animatingEntity.getEntityLivingBaseNearby(range, 3, range, range);
            float damage = (float) animatingEntity.getAttack();
            boolean hit = false;
            for (EntityLivingBase entityHit : entitiesHit) {
                float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - animatingEntity.posZ, entityHit.posX - animatingEntity.posX) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = animatingEntity.renderYawOffset % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                float entityHitDistance = (float) Math.sqrt((entityHit.posZ - animatingEntity.posZ) * (entityHit.posZ - animatingEntity.posZ) + (entityHit.posX - animatingEntity.posX) * (entityHit.posX - animatingEntity.posX));
                if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                    entityHit.attackEntityFrom(DamageSource.causeMobDamage(animatingEntity), damage);
                    entityHit.motionX *= knockback;
                    entityHit.motionZ *= knockback;
                    hit = true;
                }
            }
            if (hit) {
                animatingEntity.playSound("minecraft:random.anvil_land", 1, 0.5F);
            }
        }
    }
}
