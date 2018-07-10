package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class MMAIAvoidEntity<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    /** The entity we are attached to */
    protected EntityCreature entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    /** The PathEntity of our entity */
    private Path entityPathEntity;
    /** The PathNavigate of our entity */
    private final PathNavigate entityPathNavigate;
    /** Class of entity this behavior seeks to avoid */
    private final Class<T> classToAvoid;
    private final Predicate <? super T > avoidTargetSelector;
    private int numChecks;
    private int distanceXZ;
    private int distanceY;

    public MMAIAvoidEntity(EntityCreature entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this(entityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn, 10, 12, 7);
    }

    public MMAIAvoidEntity(EntityCreature entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, int numChecks, int distanceXZ, int distanceY)
    {
        this(entityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn, numChecks, distanceXZ, distanceY);
    }

    public MMAIAvoidEntity(EntityCreature entityIn, Class<T> classToAvoidIn, Predicate <? super T > avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn, int numChecks, int distanceXZ, int distanceY)
    {
        this.canBeSeenSelector = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_.isEntityAlive() && MMAIAvoidEntity.this.entity.getEntitySenses().canSee(p_apply_1_) && !MMAIAvoidEntity.this.entity.isOnSameTeam(p_apply_1_);
            }
        };
        this.entity = entityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = entityIn.getNavigator();
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<T> list = this.entity.world.<T>getEntitiesWithinAABB(this.classToAvoid, this.entity.getEntityBoundingBox().grow((double)this.avoidDistance, 3.0D, (double)this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = null;
            int i = 0;
            while (i < numChecks) {
                vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, distanceXZ, distanceY, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

                if (vec3d == null) {

                } else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSqToEntity(this.entity)) {

                } else {
                    this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                    if (entityPathEntity != null) return true;
                }
                i++;
            }
            if (entity instanceof EntityGrottol && ((EntityGrottol) entity).ticksExisted > 20 && ((EntityGrottol) entity).getAnimation() == EntityGrottol.NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage((EntityGrottol)entity, EntityGrottol.BURROW_ANIMATION);
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.entityPathNavigate.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.closestLivingEntity = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (this.entity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D)
        {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        }
        else
        {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}