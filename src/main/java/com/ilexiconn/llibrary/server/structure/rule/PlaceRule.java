package com.ilexiconn.llibrary.server.structure.rule;

import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class PlaceRule extends RepeatRule {
    private boolean placed;

    @Override
    public boolean continueRepeating(World world, Random rand, MutableBlockPos position) {
        return !this.placed;
    }

    @Override
    public void repeat(World world, Random rand, MutableBlockPos position) {
        this.placed = true;
    }

    @Override
    public void reset(World world, Random random, MutableBlockPos pos) {
        this.placed = false;
    }
}
