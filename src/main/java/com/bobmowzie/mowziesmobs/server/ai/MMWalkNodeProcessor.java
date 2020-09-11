package com.bobmowzie.mowziesmobs.server.ai;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Set;

public class MMWalkNodeProcessor extends WalkNodeProcessor {
    @Override
    public PathPoint getStart() {
        int y;
        AxisAlignedBB bounds = this.entity.getEntityBoundingBox();
        if (this.getCanSwim() && this.entity.isInWater()) {
            y = (int) bounds.minY;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), y, MathHelper.floor(this.entity.posZ));
            for (Block block = this.blockaccess.getBlockState(pos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(pos).getBlock()) {
                pos.setY(++y);
            }
        } else if (this.entity.onGround) {
            y = MathHelper.floor(bounds.minY + 0.5D);
        } else {
            y = MathHelper.floor(this.entity.posY);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), y, MathHelper.floor(this.entity.posZ));
            while (y > 0 && (this.blockaccess.getBlockState(pos).getMaterial() == Material.AIR || this.blockaccess.getBlockState(pos).getBlock().isPassable(this.blockaccess, pos))) {
                pos.setY(y--);
            }
            y++;
        }
        // account for node size
        float r = this.entity.width * 0.5F;
        int x = MathHelper.floor(this.entity.posX - r);
        int z = MathHelper.floor(this.entity.posZ - r);
        if (this.entity.getPathPriority(this.getPathType(this.entity, x, y, z)) < 0.0F) {
            Set<BlockPos> diagonals = Sets.newHashSet();
            diagonals.add(new BlockPos(bounds.minX - r, y, bounds.minZ - r));
            diagonals.add(new BlockPos(bounds.minX - r, y, bounds.maxZ - r));
            diagonals.add(new BlockPos(bounds.maxX - r, y, bounds.minZ - r));
            diagonals.add(new BlockPos(bounds.maxX - r, y, bounds.maxZ - r));
            for (BlockPos p : diagonals) {
                PathNodeType pathnodetype = this.getPathType(this.entity, p.getX(), p.getY(), p.getZ());
                if (this.entity.getPathPriority(pathnodetype) >= 0.0F) {
                    return this.openPoint(p.getX(), p.getY(), p.getZ());
                }
            }
        }
        return this.openPoint(x, y, z);
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int optionCount = 0;
        int step = 0;
        PathNodeType pathnodetype = this.getPathType(this.entity, currentPoint.x, currentPoint.y + 1, currentPoint.z);
        if (this.entity.getPathPriority(pathnodetype) >= 0.0F) {
            step = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
        }
        BlockPos under = (new BlockPos(currentPoint.x, currentPoint.y, currentPoint.z)).down();
        double floor = currentPoint.y - (1.0D - this.blockaccess.getBlockState(under).getBoundingBox(this.blockaccess, under).maxY);
        PathPoint south = this.getNode(currentPoint.x, currentPoint.y, currentPoint.z + 1, step, floor, Direction.SOUTH);
        PathPoint west = this.getNode(currentPoint.x - 1, currentPoint.y, currentPoint.z, step, floor, Direction.WEST);
        PathPoint east = this.getNode(currentPoint.x + 1, currentPoint.y, currentPoint.z, step, floor, Direction.EAST);
        PathPoint north = this.getNode(currentPoint.x, currentPoint.y, currentPoint.z - 1, step, floor, Direction.NORTH);
        if (south != null && !south.visited && south.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionCount++] = south;
        }
        if (west != null && !west.visited && west.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionCount++] = west;
        }
        if (east != null && !east.visited && east.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionCount++] = east;
        }
        if (north != null && !north.visited && north.distanceTo(targetPoint) < maxDistance) {
            pathOptions[optionCount++] = north;
        }
        boolean northPassable = north == null || north.nodeType == PathNodeType.OPEN || north.costMalus != 0.0F;
        boolean southPassable = south == null || south.nodeType == PathNodeType.OPEN || south.costMalus != 0.0F;
        boolean eastPassable = east == null || east.nodeType == PathNodeType.OPEN || east.costMalus != 0.0F;
        boolean westPassable = west == null || west.nodeType == PathNodeType.OPEN || west.costMalus != 0.0F;
        if (northPassable && westPassable) {
            PathPoint northwest = this.getNode(currentPoint.x - 1, currentPoint.y, currentPoint.z - 1, step, floor, Direction.NORTH);
            if (northwest != null && !northwest.visited && northwest.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionCount++] = northwest;
            }
        }
        if (northPassable && eastPassable) {
            PathPoint northeast = this.getNode(currentPoint.x + 1, currentPoint.y, currentPoint.z - 1, step, floor, Direction.NORTH);
            if (northeast != null && !northeast.visited && northeast.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionCount++] = northeast;
            }
        }
        if (southPassable && westPassable) {
            PathPoint southwest = this.getNode(currentPoint.x - 1, currentPoint.y, currentPoint.z + 1, step, floor, Direction.SOUTH);
            if (southwest != null && !southwest.visited && southwest.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionCount++] = southwest;
            }
        }
        if (southPassable && eastPassable) {
            PathPoint southeast = this.getNode(currentPoint.x + 1, currentPoint.y, currentPoint.z + 1, step, floor, Direction.SOUTH);
            if (southeast != null && !southeast.visited && southeast.distanceTo(targetPoint) < maxDistance) {
                pathOptions[optionCount++] = southeast;
            }
        }
        return optionCount;
    }

    @Nullable
    private PathPoint getNode(int x, int y, int z, int step, double floor, Direction dir) {
        PathPoint result = null;
        BlockPos pos = new BlockPos(x, y, z);
        BlockPos under = pos.down();
        double dirFloor = (double) y - (1.0D - this.blockaccess.getBlockState(under).getBoundingBox(this.blockaccess, under).maxY);
        if (dirFloor - floor > 1.125D) {
            return null;
        }
        PathNodeType atNode = this.getPathType(this.entity, x, y, z);
        float malus = this.entity.getPathPriority(atNode);
        double r = this.entity.width / 2.0D;
        if (malus >= 0.0F) {
            result = this.openPoint(x, y, z);
            result.nodeType = atNode;
            result.costMalus = Math.max(result.costMalus, malus);
        }
        if (atNode == PathNodeType.WALKABLE) {
            return result;
        }
        if (result == null && step > 0 && atNode != PathNodeType.FENCE && atNode != PathNodeType.TRAPDOOR) {
            result = this.getNode(x, y + 1, z, step - 1, floor, dir);
            if (result != null && (result.nodeType == PathNodeType.OPEN || result.nodeType == PathNodeType.WALKABLE) && this.entity.width < 1.0F) {
                double px = (x - dir.getXOffset()) + 0.5D;
                double pz = (z - dir.getZOffset()) + 0.5D;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(px - r, y + 0.001D, pz - r, px + r, y + this.entity.height, pz + r);
                AxisAlignedBB floorShape = this.blockaccess.getBlockState(pos).getBoundingBox(this.blockaccess, pos);
                AxisAlignedBB collision = axisalignedbb.expand(0.0D, floorShape.maxY - 0.002D, 0.0D);
                if (this.entity.world.collidesWithAnyBlock(collision)) {
                    result = null;
                }
            }
        }
        if (atNode == PathNodeType.OPEN) {
            // account for node size
            AxisAlignedBB collision = new AxisAlignedBB(
                x - r + this.entitySizeX * 0.5D, y + 0.001D, z - r + this.entitySizeZ * 0.5D,
                x + r + this.entitySizeX * 0.5D, y + this.entity.height, z + r + this.entitySizeZ * 0.5D
            );
            if (this.entity.world.collidesWithAnyBlock(collision)) {
                return null;
            }
            if (this.entity.width >= 1.0F) {
                PathNodeType down = this.getPathType(this.entity, x, y - 1, z);
                if (down == PathNodeType.BLOCKED) {
                    result = this.openPoint(x, y, z);
                    result.nodeType = PathNodeType.WALKABLE;
                    result.costMalus = Math.max(result.costMalus, malus);
                    return result;
                }
            }
            int fallDistance = 0;
            while (y-- > 0 && atNode == PathNodeType.OPEN) {
                if (fallDistance++ >= this.entity.getMaxFallHeight()) {
                    return null;
                }
                atNode = this.getPathType(this.entity, x, y, z);
                malus = this.entity.getPathPriority(atNode);
                if (atNode != PathNodeType.OPEN && malus >= 0.0F) {
                    result = this.openPoint(x, y, z);
                    result.nodeType = atNode;
                    result.costMalus = Math.max(result.costMalus, malus);
                    break;
                }
                if (malus < 0.0F) {
                    return null;
                }
            }
        }
        return result;
    }

    private PathNodeType getPathType(MobEntity living, int x, int y, int z) {
        return this.getPathNodeType(this.blockaccess, x, y, z, living, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }
}
