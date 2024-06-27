package com.bobmowzie.mowziesmobs.client.render.block;

import net.minecraft.core.BlockPos;

public class SculptorBlockMarking {
    private final BlockPos pos;
    private int ticks;

    public SculptorBlockMarking(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void resetTick() {
        this.ticks = 0;
    }

    public int getTicks() {
        return this.ticks;
    }

    public void tick() {
        this.ticks++;
    }

    public int getDuration() {
        return 160;
    }
}