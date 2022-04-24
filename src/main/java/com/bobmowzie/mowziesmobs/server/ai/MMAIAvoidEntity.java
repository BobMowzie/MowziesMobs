package com.bobmowzie.mowziesmobs.server.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.sounds.EntityPredicates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class MMAIAvoidEntity<U extends PathfinderMob, T extends Entity> extends Goal {
    private static final double NEAR_DISTANCE = 7.0D;
    
    protected final U entity;

    private final Predicate<T> selector;

    private final double farSpeed;

    private final double nearSpeed;

    private final float evadeDistance;

    private final Class<T> avoidedEntityType;

    private final int horizontalEvasion;

    private final int verticalEvasion;

    private final int numChecks;

    private T entityEvading;

    private Path entityPathEntity;

    public MMAIAvoidEntity(U entity, Class<T> avoidedEntityType, float evadeDistance, double farSpeed, double nearSpeed) {
        this(entity, avoidedEntityType, Predicates.alwaysTrue(), evadeDistance, farSpeed, nearSpeed, 10, 12, 7);
    }

    public MMAIAvoidEntity(U entity, Class<T> avoidedEntityType, float evadeDistance, double farSpeed, double nearSpeed, int numChecks, int horizontalEvasion, int verticalEvasion) {
        this(entity, avoidedEntityType, Predicates.alwaysTrue(), evadeDistance, farSpeed, nearSpeed, numChecks, horizontalEvasion, verticalEvasion);
    }

    public MMAIAvoidEntity(U entity, Class<T> avoidedEntityType, Predicate<? super T> predicate, float evadeDistance, double farSpeed, double nearSpeed, int numChecks, int horizontalEvasion, int verticalEvasion) {
        this.entity = entity;
        this.selector = e -> e != null &&
            EntityPredicates.CAN_AI_TARGET.test(e) &&
            e.isAlive() &&
            entity.getEntitySenses().canSee(e) &&
            !entity.isOnSameTeam(e) &&
            predicate.test(e);
        this.avoidedEntityType = avoidedEntityType;
        this.evadeDistance = evadeDistance;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.numChecks = numChecks;
        this.horizontalEvasion = horizontalEvasion;
        this.verticalEvasion = verticalEvasion;
        setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        List<T> entities = entity.world.getEntitiesWithinAABB(avoidedEntityType, entity.getBoundingBox().grow(evadeDistance, 3.0D, evadeDistance), selector);
        if (entities.isEmpty()) {
            onSafe();
            return false;
        }
        entityEvading = entities.get(0);
        for (int n = 0; n < numChecks; n++) {
            Vec3 pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, horizontalEvasion, verticalEvasion, entityEvading.position());
            if (pos != null && !(entityEvading.getDistanceSq(pos.x, pos.y, pos.z) < entityEvading.getDistanceSq(entity))) {
                entityPathEntity = entity.getNavigation().getPathToPos(new BlockPos(pos), 0);
                if (entityPathEntity != null) {
                    return true;
                }
            }
        }
        onPathNotFound();
        return false;
    }

    protected void onSafe() {}

    protected void onPathNotFound() {}

    @Override
    public boolean shouldContinueExecuting() {
        return !entity.getNavigation().isDone();
    }

    @Override
    public void startExecuting() {
        entity.getNavigation().setPath(entityPathEntity, farSpeed);
    }

    @Override
    public void resetTask() {
        entityEvading = null;
    }

    @Override
    public void tick() {
        entity.getNavigation().setSpeed(entity.getDistanceSq(entityEvading) < NEAR_DISTANCE * NEAR_DISTANCE ? nearSpeed : farSpeed);
    }
}