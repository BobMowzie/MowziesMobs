package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

/**
 * Created by Josh on 5/1/2017.
 */
public class MowzieBlockAccess implements IBlockAccess {
    private IBlockState accessState;
    private Biome biome = Biomes.PLAINS;

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return 0;
    }

    public void setBlockState(IBlockState state) {
        this.accessState = state;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return accessState;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return false;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return biome;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return 0;
    }

    @Override
    public WorldType getWorldType() {
        return Minecraft.getMinecraft().world.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return true;
    }
}
