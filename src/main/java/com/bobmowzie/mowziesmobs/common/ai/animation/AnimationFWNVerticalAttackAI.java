package com.bobmowzie.mowziesmobs.common.ai.animation;

import com.bobmowzie.mowziesmobs.common.entity.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.common.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import java.util.List;

public class AnimationFWNVerticalAttackAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAttackAI<T> {
    private float arc;

    public AnimationFWNVerticalAttackAI(T entity, Animation animation, String sound, float knockback, float range, float arc) {
        super(entity, animation, sound, "", knockback, range, 0, 0);
        this.arc = arc;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        animatingEntity.playSound("mowziesmobs:wroughtnautPreSwing2", 1.5F, 1F);
    }

    @Override
    public void updateTask() {
        if (animatingEntity.getAnimation() == getAnimation()) {
            animatingEntity.motionX = 0;
            animatingEntity.motionZ = 0;
            animatingEntity.rotationYaw = animatingEntity.prevRotationYaw;
            if (animatingEntity.getAnimationTick() < 26 && entityTarget != null) {
                animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
            }

            if (animatingEntity.getAnimationTick() == 6) {
                animatingEntity.playSound("mowziesmobs:wroughtnautCreak", 0.5F, 1F);
            }

            if (animatingEntity.getAnimationTick() == 25) {
                animatingEntity.playSound(attackSound, 1.2F, 1);
            }

            if (animatingEntity.getAnimationTick() == 27) {
                animatingEntity.playSound("mowziesmobs:wroughtnautSwing2", 1.5F, 1F);
                List<EntityLivingBase> entitiesHit = animatingEntity.getEntityLivingBaseNearby(range, 3, range, range);
                float damage = (float) animatingEntity.getAttack();
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
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage(animatingEntity), damage * 1.5F);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                    }
                }
            }
            if (animatingEntity.getAnimationTick() == 28) {
                animatingEntity.playSound("minecraft:random.anvil_land", 1, 0.5F);
            }
            if (animatingEntity.getAnimationTick() == 43) {
                animatingEntity.playSound("mowziesmobs:wroughtnautPull1", 1, 1F);
            }
            if (animatingEntity.getAnimationTick() == 43) {
                animatingEntity.playSound("mowziesmobs:wroughtnautCreak", 0.5F, 1F);
            }
            if (animatingEntity.getAnimationTick() == 72) {
                animatingEntity.playSound("mowziesmobs:wroughtnautPull5", 1, 1F);
            }
            if (animatingEntity.getAnimationTick() == 81) {
                animatingEntity.playSound("mowziesmobs:wroughtnautRelease2", 1, 1F);
            }
            if (animatingEntity.getAnimationTick() > 26 && animatingEntity.getAnimationTick() < 85) {
                ((EntityWroughtnaut) entity).vulnerable = true;
                ((EntityWroughtnaut) entity).rotationYaw = ((EntityWroughtnaut) entity).prevRotationYaw;
                ((EntityWroughtnaut) entity).renderYawOffset = ((EntityWroughtnaut) entity).prevRenderYawOffset;
            } else {
                ((EntityWroughtnaut) entity).vulnerable = false;
            }
        } else {
            ((EntityWroughtnaut) entity).vulnerable = false;
        }
    }
}
