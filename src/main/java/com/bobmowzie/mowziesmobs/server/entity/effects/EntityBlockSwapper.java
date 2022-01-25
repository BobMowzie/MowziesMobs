package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Created by BobMowzie on 7/8/2018.
 */
public class EntityBlockSwapper extends Entity {
    private static final DataParameter<Optional<BlockState>> ORIG_BLOCK_STATE = EntityDataManager.createKey(EntityBlockSwapper.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Integer> RESTORE_TIME = EntityDataManager.createKey(EntityBlockSwapper.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> POS = EntityDataManager.createKey(EntityBlockSwapper.class, DataSerializers.BLOCK_POS);
    private int duration;
    private final boolean breakParticlesEnd;
    private BlockPos pos;

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, World world) {
        super(type, world);
        breakParticlesEnd = false;
    }

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        super(type, world);
        setStorePos(pos);
        setRestoreTime(duration);
        this.breakParticlesEnd = breakParticlesEnd;
        setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.isRemote) {
            setOrigBlock(world.getBlockState(pos));
            if (breakParticlesStart) world.destroyBlock(pos, false);
            world.setBlockState(pos, newBlock, 19);
        }
        List<EntityBlockSwapper> swappers = world.getEntitiesWithinAABB(EntityBlockSwapper.class, getBoundingBox());
        if (!swappers.isEmpty()) {
            EntityBlockSwapper swapper = swappers.get(0);
            setOrigBlock(swapper.getOrigBlock());
            swapper.remove();
        }
    }

    public static void swapBlock(World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        if (!world.isRemote) {
            EntityBlockSwapper swapper = new EntityBlockSwapper(EntityHandler.BLOCK_SWAPPER.get(), world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            world.addEntity(swapper);
        }
    }

    @Override
    public boolean isInRangeToRender3d(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return false;
    }

    @Override
    protected void registerData() {
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

    @Nullable
    public BlockState getOrigBlock() {
        Optional<BlockState> opState = getDataManager().get(ORIG_BLOCK_STATE);
        return opState.orElse(null);
    }

    public void setOrigBlock(BlockState block) {
        getDataManager().set(ORIG_BLOCK_STATE, Optional.of(block));
    }

    public void restoreBlock() {
        if (!world.isRemote) {
            if (breakParticlesEnd) world.destroyBlock(pos, false);
            world.setBlockState(pos, getOrigBlock(), 19);
            remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted > duration && world.getEntitiesWithinAABB(PlayerEntity.class, getBoundingBox()).isEmpty()) restoreBlock();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        Optional<BlockState> blockOption = getDataManager().get(ORIG_BLOCK_STATE);
        blockOption.ifPresent(blockState -> compound.put("block", NBTUtil.writeBlockState(blockState)));
        compound.putInt("restoreTime", getRestoreTime());
        compound.putInt("storePosX", getStorePos().getX());
        compound.putInt("storePosY", getStorePos().getY());
        compound.putInt("storePosZ", getStorePos().getZ());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        INBT blockNBT = compound.get("block");
        if (blockNBT != null) {
            BlockState blockState = NBTUtil.readBlockState((CompoundNBT) blockNBT);
            setOrigBlock(blockState);
        }
        setRestoreTime(compound.getInt("restoreTime"));
        setStorePos(new BlockPos(
                compound.getInt("storePosX"),
                compound.getInt("storePosY"),
                compound.getInt("storePosZ")
        ));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
