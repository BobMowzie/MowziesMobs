package com.bobmowzie.mowziesmobs.server.ai.animation;

import java.util.List;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

public class AnimationFWNVerticalAttackAI extends AnimationAttackAI<EntityWroughtnaut> {
    private float arc;

    public AnimationFWNVerticalAttackAI(EntityWroughtnaut entity, Animation animation, SoundEvent sound, float knockback, float range, float arc) {
        super(entity, animation, sound, null, knockback, range, 0, 0);
        this.arc = arc;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_2, 1.5F, 1F);
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
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1F);
            } else if (animatingEntity.getAnimationTick() == 25) {
                animatingEntity.playSound(attackSound, 1.2F, 1);
            } else if (animatingEntity.getAnimationTick() == 27) {
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_SWING_2, 1.5F, 1F);
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
            } else if (animatingEntity.getAnimationTick() == 28) {
                animatingEntity.playSound(SoundEvents.BLOCK_ANVIL_LAND, 1, 0.5F);
            } else if (animatingEntity.getAnimationTick() == 43) {
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_PULL_1, 1, 1F);
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1F);
            } else if (animatingEntity.getAnimationTick() == 72) {
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_PULL_5, 1, 1F);
            } else if (animatingEntity.getAnimationTick() == 81) {
                animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_RELEASE_2, 1, 1F);
            }
            if (animatingEntity.getAnimationTick() > 26 && animatingEntity.getAnimationTick() < 85) {
                entity.vulnerable = true;
                entity.rotationYaw = entity.prevRotationYaw;
                entity.renderYawOffset = entity.prevRenderYawOffset;
            } else {
                entity.vulnerable = false;
            }
        } else {
            entity.vulnerable = false;
        }
    }
}
