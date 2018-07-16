package com.bobmowzie.mowziesmobs.server.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class MMAIAvoidEntity<U extends EntityCreature, T extends Entity> extends EntityAIBase {
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
            EntitySelectors.CAN_AI_TARGET.test(e) &&
            e.isEntityAlive() &&
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
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        List<T> entities = entity.world.getEntitiesWithinAABB(avoidedEntityType, entity.getEntityBoundingBox().grow(evadeDistance, 3.0D, evadeDistance), selector);
        if (entities.isEmpty()) {
            onSafe();
            return false;
        }
        entityEvading = entities.get(0);
        for (int n = 0; n < numChecks; n++) {
            Vec3d pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, horizontalEvasion, verticalEvasion, entityEvading.getPositionVector());
            if (pos != null && !(entityEvading.getDistanceSq(pos.x, pos.y, pos.z) < entityEvading.getDistanceSqToEntity(entity))) {
                entityPathEntity = entity.getNavigator().getPathToXYZ(pos.x, pos.y, pos.z);
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
        return !entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().setPath(entityPathEntity, farSpeed);
    }

    @Override
    public void resetTask() {
        entityEvading = null;
    }

    @Override
    public void updateTask() {
        entity.getNavigator().setSpeed(entity.getDistanceSqToEntity(entityEvading) < NEAR_DISTANCE * NEAR_DISTANCE ? nearSpeed : farSpeed);
    }
}