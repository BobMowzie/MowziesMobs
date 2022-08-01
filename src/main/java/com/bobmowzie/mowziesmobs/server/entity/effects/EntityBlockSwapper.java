package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Created by BobMowzie on 7/8/2018.
 */
public class EntityBlockSwapper extends Entity {
    private static final EntityDataAccessor<Optional<BlockState>> ORIG_BLOCK_STATE = SynchedEntityData.defineId(EntityBlockSwapper.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> RESTORE_TIME = SynchedEntityData.defineId(EntityBlockSwapper.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> POS = SynchedEntityData.defineId(EntityBlockSwapper.class, EntityDataSerializers.BLOCK_POS);
    private int duration;
    private final boolean breakParticlesEnd;
    private BlockPos pos;

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, Level world) {
        super(type, world);
        breakParticlesEnd = false;
    }

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, Level world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        super(type, world);
        setStorePos(pos);
        setRestoreTime(duration);
        this.breakParticlesEnd = breakParticlesEnd;
        setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.isClientSide) {
            setOrigBlock(world.getBlockState(pos));
            if (breakParticlesStart) world.destroyBlock(pos, false);
            world.setBlock(pos, newBlock, 19);
        }
        List<EntityBlockSwapper> swappers = world.getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
        if (!swappers.isEmpty()) {
            EntityBlockSwapper swapper = swappers.get(0);
            setOrigBlock(swapper.getOrigBlock());
            swapper.discard();
        }
    }

    public static void swapBlock(Level world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        if (!world.isClientSide) {
            EntityBlockSwapper swapper = new EntityBlockSwapper(EntityHandler.BLOCK_SWAPPER.get(), world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            world.addFreshEntity(swapper);
        }
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(ORIG_BLOCK_STATE, Optional.of(Blocks.DIRT.defaultBlockState()));
        getEntityData().define(RESTORE_TIME, 20);
        getEntityData().define(POS, new BlockPos(0, 0, 0));
    }

    public int getRestoreTime() {
        return entityData.get(RESTORE_TIME);
    }

    public void setRestoreTime(int restoreTime) {
        entityData.set(RESTORE_TIME, restoreTime);
        duration = restoreTime;
    }

    public BlockPos getStorePos() {
        return entityData.get(POS);
    }

    public void setStorePos(BlockPos bpos) {
        entityData.set(POS, bpos);
        pos = bpos;
    }

    @Nullable
    public BlockState getOrigBlock() {
        Optional<BlockState> opState = getEntityData().get(ORIG_BLOCK_STATE);
        return opState.orElse(null);
    }

    public void setOrigBlock(BlockState block) {
        getEntityData().set(ORIG_BLOCK_STATE, Optional.of(block));
    }

    public void restoreBlock() {
        if (!level.isClientSide) {
            if (breakParticlesEnd) level.destroyBlock(pos, false);
            level.setBlock(pos, getOrigBlock(), 19);
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > duration && level.getEntitiesOfClass(Player.class, getBoundingBox()).isEmpty()) restoreBlock();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        Optional<BlockState> blockOption = getEntityData().get(ORIG_BLOCK_STATE);
        blockOption.ifPresent(blockState -> compound.put("block", NbtUtils.writeBlockState(blockState)));
        compound.putInt("restoreTime", getRestoreTime());
        compound.putInt("storePosX", getStorePos().getX());
        compound.putInt("storePosY", getStorePos().getY());
        compound.putInt("storePosZ", getStorePos().getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        Tag blockNBT = compound.get("block");
        if (blockNBT != null) {
            BlockState blockState = NbtUtils.readBlockState((CompoundTag) blockNBT);
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
