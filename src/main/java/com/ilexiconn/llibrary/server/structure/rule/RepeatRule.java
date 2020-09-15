package com.ilexiconn.llibrary.server.structure.rule;

import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public abstract class RepeatRule {
    private int spacingX;
    private int spacingY;
    private int spacingZ;

    public void setSpacing(int spacingX, int spacingY, int spacingZ) {
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.spacingZ = spacingZ;
    }

    public int getSpacingX() {
        return this.spacingX;
    }

    public int getSpacingY() {
        return this.spacingY;
    }

    public int getSpacingZ() {
        return this.spacingZ;
    }

    public abstract boolean continueRepeating(World world, Random rand, MutableBlockPos position);

    public abstract void repeat(World world, Random rand, MutableBlockPos position);

    public abstract void reset(World world, Random random, MutableBlockPos pos);

    public void init(World world, Random random, MutableBlockPos pos) {
        this.reset(world, random, pos);
    }
}
