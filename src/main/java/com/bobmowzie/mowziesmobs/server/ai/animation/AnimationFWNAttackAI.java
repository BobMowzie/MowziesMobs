package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

import javax.management.Attribute;
import java.util.List;

public class AnimationFWNAttackAI extends AnimationAI<EntityWroughtnaut> {
    protected float applyKnockback = 1;
    protected float range;
    private final float arc;

    public AnimationFWNAttackAI(EntityWroughtnaut entity, float applyKnockback, float range, float arc) {
        super(entity);
        this.applyKnockback = applyKnockback;
        this.range = range;
        this.arc = arc;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == EntityWroughtnaut.ATTACK_ANIMATION || animation == EntityWroughtnaut.ATTACK_TWICE_ANIMATION || animation == EntityWroughtnaut.ATTACK_THRICE_ANIMATION;
    }

    @Override
    public void start() {
        super.start();
        if (entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_1.get(), 1.5F, 1F);
    }

    @Override
    public void stop() {
        super.stop();
    }

    private boolean shouldFollowUp(float bonusRange) {
        LivingEntity entityTarget = entity.getTarget();
        if (entityTarget != null && entityTarget.isAlive()) {
            Vec3 targetMoveVec = entityTarget.getDeltaMovement();
            Vec3 betweenEntitiesVec = entity.position().subtract(entityTarget.position());
            boolean targetComingCloser = targetMoveVec.dot(betweenEntitiesVec) > 0;
            return entity.targetDistance < range + bonusRange || (entity.targetDistance < range + 5 + bonusRange && targetComingCloser);
        }
        return false;
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = entity.getTarget();
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
        if (entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) {
            if (entity.getAnimationTick() < 23 && entityTarget != null) {
                entity.lookAt(entityTarget, 30F, 30F);
            } else {
                entity.yRot = entity.yRotO;
            }
            if (entity.getAnimationTick() == 6) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK.get(), 0.5F, 1);
            } else if (entity.getAnimationTick() == 25) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 1.2F, 1);
            } else if (entity.getAnimationTick() == 27) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_1.get(), 1.5F, 1);
                List<LivingEntity> entitiesHit = entity.getEntityLivingBaseNearby(range, 3, range, range);
                float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                boolean hit = false;
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
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                    if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                        entityHit.hurt(DamageSource.mobAttack(entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                        entityHit.setDeltaMovement(entityHit.getDeltaMovement().x * applyKnockback, entityHit.getDeltaMovement().y, entityHit.getDeltaMovement().z * applyKnockback);
                        hit = true;
                    }
                }
                if (hit) {
                    entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT.get(), 1, 0.5F);
                }
            } else if (entity.getAnimationTick() == 37 && shouldFollowUp(2.5f) && entity.getHealthRatio() <= 0.9 && entity.getRandom().nextFloat() < 0.6F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityWroughtnaut.ATTACK_TWICE_ANIMATION);
            }
        }
        else if (entity.getAnimation() == EntityWroughtnaut.ATTACK_TWICE_ANIMATION) {
            if (entity.getAnimationTick() < 7 && entityTarget != null) {
                entity.lookAt(entityTarget, 30F, 30F);
            } else {
                entity.yRot = entity.yRotO;
            }
            if (entity.getAnimationTick() == 10) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 1.2F, 1);
            }
            else if (entity.getAnimationTick() == 12) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_3.get(), 1.5F, 1);
                List<LivingEntity> entitiesHit = entity.getEntityLivingBaseNearby(range - 0.3, 3, range - 0.3, range - 0.3);
                float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                boolean hit = false;
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
                    if (entityHitDistance <= range - 0.3 && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                        entityHit.hurt(DamageSource.mobAttack(entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                        entityHit.setDeltaMovement(entityHit.getDeltaMovement().x * applyKnockback, entityHit.getDeltaMovement().y, entityHit.getDeltaMovement().z * applyKnockback);
                        hit = true;
                    }
                }
                if (hit) {
                    entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT.get(), 1, 0.5F);
                }
            } else if (entity.getAnimationTick() == 23 && shouldFollowUp(3.5f) && entity.getHealthRatio() <= 0.6 && entity.getRandom().nextFloat() < 0.6f) {
                AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityWroughtnaut.ATTACK_THRICE_ANIMATION);
            }
        }
        else if (entity.getAnimation() == EntityWroughtnaut.ATTACK_THRICE_ANIMATION) {
            if (entity.getAnimationTick() == 1) entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_3.get(), 1.2F, 1f);
            if (entity.getAnimationTick() < 22 && entityTarget != null) {
                entity.lookAt(entityTarget, 30F, 30F);
            } else {
                entity.yRot = entity.yRotO;
            }
            if (entity.getAnimationTick() == 20) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH.get(), 1.2F, 0.9f);
            } else if (entity.getAnimationTick() == 24) {
                entity.playSound(MMSounds.ENTITY_WROUGHT_GRUNT_3.get(), 1.5F, 1.13f);
                entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.yRot + 90)), 0, Math.sin(Math.toRadians(entity.yRot + 90))));
                List<LivingEntity> entitiesHit = entity.getEntityLivingBaseNearby(range + 0.2, 3, range + 0.2, range + 0.2);
                float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                boolean hit = false;
                for (LivingEntity entityHit : entitiesHit) {
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX()));
                    if (entityHitDistance <= range + 0.2) {
                        entityHit.hurt(DamageSource.mobAttack(entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                        entityHit.setDeltaMovement(entityHit.getDeltaMovement().x * applyKnockback, entityHit.getDeltaMovement().y, entityHit.getDeltaMovement().z * applyKnockback);
                        hit = true;
                    }
                }
                if (hit) {
                    entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT.get(), 1, 0.5F);
                }
            }
        }
    }
}
