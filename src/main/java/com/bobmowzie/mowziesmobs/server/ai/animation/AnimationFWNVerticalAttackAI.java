package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;

import java.util.List;

public class AnimationFWNVerticalAttackAI extends AnimationAttackAI<EntityWroughtnaut> {
    private final float arc;

    public AnimationFWNVerticalAttackAI(EntityWroughtnaut entity, Animation animation, SoundEvent sound, float applyKnockback, float range, float arc) {
        super(entity, animation, sound, null, applyKnockback, range, 0, 0);
        this.arc = arc;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_2.get(), 1.5F, 1F);
    }

    @Override
    public void tick() {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        if (entity.getAnimationTick() < 21 && entityTarget != null) {
            entity.faceEntity(entityTarget, 30F, 30F);
        }
        else {
            entity.getYRot() = entity.yRotO;
        }

        if (entity.getAnimationTick() == 6) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK.get(), 0.5F, 1F);
        } else if (entity.getAnimationTick() == 25) {
            entity.playSound(attackSound, 1.2F, 1);
        } else if (entity.getAnimationTick() == 27) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_2.get(), 1.5F, 1F);
            List<LivingEntity> entitiesHit = entity.getEntityLivingBaseNearby(range, 3, range, range);
            float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.combatConfig.attackMultiplier.get().floatValue();
            for (LivingEntity entityHit : entitiesHit) {
                float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - entity.getZ(), entityHit.getX() - entity.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = entity.yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX()));
                if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                    entityHit.hurt(DamageSource.causeMobDamage(entity), damage * 1.5F);
                    if (entityHit.isActiveItemStackBlocking()) entityHit.getActiveItemStack().damageItem(400, entityHit, player -> player.sendBreakAnimation(entityHit.getActiveHand()));
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().x * applyKnockbackMultiplier, entityHit.getDeltaMovement().y, entityHit.getDeltaMovement().z * applyKnockbackMultiplier);
                }
            }
        } else if (entity.getAnimationTick() == 28) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_LAND.get(), 1, 0.5F);
            EntityCameraShake.cameraShake(entity.world, entity.position(), 20, 0.3f, 0, 10);
        } else if (entity.getAnimationTick() == 44) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_PULL_1.get(), 1, 1F);
            entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK.get(), 0.5F, 1F);
        } else if (entity.getAnimationTick() == 75) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_PULL_5.get(), 1, 1F);
        } else if (entity.getAnimationTick() == 83) {
            entity.playSound(MMSounds.ENTITY_WROUGHT_RELEASE_2.get(), 1, 1F);
        }
        if (entity.getAnimationTick() > 26 && entity.getAnimationTick() < 85) {
            entity.vulnerable = true;
            entity.getYRot() = entity.yRotO;
            entity.yBodyRot = entity.yBodyRotO;
        } else {
            entity.vulnerable = false;
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        entity.vulnerable = false;
    }
}
