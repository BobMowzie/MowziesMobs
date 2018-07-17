package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Josh on 7/8/2018.
 */
public class EntityBlockSwapper extends Entity {
    private static final DataParameter<Optional<IBlockState>> ORIG_BLOCK_STATE = EntityDataManager.createKey(EntityBlockSwapper.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Integer> RESTORE_TIME = EntityDataManager.createKey(EntityBlockSwapper.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> POS = EntityDataManager.createKey(EntityBlockSwapper.class, DataSerializers.BLOCK_POS);
    private int duration;
    private boolean breakParticlesEnd;
    private BlockPos pos;

    public EntityBlockSwapper(World world) {
        this(world, new BlockPos(0, 0, 0), Blocks.AIR.getDefaultState(), 20, false, false);
    }

    public EntityBlockSwapper(World world, BlockPos pos, IBlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        super(world);
        setStorePos(pos);
        setRestoreTime(duration);
        this.breakParticlesEnd = breakParticlesEnd;
        setSize(1, 1);
        setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.isRemote) {
            setOrigBlock(world.getBlockState(pos));
            if (breakParticlesStart) world.destroyBlock(pos, false);
            world.setBlockState(pos, newBlock);
        }
        List<EntityBlockSwapper> swappers = world.getEntitiesWithinAABB(EntityBlockSwapper.class, getEntityBoundingBox());
        if (!swappers.isEmpty()) {
            EntityBlockSwapper swapper = swappers.get(0);
            setOrigBlock(swapper.getOrigBlock());
            swapper.setDead();
        }
    }

    public static void swapBlock(World world, BlockPos pos, IBlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        if (!world.isRemote) {
            EntityBlockSwapper swapper = new EntityBlockSwapper(world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            world.spawnEntity(swapper);
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return false;
    }

    @Override
    protected void entityInit() {
        getDataManager().register(ORIG_BLOCK_STATE, Optional.of(Blocks.DIRT.getDefaultState()));
        getDataManager().register(RESTORE_TIME, 20);
        getDataManager().register(POS, new BlockPos(0, 0, 0));
    }

    public int getRestoreTime() {
        return dataManager.get(RESTORE_TIME);
    }

    public void setRestoreTime(int restoreTime) {
        dataManager.set(RESTORE_TIME, restoreTime);
        duration = restoreTime;
    }

    public BlockPos getStorePos() {
        return dataManager.get(POS);
    }

    public void setStorePos(BlockPos bpos) {
        dataManager.set(POS, bpos);
        pos = bpos;
    }

    public IBlockState getOrigBlock() {
        return getDataManager().get(ORIG_BLOCK_STATE).get();
    }

    public void setOrigBlock(IBlockState block) {
        getDataManager().set(ORIG_BLOCK_STATE, Optional.of(block));
    }

    public void restoreBlock() {
        if (!world.isRemote) {
            if (breakParticlesEnd) world.destroyBlock(pos, false);
            world.setBlockState(pos, getOrigBlock());
            setDead();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted > duration && world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox()).isEmpty()) restoreBlock();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        Optional<IBlockState> blockOption = Optional.of(getOrigBlock());
        if (blockOption.isPresent()) {
            compound.setTag("block", NBTUtil.writeBlockState(new NBTTagCompound(), blockOption.get()));
        }
        compound.setInteger("restoreTime", getRestoreTime());
        compound.setInteger("storePosX", getStorePos().getX());
        compound.setInteger("storePosY", getStorePos().getY());
        compound.setInteger("storePosZ", getStorePos().getZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        IBlockState blockState = NBTUtil.readBlockState((NBTTagCompound) compound.getTag("block"));
        setOrigBlock(blockState);
        setRestoreTime(compound.getInteger("restoreTime"));
        setStorePos(new BlockPos(
                compound.getInteger("storePosX"),
                compound.getInteger("storePosY"),
                compound.getInteger("storePosZ")
        ));
    }
}
