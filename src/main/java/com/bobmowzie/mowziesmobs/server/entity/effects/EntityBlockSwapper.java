package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
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
    protected int duration;
    protected boolean breakParticlesEnd;
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
        for (EntityBlockSwapper swapper : swappers) {
            if (swapper == this) continue;
            if (swapper instanceof EntityBlockSwapperSculptor) {
                EntityBlockSwapperSculptor swapperSculptor = (EntityBlockSwapperSculptor) swapper;
                setOrigBlock(swapperSculptor.getOrigBlockAtLocation(pos));
            }
            else {
                setOrigBlock(swapper.getOrigBlock());
                swapper.discard();
            }
        }
    }

    public static void swapBlock(Level world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        if (!world.isClientSide) {
            EntityBlockSwapper swapper = new EntityBlockSwapper(EntityHandler.BLOCK_SWAPPER.get(), world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            world.addFreshEntity(swapper);
        }
    }

    public boolean isBlockPosInsideSwapper(BlockPos pos) {
        return pos.equals(getStorePos());
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
        List<EntityBlockSwapper> swappers = level.getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
        if (!level.isClientSide) {
            boolean canReplace = true;
            for (EntityBlockSwapper swapper : swappers) {
                if (swapper == this) continue;
                if (swapper.isBlockPosInsideSwapper(pos)) {
                    canReplace = false;
                    break;
                }
            }
            if (canReplace) {
                if (breakParticlesEnd) level.destroyBlock(pos, false);
                level.setBlock(pos, getOrigBlock(), 19);
            }
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (canRestoreBlock()) restoreBlock();
    }

    protected boolean canRestoreBlock() {
        return tickCount > duration && level.getEntitiesOfClass(Player.class, getBoundingBox()).isEmpty();
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

    // Like the regular block swapper, but clears out a whole cylinder for the sculptor's test
    public static class EntityBlockSwapperSculptor extends EntityBlockSwapper {
        private int height;
        private int radius;
        private BlockState[][][] origStates;

        public EntityBlockSwapperSculptor(EntityType<? extends EntityBlockSwapperSculptor> type, Level world) {
            super(type, world);
            breakParticlesEnd = false;
            this.height = EntitySculptor.TEST_HEIGHT + 3;
            this.radius = EntitySculptor.TEST_RADIUS;
            this.origStates = new BlockState[height][radius * 2][radius * 2];
            setBoundingBox(makeBoundingBox());
        }

        public EntityBlockSwapperSculptor(EntityType<? extends EntityBlockSwapperSculptor> type, Level world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
            super(type, world);
            this.height = EntitySculptor.TEST_HEIGHT + 3;
            this.radius = EntitySculptor.TEST_RADIUS;
            this.origStates = new BlockState[height][radius * 2][radius * 2];
            setStorePos(pos);
            setRestoreTime(duration);
            this.breakParticlesEnd = breakParticlesEnd;
            setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            setBoundingBox(makeBoundingBox());

            // Get any other sculptor block swappers it overlaps with. We need special logic to make sure they can't replace each other's blocks
            List<EntityBlockSwapperSculptor> swapperSculptors = world.getEntitiesOfClass(EntityBlockSwapperSculptor.class, getBoundingBox());

            // Loop over the blocks inside this one and replace them
            if (!world.isClientSide) {
                for (int k = 0; k < height; k++) {
                    for (int i = -radius; i < radius; i++) {
                        for (int j = -radius; j < radius; j++) {
                            BlockPos thisPos = pos.offset(i, k, j);
                            if (isBlockPosInsideSwapper(thisPos)) {
                                if (world.getBlockState(thisPos).getBlock() == Blocks.BEDROCK) continue;
                                origStates[k][i + radius][j + radius] = world.getBlockState(thisPos);
                                if (breakParticlesStart) world.destroyBlock(thisPos, false);
                                world.setBlock(thisPos, newBlock, 19);
                                for (EntityBlockSwapperSculptor swapper : swapperSculptors) {
                                    if (swapper == this) continue;
                                    if (swapper.getOrigBlockAtLocation(thisPos) != null) {
                                        origStates[k][i + radius][j + radius] = swapper.getOrigBlockAtLocation(thisPos);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Now handle any regular block swappers it overlaps with
            List<EntityBlockSwapperSculptor> swappers = world.getEntitiesOfClass(EntityBlockSwapperSculptor.class, getBoundingBox());
            if (!swappers.isEmpty()) {
                for (EntityBlockSwapper swapper : swappers) {
                    if (swapper == this) continue;
                    if (!(swapper instanceof EntityBlockSwapperSculptor)) {
                        setOrigBlockAtLocation(swapper.getStorePos(), swapper.getOrigBlock());
                    }
                }
            }
        }

        @Override
        public boolean isBlockPosInsideSwapper(BlockPos pos) {
            return new Vec2(pos.getX() - this.getStorePos().getX(), pos.getZ() - this.getStorePos().getZ()).length() < EntitySculptor.testRadiusAtHeight(pos.getY() - getY()) && getBoundingBox().contains(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        }

        public void setOrigBlockAtLocation(BlockPos pos, BlockState state) {
            if (isBlockPosInsideSwapper(pos)) {
                BlockPos indices = posToArrayIndices(pos);
                origStates[indices.getY()][indices.getX()][indices.getZ()] = state;
            }
        }

        public BlockState getOrigBlockAtLocation(BlockPos pos) {
            if (isBlockPosInsideSwapper(pos)) {
                BlockPos indices = posToArrayIndices(pos);
                return origStates[indices.getY()][indices.getX()][indices.getZ()];
            }
            return null;
        }

        protected BlockPos posToArrayIndices(BlockPos pos) {
            return pos.subtract(getStorePos()).offset(radius, 0, radius);
        }

        @Override
        protected AABB makeBoundingBox() {
            return EntityDimensions.scalable(radius * 2, height).makeBoundingBox(position());
        }

        @Override
        public void restoreBlock() {
            if (!level.isClientSide) {
                List<EntityBlockSwapper> swappers = level.getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
                for (int k = 0; k < height; k++) {
                    for (int i = -radius; i < radius; i++) {
                        for (int j = -radius; j < radius; j++) {
                            if (!level.isClientSide) {
                                BlockPos thisPos = getStorePos().offset(i, k, j);
                                if (isBlockPosInsideSwapper(thisPos)) {
                                    boolean canReplace = true;
                                    for (EntityBlockSwapper swapper : swappers) {
                                        if (swapper == this) continue;
                                        if (swapper.isBlockPosInsideSwapper(thisPos)) {
                                            canReplace = false;
                                            break;
                                        }
                                    }
                                    if (canReplace) {
                                        BlockState restoreState = origStates[k][i + radius][j + radius];
                                        if (restoreState != null) {
                                            if (breakParticlesEnd) level.destroyBlock(thisPos, false);
                                            level.setBlock(thisPos, restoreState, 19);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                discard();
            }
        }

        @Override
        protected boolean canRestoreBlock() {
            return tickCount > duration && level.getEntitiesOfClass(EntitySculptor.class, this.getBoundingBox(), EntitySculptor::isTesting).isEmpty();
        }

        @Override
        public void addAdditionalSaveData(CompoundTag compound) {
            compound.putInt("restoreTime", getRestoreTime());
            compound.putInt("storePosX", getStorePos().getX());
            compound.putInt("storePosY", getStorePos().getY());
            compound.putInt("storePosZ", getStorePos().getZ());
            for (int i = 0; i < radius * 2; i++) {
                for (int j = 0; j < radius * 2; j++) {
                    for (int k = 0; k < height; k++) {
                        BlockState block = origStates[k][i][j];
                        if (block != null) compound.put("block_" + i + "_" + j + "_" + k, NbtUtils.writeBlockState(block));
                    }
                }
            }
        }

        @Override
        public void readAdditionalSaveData(CompoundTag compound) {
            setRestoreTime(compound.getInt("restoreTime"));
            setStorePos(new BlockPos(
                    compound.getInt("storePosX"),
                    compound.getInt("storePosY"),
                    compound.getInt("storePosZ")
            ));
            for (int i = 0; i < radius * 2; i++) {
                for (int j = 0; j < radius * 2; j++) {
                    for (int k = 0; k < height; k++) {
                        Tag blockNBT = compound.get("block_" + i + "_" + j + "_" + k);
                        if (blockNBT != null) {
                            BlockState blockState = NbtUtils.readBlockState((CompoundTag) blockNBT);
                            origStates[k][i][j] = blockState;
                        }
                    }
                }
            }
        }
    }
}
