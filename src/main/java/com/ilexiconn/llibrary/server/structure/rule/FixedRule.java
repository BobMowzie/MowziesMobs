package com.ilexiconn.llibrary.server.structure.rule;

import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class FixedRule extends RepeatRule {
    protected int times;
    protected int countdown;

    public FixedRule(int times) {
        super();
        this.times = times;
        this.countdown = times;
    }

    @Override
    public boolean continueRepeating(World world, Random rand, MutableBlockPos position) {
        return this.countdown > 0;
    }

    @Override
    public void repeat(World world, Random rand, MutableBlockPos position) {
        this.countdown--;
        position.setPos(
                position.getX() + this.getSpacingX(),
                position.getY() + this.getSpacingY(),
                position.getZ() + this.getSpacingZ()
        );
    }

    @Override
    public void reset(World world, Random random, MutableBlockPos pos) {
        this.countdown = this.times;
    }
}
