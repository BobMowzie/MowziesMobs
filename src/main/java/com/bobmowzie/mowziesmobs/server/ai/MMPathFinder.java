package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class MMPathFinder extends PathFinder {
    private final PathHeap path = new PathHeap();
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;

    public MMPathFinder(NodeProcessor processor) {
        super(processor);
        this.nodeProcessor = processor;
    }

    @Nullable
    @Override
    public Path findPath(IBlockAccess worldIn, MobEntity living, Entity targetEntity, float maxDistance) {
        return this.findPath(worldIn, living, targetEntity.posX, targetEntity.getBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    @Override
    public Path findPath(IBlockAccess worldIn, MobEntity living, BlockPos targetPos, float maxDistance) {
        return this.findPath(worldIn, living, targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, maxDistance);
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, MobEntity living, double x, double y, double z, float maxDistance) {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, living);
        PathPoint start = this.nodeProcessor.getStart();
        PathPoint end = this.nodeProcessor.getPathPointToCoords(x, y, z);
        Path path = this.findPath(start, end, maxDistance);
        this.nodeProcessor.postProcess();
        return path;
    }

    @Nullable
    private Path findPath(PathPoint pathFrom, PathPoint pathTo, float maxDistance) {
        pathFrom.totalPathDistance = 0.0F;
        pathFrom.distanceToNext = pathFrom.distanceManhattan(pathTo);
        pathFrom.distanceToTarget = pathFrom.distanceToNext;
        this.path.clearPath();
        this.path.addPoint(pathFrom);
        PathPoint pathpoint = pathFrom;
        int attempts = 0;
        while (!this.path.isPathEmpty()) {
            if (++attempts >= 200) {
                break;
            }
            PathPoint point = this.path.dequeue();
            if (point.equals(pathTo)) {
                pathpoint = pathTo;
                break;
            }
            if (point.distanceManhattan(pathTo) < pathpoint.distanceManhattan(pathTo)) {
                pathpoint = point;
            }
            point.visited = true;
            int found = this.nodeProcessor.findPathOptions(this.pathOptions, point, pathTo, maxDistance);
            for (int i = 0; i < found; i++) {
                PathPoint p = this.pathOptions[i];
                float dist = point.distanceManhattan(p);
                p.distanceFromOrigin = point.distanceFromOrigin + dist;
                p.cost = dist + p.costMalus;
                float totalDistance = point.totalPathDistance + p.cost;
                if (p.distanceFromOrigin < maxDistance && (!p.isAssigned() || totalDistance < p.totalPathDistance)) {
                    p.previous = point;
                    p.totalPathDistance = totalDistance;
                    p.distanceToNext = p.distanceManhattan(pathTo) + p.costMalus;
                    if (p.isAssigned()) {
                        this.path.changeDistance(p, p.totalPathDistance + p.distanceToNext);
                    } else {
                        p.distanceToTarget = p.totalPathDistance + p.distanceToNext;
                        this.path.addPoint(p);
                    }
                }
            }
        }
        if (pathpoint == pathFrom) {
            return null;
        }
        return this.createPath(pathFrom, pathpoint);
    }

    private Path createPath(PathPoint start, PathPoint end) {
        int i = 1;
        for (PathPoint p = end; p.previous != null; p = p.previous) {
            i++;
        }
        PathPoint[] points = new PathPoint[i];
        PathPoint p = end;
        i--;
        for (points[i] = end; p.previous != null; points[i] = p) {
            p = p.previous;
            i--;
        }
        return new PatchedPath(points);
    }

    static class PatchedPath extends Path {
        public PatchedPath(PathPoint[] pathpoints) {
            super(pathpoints);
        }

        @Override
        public Vec3d getVectorFromIndex(Entity entity, int index) {
            PathPoint point = this.getPathPointFromIndex(index);
            double d0 = point.x + MathHelper.floor(entity.width + 1.0F) * 0.5D;
            double d1 = point.y;
            double d2 = point.z + MathHelper.floor(entity.width + 1.0F) * 0.5D;
            return new Vec3d(d0, d1, d2);
        }
    }
}
