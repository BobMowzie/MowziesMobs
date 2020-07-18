package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.block.Block;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class MMPathNavigateGround extends PathNavigateGround {
    public MMPathNavigateGround(MowzieEntity entity, World world) {
        super(entity, world);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new MMPathFinder(this.nodeProcessor);
    }

    @Override
    protected void pathFollow() {
        Path path = Objects.requireNonNull(currentPath);
        Vec3d entityPos = getEntityPosition();
        int pathLength = path.getCurrentPathLength();
        for (int i = path.getCurrentPathIndex(); i < path.getCurrentPathLength(); i++) {
            if (path.getPathPointFromIndex(i).y != Math.floor(entityPos.y)) {
                pathLength = i;
                break;
            }
        }
        final int sizeX = MathHelper.floor(entity.width + 1.0F);
        final int sizeY = MathHelper.floor(entity.height + 1.0F);
        final float threshold = (sizeX - entity.width) * 0.5F;
        Vec3d pathPos = path.getPosition(entity);
        if (MathHelper.abs((float) (entity.posX - pathPos.x)) < threshold &&
                MathHelper.abs((float) (entity.posZ - pathPos.z)) < threshold &&
                Math.abs(entity.posY - pathPos.y) < 1.0D) {
            path.setCurrentPathIndex(path.getCurrentPathIndex() + 1);
        }
        for (int i = pathLength; i-- > path.getCurrentPathIndex(); ) {
            if (this.isDirectPathBetweenPoints(entityPos, path.getVectorFromIndex(entity, i), sizeX, sizeY, sizeX)) {
                path.setCurrentPathIndex(i);
                break;
            }
        }
        checkForStuck(entityPos);
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3d start, Vec3d end, int sizeX, int sizeY, int sizeZ) {
        int x0 = MathHelper.floor(start.x);
        int z0 = MathHelper.floor(start.z);
        double vx = end.x - start.x;
        double vz = end.z - start.z;
        double dist = vx * vx + vz * vz;
        if (dist < 1.0E-8D) {
            return false;
        }
        double invDist = 1.0D / Math.sqrt(dist);
        vx = vx * invDist;
        vz = vz * invDist;
        /*sizeX = sizeX + 2;
        sizeZ = sizeZ + 2;*/
        if (!this.isSafeToStandAt(x0, (int) start.y, z0, sizeX, sizeY, sizeZ, start, vx, vz)) {
            return false;
        }
        /*sizeX = sizeX - 2;
        sizeZ = sizeZ - 2;*/
        double d4 = 1.0D / Math.abs(vx);
        double d5 = 1.0D / Math.abs(vz);
        double d6 = (double) x0 - start.x;
        double d7 = (double) z0 - start.z;
        if (vx >= 0.0D) {
            ++d6;
        }
        if (vz >= 0.0D) {
            ++d7;
        }
        d6 = d6 / vx;
        d7 = d7 / vz;
        int sx = vx < 0.0D ? -1 : 1;
        int sz = vz < 0.0D ? -1 : 1;
        int x1 = MathHelper.floor(end.x);
        int z1 = MathHelper.floor(end.z);
        int dx = x1 - x0;
        int dz = z1 - z0;
        while (dx * sx > 0 || dz * sz > 0) {
            if (d6 < d7) {
                d6 += d4;
                x0 += sx;
                dx = x1 - x0;
            } else {
                d7 += d5;
                z0 += sz;
                dz = z1 - z0;
            }
            if (!this.isSafeToStandAt(x0, (int) start.y, z0, sizeX, sizeY, sizeZ, start, vx, vz)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d origin, double dx, double dz) {
        int minX = x - sizeX / 2;
        int minY = z - sizeZ / 2;
        if (!this.isPositionClear(minX, y, minY, sizeX, sizeY, sizeZ, origin, dx, dz)) {
            return false;
        }
        for (int k = minX; k < minX + sizeX; k++) {
            for (int l = minY; l < minY + sizeZ; l++) {
                double d0 = (double) k + 0.5D - origin.x;
                double d1 = (double) l + 0.5D - origin.z;
                if (d0 * dx + d1 * dz >= 0.0D) {
                    PathNodeType below = this.nodeProcessor.getPathNodeType(this.world, k, y - 1, l, this.entity, sizeX, sizeY, sizeZ, true, true);
                    if (below == PathNodeType.WATER) {
                        return false;
                    }
                    if (below == PathNodeType.LAVA) {
                        return false;
                    }
                    if (below == PathNodeType.OPEN) {
                        return false;
                    }
                    PathNodeType in = this.nodeProcessor.getPathNodeType(this.world, k, y, l, this.entity, sizeX, sizeY, sizeZ, true, true);
                    float priority = this.entity.getPathPriority(in);
                    if (priority < 0.0F || priority >= 8.0F) {
                        return false;
                    }
                    if (in == PathNodeType.DAMAGE_FIRE || in == PathNodeType.DANGER_FIRE || in == PathNodeType.DAMAGE_OTHER) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isPositionClear(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d p_179692_7_, double p_179692_8_, double p_179692_10_) {
        for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1))) {
            double d0 = (double) pos.getX() + 0.5D - p_179692_7_.x;
            double d1 = (double) pos.getZ() + 0.5D - p_179692_7_.z;
            if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D) {
                Block block = this.world.getBlockState(pos).getBlock();
                if (!block.isPassable(this.world, pos)) {
                    return false;
                }
            }
        }
        return true;
    }
}
