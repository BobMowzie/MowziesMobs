package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public final class EntityAIAvoidEntity<T extends Entity> extends Goal {
    private final CreatureEntity entity;

    private final Class<T> avoidClass;

    private final float distance;

    private final Predicate<T> predicate;

    private final double speed;

    private final PathNavigator navigator;

    private T avoiding;

    private Path path;

    public EntityAIAvoidEntity(CreatureEntity entity, Class<T> avoidClass, float distance, double speed) {
        this(entity, avoidClass, e -> true, distance, speed);
    }

    public EntityAIAvoidEntity(CreatureEntity entity, Class<T> avoidClass, Predicate<? super T> predicate, float distance, double speed) {
        this.entity = entity;
        this.avoidClass = avoidClass;
        this.distance = distance;
        Predicate<T> visible = e -> e.isAlive() && entity.getEntitySenses().canSee(e);
        Predicate<T> targetable = e -> !(e instanceof PlayerEntity) || !e.isSpectator() && !((PlayerEntity)e).isCreative();
        this.predicate = targetable.and(predicate).and(visible);
        this.speed = speed;
        navigator = entity.getNavigator();
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        List<T> entities = entity.world.getEntitiesWithinAABB(avoidClass, entity.getBoundingBox().grow(distance, 3, distance), predicate);
        if (entities.isEmpty()) {
            return false;
        }
        avoiding = entities.get(entity.getRNG().nextInt(entities.size()));
        Vector3d pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, (int) (distance + 1), (int) (distance / 2 + 1), new Vector3d(avoiding.getPosX(), avoiding.getPosY(), avoiding.getPosZ()));
        if (pos == null) {
            return false;
        }
        if (avoiding.getDistanceSq(pos.x, pos.y, pos.z) < avoiding.getDistanceSq(entity)) {
            return false;
        }
        path = navigator.getPathToPos(new BlockPos(pos), 0);
        return path != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !navigator.noPath();
    }

    @Override
    public void startExecuting() {
        navigator.setPath(path, speed);
    }

    @Override
    public void resetTask() {
        avoiding = null;
    }

    @Override
    public void tick() {
        entity.getNavigator().setSpeed(speed);
    }
}