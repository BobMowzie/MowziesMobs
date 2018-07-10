package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Josh on 7/8/2018.
 */
public class EntityBlockSwapper extends Entity {
    public EntityBlockSwapper(World world, BlockPos pos, IBlockState newBlock, int duration) {
        super(world);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
