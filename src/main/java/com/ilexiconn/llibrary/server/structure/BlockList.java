package com.ilexiconn.llibrary.server.structure;

import net.minecraft.block.state.IBlockState;

import java.util.Arrays;
import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class BlockList {
    private final float[] probabilities;
    private final IBlockState[] states;

    public BlockList(IBlockState blockState) {
        this(new IBlockState[]{blockState}, new float[]{1f});
    }

    public BlockList(IBlockState[] blockStates, float[] probabilities) {
        this.states = blockStates;
        this.probabilities = probabilities;
    }

    public IBlockState getRandom(Random rand) {
        float chosen = rand.nextFloat();
        IBlockState result = null;
        int index = 0;
        while (chosen >= 0f) {
            if (index >= this.states.length) {
                return null;
            }
            chosen -= this.probabilities[index];
            result = this.states[index];
            index++;
        }
        return result;
    }

    public BlockList copy() {
        IBlockState[] newStates = Arrays.copyOf(this.states, this.states.length);
        float[] newProbabilities = Arrays.copyOf(this.probabilities, this.probabilities.length);
        return new BlockList(newStates, newProbabilities);
    }

    public IBlockState[] getStates() {
        return this.states;
    }

    public float[] getProbabilities() {
        return this.probabilities;
    }
}
