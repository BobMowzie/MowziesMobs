package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public final class EntityAIAvoidEntity<T extends Entity> extends Goal {
    private final PathfinderMob entity;

    private final Class<T> avoidClass;

    private final float distance;

    private final Predicate<T> predicate;

    private final double speed;

    private final PathNavigation navigator;

    private T avoiding;

    private Path path;

    public EntityAIAvoidEntity(PathfinderMob entity, Class<T> avoidClass, float distance, double speed) {
        this(entity, avoidClass, e -> true, distance, speed);
    }

    public EntityAIAvoidEntity(PathfinderMob entity, Class<T> avoidClass, Predicate<? super T> predicate, float distance, double speed) {
        this.entity = entity;
        this.avoidClass = avoidClass;
        this.distance = distance;
        Predicate<T> visible = e -> e.isAlive() && entity.getSensing().hasLineOfSight(e);
        Predicate<T> targetable = e -> !(e instanceof Player) || !e.isSpectator() && !((Player)e).isCreative();
        this.predicate = targetable.and(predicate).and(visible);
        this.speed = speed;
        navigator = entity.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<T> entities = entity.level.getEntitiesOfClass(avoidClass, entity.getBoundingBox().inflate(distance, 3, distance), predicate);
        if (entities.isEmpty()) {
            return false;
        }
        avoiding = entities.get(entity.getRandom().nextInt(entities.size()));
        Vec3 pos = DefaultRandomPos.getPosAway(entity, (int) (distance + 1), (int) (distance / 2 + 1), new Vec3(avoiding.getX(), avoiding.getY(), avoiding.getZ()));
        if (pos == null) {
            return false;
        }
        if (avoiding.distanceToSqr(pos.x, pos.y, pos.z) < avoiding.distanceToSqr(entity)) {
            return false;
        }
        path = navigator.createPath(new BlockPos(pos), 0);
        return path != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !navigator.isDone();
    }

    @Override
    public void start() {
        navigator.moveTo(path, speed);
    }

    @Override
    public void stop() {
        avoiding = null;
    }

    @Override
    public void tick() {
        entity.getNavigation().setSpeedModifier(speed);
    }
}