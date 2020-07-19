package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationFWNAttackAI extends AnimationAI<EntityWroughtnaut> {
    protected float knockback = 1;
    protected float range;
    private float arc;

    public AnimationFWNAttackAI(EntityWroughtnaut entity, float knockback, float range, float arc) {
        super(entity);
        this.knockback = knockback;
        this.range = range;
        this.arc = arc;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == EntityWroughtnaut.ATTACK_ANIMATION || animation == EntityWroughtnaut.ATTACK_TWICE_ANIMATION || animation == EntityWroughtnaut.ATTACK_THRICE_ANIMATION;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        if (entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_1, 1.5F, 1F);
    }

    @Override
    public void resetTask() {
        super.resetTask();
    }

    private boolean shouldFollowUp() {
        EntityLivingBase entityTarget = entity.getAttackTarget();
        if (entityTarget != null && entityTarget.isEntityAlive()) {
            Vec3d targetMoveVec = new Vec3d(entityTarget.motionX, entityTarget.motionY, entityTarget.motionZ);
            Vec3d betweenEntitiesVec = entity.getPositionVector().subtract(entityTarget.getPositionVector());
            boolean targetComingCloser = targetMoveVec.dotProduct(betweenEntitiesVec) > 0.1;
            if (entity.targetDistance < range + 1 || (entity.targetDistance < range + 5 && targetComingCloser)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        EntityLivingBase entityTarget = entity.getAttackTarget();
        entity.motionX = 0;
        entity.motionZ = 0;
        if (entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) {
            if (entity.getAnimationTick() < 23 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.rotationYaw = entity.prevRotationYaw;
            }
            if (entity.getAnimationTick() == 6) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1);
            } else if (entity.getAnimationTick() == 25) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 1.2F, 1);
            } else if (entity.getAnimationTick() == 27) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_1, 1.5F, 1);
                List<EntityLivingBase> entitiesHit = entity.getEntityLivingBaseNearby(range, 3, range, range);
                float damage = (float) entity.getAttack();
                boolean hit = false;
                for (EntityLivingBase entityHit : entitiesHit) {
                    float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - entity.posZ, entityHit.posX - entity.posX) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingAngle = entity.renderYawOffset % 360;
                    if (entityHitAngle < 0) {
                        entityHitAngle += 360;
                    }
                    if (entityAttackingAngle < 0) {
                        entityAttackingAngle += 360;
                    }
                    float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                    float entityHitDistance = (float) Math.sqrt((entityHit.posZ - entity.posZ) * (entityHit.posZ - entity.posZ) + (entityHit.posX - entity.posX) * (entityHit.posX - entity.posX));
                    if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
                        if (entityHit.isActiveItemStackBlocking())
                            entityHit.getActiveItemStack().damageItem(400, entityHit);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                        hit = true;
                    }
                }
                if (hit) {
                    entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 1, 0.5F);
                }
            } else if (entity.getAnimationTick() == 37 && shouldFollowUp() && entity.getHealthRatio() <= 0.9 && entity.getRNG().nextFloat() < 0.5F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityWroughtnaut.ATTACK_TWICE_ANIMATION);
            }
        }
        else if (entity.getAnimation() == EntityWroughtnaut.ATTACK_TWICE_ANIMATION) {
            if (entity.getAnimationTick() < 7 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.rotationYaw = entity.prevRotationYaw;
            }
            if (entity.getAnimationTick() == 7) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 1.2F, 1);
            }
            else if (entity.getAnimationTick() == 9) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_3, 1.5F, 1);
                List<EntityLivingBase> entitiesHit = entity.getEntityLivingBaseNearby(range, 3, range, range);
                float damage = (float) entity.getAttack();
                boolean hit = false;
                for (EntityLivingBase entityHit : entitiesHit) {
                    float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - entity.posZ, entityHit.posX - entity.posX) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingAngle = entity.renderYawOffset % 360;
                    if (entityHitAngle < 0) {
                        entityHitAngle += 360;
                    }
                    if (entityAttackingAngle < 0) {
                        entityAttackingAngle += 360;
                    }
                    float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                    float entityHitDistance = (float) Math.sqrt((entityHit.posZ - entity.posZ) * (entityHit.posZ - entity.posZ) + (entityHit.posX - entity.posX) * (entityHit.posX - entity.posX));
                    if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
                        if (entityHit.isActiveItemStackBlocking())
                            entityHit.getActiveItemStack().damageItem(400, entityHit);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                        hit = true;
                    }
                }
                if (hit) {
                    entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 1, 0.5F);
                }
            } else if (entity.getAnimationTick() == 23 && shouldFollowUp() && entity.swingDirection && entity.getHealthRatio() <= 0.6 && entity.getRNG().nextFloat() < 0.25F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityWroughtnaut.ATTACK_THRICE_ANIMATION);
            }
        }
        else if (entity.getAnimation() == EntityWroughtnaut.ATTACK_THRICE_ANIMATION) {
            if (entity.getAnimationTick() < 22 && entityTarget != null) {
                entity.faceEntity(entityTarget, 30F, 30F);
            } else {
                entity.rotationYaw = entity.prevRotationYaw;
            }
            if (entity.getAnimationTick() == 0) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_3, 1.2F, 1f);
            } else if (entity.getAnimationTick() == 20) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 1.2F, 0.9f);
            } else if (entity.getAnimationTick() == 24) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_GRUNT_3, 1.5F, 1.13f);
                entity.move(MoverType.SELF, Math.cos(Math.toRadians(entity.rotationYaw + 90)), 0, Math.sin(Math.toRadians(entity.rotationYaw + 90)));
                List<EntityLivingBase> entitiesHit = entity.getEntityLivingBaseNearby(range + 0.4, 3, range + 0.4, range + 0.4);
                float damage = (float) entity.getAttack();
                boolean hit = false;
                for (EntityLivingBase entityHit : entitiesHit) {
                    float entityHitDistance = (float) Math.sqrt((entityHit.posZ - entity.posZ) * (entityHit.posZ - entity.posZ) + (entityHit.posX - entity.posX) * (entityHit.posX - entity.posX));
                    if (entityHitDistance <= range + 0.4) {
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
                        if (entityHit.isActiveItemStackBlocking())
                            entityHit.getActiveItemStack().damageItem(400, entityHit);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                        hit = true;
                    }
                }
                if (hit) {
                    entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 1, 0.5F);
                }
            }
        }
    }
}
