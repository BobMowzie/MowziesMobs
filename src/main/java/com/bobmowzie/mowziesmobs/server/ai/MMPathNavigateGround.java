package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.google.common.base.Preconditions;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MMPathNavigateGround extends PathNavigateGround {
    public MMPathNavigateGround(MowzieEntity entity, World world) {
        super(entity, world);
    }

    @Override
    protected void pathFollow() {
        Path path = Preconditions.checkNotNull(currentPath);
        Vec3d entityPos = getEntityPosition();
        int pathLength = path.getCurrentPathLength();
        for (int i = path.getCurrentPathIndex(); i < path.getCurrentPathLength(); i++) {
            if (path.getPathPointFromIndex(i).y != Math.floor(entityPos.y)) {
                pathLength = i;
                break;
            }
        }
        float threshold = entity.width > 0.75F ? entity.width / 2.0F : 0.75F - entity.width / 2.0F;
        Vec3d pathPos = path.getPosition(entity);
        if (MathHelper.abs((float) (entity.posX - pathPos.x)) < threshold &&
            MathHelper.abs((float) (entity.posZ - pathPos.z)) < threshold &&
            Math.abs(entity.posY - pathPos.y) < 1.0D) {
            path.setCurrentPathIndex(path.getCurrentPathIndex() + 1);
        }
        int entityWidth = MathHelper.ceil(entity.width), entityHeight = MathHelper.ceil(entity.height);
        for (int i = pathLength - 1; i >= path.getCurrentPathIndex(); i--) {
            if (isDirectPathBetweenPoints(entityPos, path.getVectorFromIndex(entity, i), entityWidth, entityHeight, entityWidth)) {
                path.setCurrentPathIndex(i);
                break;
            }
        }
        checkForStuck(entityPos);
    }
}
