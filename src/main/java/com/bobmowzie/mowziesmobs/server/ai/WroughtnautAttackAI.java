package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

public class WroughtnautAttackAI extends EntityAIBase {
    private final EntityWroughtnaut wroughtnaut;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;

    private int attacksSinceVertical;
    private int timeSinceStomp;

    public WroughtnautAttackAI(EntityWroughtnaut wroughtnaut) {
        this.wroughtnaut = wroughtnaut;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.wroughtnaut.getAttackTarget();
        return target != null && target.isEntityAlive() && this.wroughtnaut.isActive() && this.wroughtnaut.getAnimation() == IAnimatedEntity.NO_ANIMATION;
    }

    @Override
    public void startExecuting() {
        this.repath = 0;
    }

    @Override
    public void resetTask() {
        this.wroughtnaut.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.wroughtnaut.getAttackTarget();
        if (target == null) return;
        double dist = this.wroughtnaut.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        this.wroughtnaut.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
            this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
            target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
            this.wroughtnaut.getNavigator().noPath()
        ) {
            this.targetX = target.posX;
            this.targetY = target.posY;
            this.targetZ = target.posZ;
            this.repath = 4 + this.wroughtnaut.getRNG().nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.wroughtnaut.getNavigator().tryMoveToEntityLiving(target, 0.2D)) {
                this.repath += 15;
            }
        }
        if (target.posY - this.wroughtnaut.posY >= -1 && target.posY - this.wroughtnaut.posY <= 3) {
            boolean couldStomp = dist < 6.0D * 6.0D && this.timeSinceStomp > 600;
            if (dist < 3.5D * 3.5D && Math.abs(MathHelper.wrapDegrees(this.wroughtnaut.getAngleBetweenEntities(target, this.wroughtnaut) - this.wroughtnaut.rotationYaw)) < 35.0D && (!couldStomp || this.wroughtnaut.getRNG().nextFloat() < 0.667F)) {
                if (this.attacksSinceVertical > 3 || this.wroughtnaut.getRNG().nextFloat() < 0.25F) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this.wroughtnaut, EntityWroughtnaut.VERTICAL_ATTACK_ANIMATION);
                    this.attacksSinceVertical = 0;
                } else {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this.wroughtnaut, EntityWroughtnaut.ATTACK_ANIMATION);
                    this.attacksSinceVertical++;
                }
            } else if (couldStomp) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this.wroughtnaut, EntityWroughtnaut.STOMP_ATTACK_ANIMATION);
                this.timeSinceStomp = 0;
                this.attacksSinceVertical++;
            }
        }
        this.timeSinceStomp++;
    }
}
