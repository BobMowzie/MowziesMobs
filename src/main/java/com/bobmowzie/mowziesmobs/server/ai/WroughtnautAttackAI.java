package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.util.Mth;

import java.util.EnumSet;

public class WroughtnautAttackAI extends Goal {
    private final EntityWroughtnaut wroughtnaut;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;

    private int attacksSinceVertical;
    private int timeSinceStomp;

    public WroughtnautAttackAI(EntityWroughtnaut wroughtnaut) {
        this.wroughtnaut = wroughtnaut;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity target = this.wroughtnaut.getTarget();
        return target != null && target.isAlive() && this.wroughtnaut.isActive() && this.wroughtnaut.getAnimation() == IAnimatedEntity.NO_ANIMATION;
    }

    @Override
    public void startExecuting() {
        this.repath = 0;
    }

    @Override
    public void resetTask() {
        this.wroughtnaut.getNavigation().clearPath();
    }

    @Override
    public void tick() {
        LivingEntity target = this.wroughtnaut.getTarget();
        if (target == null) return;
        double dist = this.wroughtnaut.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        this.wroughtnaut.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
            this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
            target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
            this.wroughtnaut.getNavigation().isDone()
        ) {
            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.repath = 4 + this.wroughtnaut.getRNG().nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.wroughtnaut.getNavigation().tryMoveToEntityLiving(target, 0.2D)) {
                this.repath += 15;
            }
        }
        dist = this.wroughtnaut.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        if (target.getY() - this.wroughtnaut.getY() >= -1 && target.getY() - this.wroughtnaut.getY() <= 3) {
            boolean couldStomp = dist < 6.0D * 6.0D && this.timeSinceStomp > 200;
            if (dist < 3.5D * 3.5D && Math.abs(Mth.wrapDegrees(this.wroughtnaut.getAngleBetweenEntities(target, this.wroughtnaut) - this.wroughtnaut.getYRot())) < 35.0D && (!couldStomp || this.wroughtnaut.getRNG().nextFloat() < 0.667F)) {
                if (this.attacksSinceVertical > 3 + 2 * (1 - wroughtnaut.getHealthRatio()) || this.wroughtnaut.getRNG().nextFloat() < 0.18F) {
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
