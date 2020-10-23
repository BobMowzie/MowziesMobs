package com.ilexiconn.llibrary.server.structure;

import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public abstract class StructureGenerator {
    public static final Direction[] CLOCKWISE_FACINGS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static Direction getNextClockwise(Direction facing) {
        int index = ArrayUtils.indexOf(CLOCKWISE_FACINGS, facing);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        return CLOCKWISE_FACINGS[(index + 1) % CLOCKWISE_FACINGS.length];
    }

    public abstract void generate(World world, BlockPos pos, Random random);

    public abstract StructureGenerator rotate(Direction front, Direction top);

    public abstract StructureGenerator rotateTowards(Direction facing);
}
