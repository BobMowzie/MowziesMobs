package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCampfire extends Block {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.4375, 0.875);

    public BlockCampfire() {
        super(Material.WOOD, MapColor.TNT);
        setTranslationKey("campfire");
        setRegistryName("campfire");
        setLightLevel(0.8125F);
        useNeighborBrightness = true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random rng) {
        return rng.nextInt(3);
    }

    @Override
    public Item getItemDropped(BlockState state, Random rng, int fortune) {
        return Items.STICK;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rng) {
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
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!(entity instanceof EntityBarakoa)) entity.attackEntityFrom(DamageSource.IN_FIRE, 1);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
