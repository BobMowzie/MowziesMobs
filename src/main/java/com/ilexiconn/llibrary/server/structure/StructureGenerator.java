package com.ilexiconn.llibrary.server.structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public abstract class StructureGenerator {
    public static final EnumFacing[] CLOCKWISE_FACINGS = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};

    public static EnumFacing getNextClockwise(EnumFacing facing) {
        int index = ArrayUtils.indexOf(CLOCKWISE_FACINGS, facing);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        return CLOCKWISE_FACINGS[(index + 1) % CLOCKWISE_FACINGS.length];
    }

    public abstract void generate(World world, BlockPos pos, Random random);

    public abstract StructureGenerator rotate(EnumFacing front, EnumFacing top);

    public abstract StructureGenerator rotateTowards(EnumFacing facing);
}
