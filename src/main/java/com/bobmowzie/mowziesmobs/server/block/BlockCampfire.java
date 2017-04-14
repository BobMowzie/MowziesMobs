package com.bobmowzie.mowziesmobs.server.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCampfire extends Block {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.4375, 0.875);

    public BlockCampfire() {
        super(Material.WOOD);
        setUnlocalizedName("campfire");
        setRegistryName("campfire");
        setLightLevel(0.8125F);
        useNeighborBrightness = true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random rng) {
        return rng.nextInt(3);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rng, int fortune) {
        return Items.STICK;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rng) {
        if (rng.nextFloat() < 1 / 12F) {
            world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1 + rng.nextFloat(), rng.nextFloat() * 0.7F + 0.3F, false);
        }
        for (int n = rng.nextInt(3) + 2; n >= 0; n--) {
            double x = pos.getX() + 0.2 + rng.nextDouble() * 0.6;
            double y = pos.getY() + 0.1 + rng.nextDouble() * 0.4;
            double z = pos.getZ() + 0.2 + rng.nextDouble() * 0.6;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
        }
        for (int n = rng.nextInt(2) + 3; n >= 0; n--) {
            double x = pos.getX() + 0.2 + rng.nextDouble() * 0.6;
            double y = pos.getY() + 0.1 + rng.nextDouble() * 0.3;
            double z = pos.getZ() + 0.2 + rng.nextDouble() * 0.6;
            world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.attackEntityFrom(DamageSource.IN_FIRE, 1);
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.TNT;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
