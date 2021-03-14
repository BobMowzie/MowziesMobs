package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public class MowzieBlockAccess implements IBlockReader {
    private BlockState accessState;
    private Biome biome = Biomes.PLAINS;

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getLightValue(BlockPos pos) {
        return 0;
    }

    public void setBlockState(BlockState state) {
        this.accessState = state;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return accessState;
    }

    @Override
    public IFluidState getFluidState(BlockPos blockPos) {
        return null;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public Biome getBiome() {
        return biome;
    }
}
