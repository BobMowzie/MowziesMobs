package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.message.MessageLinkEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by BobMowzie on 7/8/2018.
 */
public class EntityBlockSwapper extends Entity {
    private static final EntityDataAccessor<BlockState> ORIG_BLOCK_STATE = SynchedEntityData.defineId(EntityBlockSwapper.class, EntityDataSerializers.BLOCK_STATE);
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
        getEntityData().define(ORIG_BLOCK_STATE, Blocks.DIRT.defaultBlockState());
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
        return getEntityData().get(ORIG_BLOCK_STATE);
    }

    public void setOrigBlock(BlockState block) {
        getEntityData().set(ORIG_BLOCK_STATE, block);
    }

    public void restoreBlock() {
        List<EntityBlockSwapper> swappers = level().getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
        if (!level().isClientSide) {
            boolean canReplace = true;
            for (EntityBlockSwapper swapper : swappers) {
                if (swapper == this) continue;
                if (swapper.isBlockPosInsideSwapper(pos)) {
                    canReplace = false;
                    break;
                }
            }
            if (canReplace) {
                if (breakParticlesEnd) level().destroyBlock(pos, false);
                level().setBlock(pos, getOrigBlock(), 19);
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
        return tickCount > duration && level().getEntitiesOfClass(Player.class, getBoundingBox()).isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        BlockState blockState = getEntityData().get(ORIG_BLOCK_STATE);
        compound.put("block", NbtUtils.writeBlockState(blockState));
        compound.putInt("restoreTime", getRestoreTime());
        compound.putInt("storePosX", getStorePos().getX());
        compound.putInt("storePosY", getStorePos().getY());
        compound.putInt("storePosZ", getStorePos().getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        Tag blockNBT = compound.get("block");
        if (blockNBT != null) {
            BlockState blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), (CompoundTag) blockNBT);
            setOrigBlock(blockState);
        }
        setRestoreTime(compound.getInt("restoreTime"));
        setStorePos(new BlockPos(
                compound.getInt("storePosX"),
                compound.getInt("storePosY"),
                compound.getInt("storePosZ")
        ));
    }

    public static class EntityBlockSwapperTunneling extends EntityBlockSwapper implements ILinkedEntity {
        private LivingEntity cachedTunneler;
        private static final EntityDataAccessor<Optional<UUID>> TUNNELER = SynchedEntityData.defineId(EntityBlockSwapper.EntityBlockSwapperTunneling.class, EntityDataSerializers.OPTIONAL_UUID);

        public EntityBlockSwapperTunneling(EntityType<? extends EntityBlockSwapperTunneling> type, Level world) {
            super(type, world);
        }

        public EntityBlockSwapperTunneling(EntityType<? extends EntityBlockSwapperTunneling> type, Level world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd, LivingEntity tunneler) {
            super(type, world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            cachedTunneler = tunneler;
            if (!world.isClientSide && tunneler != null) {
                this.setTunnelerID(tunneler.getUUID());
            }
        }

        @Override
        public boolean isPickable() {
            return false;
        }

        @Override
        protected void defineSynchedData() {
            super.defineSynchedData();
            getEntityData().define(TUNNELER, Optional.empty());
        }

        public Optional<UUID> getTunnelerID() {
            return getEntityData().get(TUNNELER);
        }

        public void setTunnelerID(UUID id) {
            getEntityData().set(TUNNELER, Optional.of(id));
        }

        public LivingEntity getTunneler() {
            if (this.cachedTunneler != null && !this.cachedTunneler.isRemoved()) {
                return this.cachedTunneler;
            } else if (this.getTunnelerID().isPresent() && this.level() instanceof ServerLevel) {
                Entity entity = ((ServerLevel)this.level()).getEntity(this.getTunnelerID().get());
                if (entity instanceof LivingEntity) {
                    cachedTunneler = (LivingEntity) entity;
                    MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new MessageLinkEntities(this, cachedTunneler));
                }
                return this.cachedTunneler;
            } else {
                return null;
            }
        }

        @Override
        public boolean canCollideWith(Entity p_20303_) {
            return super.canCollideWith(p_20303_);
        }

        @Override
        public boolean canBeCollidedWith() {
            return true;
        }

        // Hacky solution to prevent tunneler from colliding with the block swapper
        @Override
        public Entity getRootVehicle() {
            return getTunneler();
        }

        @Override
        public void link(Entity entity) {
            if (entity instanceof LivingEntity) {
                cachedTunneler = (LivingEntity) entity;
            }
        }

        @Override
        public void readAdditionalSaveData(CompoundTag compound) {
            super.readAdditionalSaveData(compound);
            setTunnelerID(compound.getUUID("tunneler"));
        }

        @Override
        public void addAdditionalSaveData(CompoundTag compound) {
            super.addAdditionalSaveData(compound);
            if (getTunnelerID().isPresent()) {
                compound.putUUID("tunneler", getTunnelerID().get());
            }
        }

        @Override
        public Packet<ClientGamePacketListener> getAddEntityPacket() {
            LivingEntity entity = this.cachedTunneler;
            return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
        }

        @Override
        public void recreateFromPacket(ClientboundAddEntityPacket packet) {
            super.recreateFromPacket(packet);
            Entity entity = this.level().getEntity(packet.getData());
            if (entity instanceof LivingEntity) {
                cachedTunneler = (LivingEntity) entity;
            }
        }
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
            if (!level().isClientSide) {
                List<EntityBlockSwapper> swappers = level().getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
                for (int k = 0; k < height; k++) {
                    for (int i = -radius; i < radius; i++) {
                        for (int j = -radius; j < radius; j++) {
                            if (!level().isClientSide) {
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
                                            if (breakParticlesEnd) level().destroyBlock(thisPos, false);
                                            level().setBlock(thisPos, restoreState, 19);
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
            return tickCount > duration && level().getEntitiesOfClass(EntitySculptor.class, this.getBoundingBox(), EntitySculptor::isTesting).isEmpty();
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
                            BlockState blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), (CompoundTag) blockNBT);
                            origStates[k][i][j] = blockState;
                        }
                    }
                }
            }
        }
    }
}
