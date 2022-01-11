package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.world.entity.CreatureEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.RandomPositionGenerator;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

// Copied from AvoidEntityGoal
public class AvoidProjectilesGoal extends Goal {
    protected final CreatureEntity entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected ProjectileEntity avoidTarget;
    protected final float avoidDistance;
    protected Path path;
    protected Vector3d dodgeVec;
    protected final PathNavigator navigation;
    /** Class of entity this behavior seeks to avoid */
    protected final Class<ProjectileEntity> classToAvoid;
    protected final Predicate<ProjectileEntity> avoidTargetSelector;
    private int dodgeTimer = 0;

    public AvoidProjectilesGoal(CreatureEntity entityIn, Class<ProjectileEntity> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(entityIn, classToAvoidIn, (entity) -> {
            return true;
        }, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public AvoidProjectilesGoal(CreatureEntity entityIn, Class<ProjectileEntity> avoidClass, Predicate<ProjectileEntity> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
        this.entity = entityIn;
        this.classToAvoid = avoidClass;
        this.avoidTargetSelector = targetPredicate.and(target -> {
            Vector3d aActualMotion = new Vector3d(target.getPosX() - target.prevPosX, target.getPosY() - target.prevPosY, target.getPosZ() - target.prevPosZ);
            if (aActualMotion.length() < 0.1 || target.ticksExisted < 0) {
                return false;
            }
            if (!entity.getEntitySenses().canSee(target)) return false;
            float dot = (float) target.getMotion().normalize().dotProduct(entity.getPositionVec().subtract(target.getPositionVec()).normalize());
            return !(dot < 0.8);
        });
        this.avoidDistance = distance;
        this.farSpeed = nearSpeedIn;
        this.nearSpeed = farSpeedIn;
        this.navigation = entityIn.getNavigator();
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        if (dodgeTimer > 0) return false;
        this.avoidTarget = this.getMostMovingTowardsMeEntity(this.classToAvoid, this.avoidTargetSelector, this.entity, this.entity.getBoundingBox().grow((double)this.avoidDistance, 3.0D, (double)this.avoidDistance));
        if (this.avoidTarget == null) {
            return false;
        } else {
            Vector3d projectilePos = guessProjectileDestination(this.avoidTarget);
//            Vector3d vector3d = entity.getPositionVec().subtract(projectilePos);
//            entity.setMotion(entity.getMotion().add(vector3d.normalize().scale(1.0)));

            dodgeVec = avoidTarget.getMotion().crossProduct(new Vector3d(0, 1, 0)).normalize().scale(1);
            Vector3d newPosLeft = entity.getPositionVec().add(dodgeVec);
            Vector3d newPosRight = entity.getPositionVec().add(dodgeVec.scale(-1));
            Vector3d diffLeft = newPosLeft.subtract(projectilePos);
            Vector3d diffRight = newPosRight.subtract(projectilePos);
            if (diffRight.lengthSquared() > diffLeft.lengthSquared()) {
                dodgeVec = dodgeVec.scale(-1);
            }
            Vector3d dodgeDest = diffRight.lengthSquared() > diffLeft.lengthSquared() ? newPosRight : newPosLeft;
            Vector3d vector3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 5, 3, dodgeDest);
            if (vector3d == null) {
                this.path = null;
                return true;
            } else if (projectilePos.subtract(vector3d).lengthSquared() < projectilePos.subtract(entity.getPositionVec()).lengthSquared()) {
                return false;
            } else {
                this.path = this.navigation.pathfind(vector3d.x, vector3d.y, vector3d.z, 0);
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.navigation.noPath() || dodgeTimer < 10;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        if (path != null) {
            this.navigation.setPath(this.path, this.farSpeed);
            dodgeVec = this.path.getPosition(entity).subtract(entity.getPositionVec()).normalize().scale(1);
        }
        entity.setMotion(entity.getMotion().add(dodgeVec));
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.avoidTarget = null;
        dodgeTimer = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.entity.getDistanceSq(this.avoidTarget) < 49.0D) {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        } else {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }
        dodgeTimer++;

    }

    private Vector3d guessProjectileDestination(ProjectileEntity projectile) {
        Vector3d vector3d = projectile.getPositionVec();
        Vector3d vector3d1 = vector3d.add(projectile.getMotion().scale(50));
        return entity.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, projectile)).getHitVec();
    }

    @Nullable
    private <T extends ProjectileEntity> T getMostMovingTowardsMeEntity(Class<? extends T> entityClazz, Predicate<? super T> predicate, LivingEntity entity, AxisAlignedBB p_225318_10_) {
        return this.getMostMovingTowardsMeEntityFromList(entity.world.getLoadedEntitiesWithinAABB(entityClazz, p_225318_10_, predicate), entity);
    }

    private <T extends ProjectileEntity> T getMostMovingTowardsMeEntityFromList(List<? extends T> entities, LivingEntity target) {
        double d0 = -2.0D;
        T t = null;

        for(T t1 : entities) {
            double d1 = t1.getMotion().normalize().dotProduct(target.getPositionVec().subtract(t1.getPositionVec()).normalize());;
            if (d1 > d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }
}
