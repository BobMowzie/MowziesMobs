package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.Set;

public class MMPathFinder extends PathFinder {
    public MMPathFinder(NodeProcessor processor, int maxVisitedNodes) {
        super(processor, maxVisitedNodes);
    }

    @Nullable
    @Override
    public Path func_224775_a(IWorldReader world, MobEntity mob, Set<BlockPos> targets, float maxDistance, int requiredDistance) {
        Path path = super.func_224775_a(world, mob, targets, maxDistance, requiredDistance);
        return path == null ? null : new PatchedPath(path);
    }

    static class PatchedPath extends Path {
        public PatchedPath(Path original) {
            super(original.func_215746_d(), original.func_224770_k(), original.func_224771_h());
        }

        @Override
        public Vec3d getVectorFromIndex(Entity entity, int index) {
            PathPoint point = this.getPathPointFromIndex(index);
            double d0 = point.x + MathHelper.floor(entity.getWidth() + 1.0F) * 0.5D;
            double d1 = point.y;
            double d2 = point.z + MathHelper.floor(entity.getWidth() + 1.0F) * 0.5D;
            return new Vec3d(d0, d1, d2);
        }
    }
}
