package com.bobmowzie.mowziesmobs.server.ai.animation;

import java.util.List;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

public class AnimationFWNAttackAI extends AnimationAttackAI<EntityWroughtnaut> {
    private float arc;

    public AnimationFWNAttackAI(EntityWroughtnaut entity, Animation animation, SoundEvent sound, float knockback, float range, float arc) {
        super(entity, animation, sound, null, knockback, range, 0, 0);
        this.arc = arc;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_1, 1.5F, 1F);
    }

    @Override
    public void updateTask() {
        animatingEntity.motionX = 0;
        animatingEntity.motionZ = 0;
        if (animatingEntity.getAnimationTick() < (getAnimation().getDuration() / 2 + 2) && entityTarget != null) {
            animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30, 30);
        }
        if (animatingEntity.getAnimationTick() == 6) {
            animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1);
        } else if (animatingEntity.getAnimationTick() == (getAnimation().getDuration() / 2)) {
            animatingEntity.playSound(attackSound, 1.2F, 1);
        } else if (animatingEntity.getAnimationTick() == (getAnimation().getDuration() / 2 + 2)) {
            animatingEntity.playSound(MMSounds.ENTITY_WROUGHT_SWING_1, 1.5F, 1);
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
                animatingEntity.playSound(SoundEvents.BLOCK_ANVIL_LAND, 1, 0.5F);
            }
        }
    }
}
