package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class EntityAIAvoidEntity<T extends Entity> extends Goal {
    private CreatureEntity entity;

    private final Class<T> avoidClass;

    private final float distance;

    private final Predicate<T> predicate;

    private final double speed;

    private final PathNavigator navigator;

    private T avoiding;

    private Path path;

    public EntityAIAvoidEntity(CreatureEntity entity, Class<T> avoidClass, float distance, double speed) {
        this(entity, avoidClass, Predicates.<T> alwaysTrue(), distance, speed);
    }

    public EntityAIAvoidEntity(CreatureEntity entity, Class<T> avoidClass, Predicate<? super T> predicate, float distance, double speed) {
        this.entity = entity;
        this.avoidClass = avoidClass;
        this.distance = distance;
        Predicate<Entity> visible = new Predicate<Entity>() {
            public boolean apply(Entity e) {
                return e.isEntityAlive() && entity.getEntitySenses().canSee(e);
            }
        };
        this.predicate = Predicates.<T>and(EntityPredicates.CAN_AI_TARGET, visible, predicate);
        this.speed = speed;
        navigator = entity.getNavigator();
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        List<T> entities = entity.world.getEntitiesWithinAABB(avoidClass, entity.getBoundingBox().grow(distance, 3, distance), predicate);
        if (entities.isEmpty()) {
            return false;
        }
        avoiding = entities.get(entity.getRNG().nextInt(entities.size()));
        Vec3d pos = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, (int) (distance + 1), (int) (distance / 2 + 1), new Vec3d(avoiding.posX, avoiding.posY, avoiding.posZ));
        if (pos == null) {
            return false;
        }
        if (avoiding.getDistanceSq(pos.x, pos.y, pos.z) < avoiding.getDistanceSq(entity)) {
            return false;
        }
        path = navigator.getPathToXYZ(pos.x, pos.y, pos.z);
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
    public void updateTask() {
        entity.getNavigator().setSpeed(speed);
    }
}