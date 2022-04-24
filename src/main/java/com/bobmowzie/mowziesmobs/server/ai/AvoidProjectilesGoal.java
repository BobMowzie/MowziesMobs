package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.RandomPositionGenerator;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.RayTraceContext;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

// Copied from AvoidEntityGoal
public class AvoidProjectilesGoal extends Goal {
    protected final PathfinderMob entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected Projectile avoidTarget;
    protected final float avoidDistance;
    protected Path path;
    protected Vec3 dodgeVec;
    protected final PathNavigation navigation;
    /** Class of entity this behavior seeks to avoid */
    protected final Class<Projectile> classToAvoid;
    protected final Predicate<Projectile> avoidTargetSelector;
    private int dodgeTimer = 0;

    public AvoidProjectilesGoal(PathfinderMob entityIn, Class<Projectile> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(entityIn, classToAvoidIn, (entity) -> {
            return true;
        }, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public AvoidProjectilesGoal(PathfinderMob entityIn, Class<Projectile> avoidClass, Predicate<Projectile> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
        this.entity = entityIn;
        this.classToAvoid = avoidClass;
        this.avoidTargetSelector = targetPredicate.and(target -> {
            Vec3 aActualMotion = new Vec3(target.getX() - target.xo, target.getY() - target.prevPosY, target.getZ() - target.zo);
            if (aActualMotion.length() < 0.1 || target.tickCount < 0) {
                return false;
            }
            if (!entity.getEntitySenses().canSee(target)) return false;
            float dot = (float) target.getDeltaMovement().normalize().dotProduct(entity.position().subtract(target.position()).normalize());
            return !(dot < 0.8);
        });
        this.avoidDistance = distance;
        this.farSpeed = nearSpeedIn;
        this.nearSpeed = farSpeedIn;
        this.navigation = entityIn.getNavigation();
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
            Vec3 projectilePos = guessProjectileDestination(this.avoidTarget);
//            Vec3 vector3d = entity.position().subtract(projectilePos);
//            entity.setDeltaMovement(entity.getDeltaMovement().add(vector3d.normalize().scale(1.0)));

            dodgeVec = avoidTarget.getDeltaMovement().crossProduct(new Vec3(0, 1, 0)).normalize().scale(1);
            Vec3 newPosLeft = entity.position().add(dodgeVec);
            Vec3 newPosRight = entity.position().add(dodgeVec.scale(-1));
            Vec3 diffLeft = newPosLeft.subtract(projectilePos);
            Vec3 diffRight = newPosRight.subtract(projectilePos);
            if (diffRight.lengthSquared() > diffLeft.lengthSquared()) {
                dodgeVec = dodgeVec.scale(-1);
            }
            Vec3 dodgeDest = diffRight.lengthSquared() > diffLeft.lengthSquared() ? newPosRight : newPosLeft;
            Vec3 vector3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 5, 3, dodgeDest);
            if (vector3d == null) {
                this.path = null;
                return true;
            } else if (projectilePos.subtract(vector3d).lengthSquared() < projectilePos.subtract(entity.position()).lengthSquared()) {
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
            dodgeVec = this.path.getPosition(entity).subtract(entity.position()).normalize().scale(1);
        }
        entity.setDeltaMovement(entity.getDeltaMovement().add(dodgeVec));
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
            this.entity.getNavigation().setSpeed(this.nearSpeed);
        } else {
            this.entity.getNavigation().setSpeed(this.farSpeed);
        }
        dodgeTimer++;

    }

    private Vec3 guessProjectileDestination(Projectile projectile) {
        Vec3 vector3d = projectile.position();
        Vec3 vector3d1 = vector3d.add(projectile.getDeltaMovement().scale(50));
        return entity.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, projectile)).getHitVec();
    }

    @Nullable
    private <T extends Projectile> T getMostMovingTowardsMeEntity(Class<? extends T> entityClazz, Predicate<? super T> predicate, LivingEntity entity, AxisAlignedBB p_225318_10_) {
        return this.getMostMovingTowardsMeEntityFromList(entity.world.getLoadedEntitiesWithinAABB(entityClazz, p_225318_10_, predicate), entity);
    }

    private <T extends Projectile> T getMostMovingTowardsMeEntityFromList(List<? extends T> entities, LivingEntity target) {
        double d0 = -2.0D;
        T t = null;

        for(T t1 : entities) {
            double d1 = t1.getDeltaMovement().normalize().dotProduct(target.position().subtract(t1.position()).normalize());;
            if (d1 > d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }
}
