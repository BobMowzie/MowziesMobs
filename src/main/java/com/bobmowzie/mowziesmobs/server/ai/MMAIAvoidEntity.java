package com.bobmowzie.mowziesmobs.server.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

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
            EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e) &&
            e.isAlive() &&
            entity.getSensing().canSee(e) &&
            !entity.isAlliedTo(e) &&
            predicate.test(e);
        this.avoidedEntityType = avoidedEntityType;
        this.evadeDistance = evadeDistance;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.numChecks = numChecks;
        this.horizontalEvasion = horizontalEvasion;
        this.verticalEvasion = verticalEvasion;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<T> entities = entity.level.getEntitiesOfClass(avoidedEntityType, entity.getBoundingBox().inflate(evadeDistance, 3.0D, evadeDistance), selector);
        if (entities.isEmpty()) {
            onSafe();
            return false;
        }
        entityEvading = entities.get(0);
        for (int n = 0; n < numChecks; n++) {
            Vec3 pos = RandomPos.getPosAvoid(entity, horizontalEvasion, verticalEvasion, entityEvading.position());
            if (pos != null && !(entityEvading.distanceToSqr(pos.x, pos.y, pos.z) < entityEvading.distanceToSqr(entity))) {
                entityPathEntity = entity.getNavigation().createPath(new BlockPos(pos), 0);
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
    public boolean canContinueToUse() {
        return !entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        entity.getNavigation().moveTo(entityPathEntity, farSpeed);
    }

    @Override
    public void stop() {
        entityEvading = null;
    }

    @Override
    public void tick() {
        entity.getNavigation().setSpeedModifier(entity.distanceToSqr(entityEvading) < NEAR_DISTANCE * NEAR_DISTANCE ? nearSpeed : farSpeed);
    }
}