package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;

public class EntityFallingBlock extends Entity {
    public static float GRAVITY = 0.1f;
    public double prevMotionX, prevMotionY, prevMotionZ;

    private static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TICKS_EXISTED = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> MODE = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> ANIM_V_Y = SynchedEntityData.defineId(EntityFallingBlock.class, EntityDataSerializers.FLOAT);

    public enum EnumFallingBlockMode {
        MOBILE,
        POPUP_ANIM
    }

    public float animY = 0;
    public float prevAnimY = 0;

    public EntityFallingBlock(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        setBlock(Blocks.DIRT.defaultBlockState());
        setDuration(70);
    }

    public EntityFallingBlock(EntityType<?> entityTypeIn, Level worldIn, int duration, BlockState blockState) {
        super(entityTypeIn, worldIn);
        setBlock(blockState);
        setDuration(duration);
    }

    public EntityFallingBlock(EntityType<?> entityTypeIn, Level worldIn, BlockState blockState, float vy) {
        super(entityTypeIn, worldIn);
        setBlock(blockState);
        setMode(EnumFallingBlockMode.POPUP_ANIM);
        setAnimVY(vy);
    }

    @Override
    public void onAddedToWorld() {
        if (getDeltaMovement().x() > 0 || getDeltaMovement().z() > 0) setYRot((float) ((180f/Math.PI) * Math.atan2(getDeltaMovement().x(), getDeltaMovement().z())));
        setXRot(getXRot() + random.nextFloat() * 360);
        super.onAddedToWorld();
    }

    @Override
    public void tick() {
        if (getMode() == EnumFallingBlockMode.POPUP_ANIM) {
            setDeltaMovement(0, 0, 0);
        }
        prevMotionX = getDeltaMovement().x;
        prevMotionY = getDeltaMovement().y;
        prevMotionZ = getDeltaMovement().z;
        super.tick();
        if (getMode() == EnumFallingBlockMode.MOBILE) {
            setDeltaMovement(getDeltaMovement().subtract(0, GRAVITY, 0));
            if (onGround) setDeltaMovement(getDeltaMovement().scale(0.7));
            else setXRot(getXRot() + 15);
            this.move(MoverType.SELF, this.getDeltaMovement());

            if (tickCount > getDuration()) discard() ;
        }
        else {
            float animVY = getAnimVY();
            prevAnimY = animY;
            animY += animVY;
            setAnimVY(animVY - GRAVITY);
            if (animY < -0.5) discard() ;
        }
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(BLOCK_STATE, Optional.of(Blocks.DIRT.defaultBlockState()));
        getEntityData().define(DURATION, 70);
        getEntityData().define(TICKS_EXISTED, 0);
        getEntityData().define(MODE, EnumFallingBlockMode.MOBILE.toString());
        getEntityData().define(ANIM_V_Y, 1f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        Tag blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NbtUtils.readBlockState((CompoundTag) blockStateCompound);
            setBlock(blockState);
        }
        setDuration(compound.getInt("duration"));
        tickCount = compound.getInt("ticksExisted");
        getEntityData().set(MODE, compound.getString("mode"));
        setAnimVY(compound.getFloat("vy"));

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        BlockState blockState = getBlock();
        if (blockState != null) compound.put("block", NbtUtils.writeBlockState(blockState));
        compound.putInt("duration", getDuration());
        compound.putInt("ticksExisted", tickCount);
        compound.putString("mode", getEntityData().get(MODE));
        compound.putFloat("vy", getEntityData().get(ANIM_V_Y));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockState getBlock() {
        Optional<BlockState> bsOp = getEntityData().get(BLOCK_STATE);
        return bsOp.orElse(null);
    }

    public void setBlock(BlockState block) {
        getEntityData().set(BLOCK_STATE, Optional.of(block));
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    public int getTicksExisted() {
        return getEntityData().get(TICKS_EXISTED);
    }

    public void setTicksExisted(int ticksExisted) {
        getEntityData().set(TICKS_EXISTED, ticksExisted);
    }

    public EnumFallingBlockMode getMode() {
        String mode = getEntityData().get(MODE);
        if (mode.isEmpty()) return EnumFallingBlockMode.MOBILE;
        return EnumFallingBlockMode.valueOf(getEntityData().get(MODE));
    }

    private void setMode(EnumFallingBlockMode mode) {
        getEntityData().set(MODE, mode.toString());
    }

    public float getAnimVY() {
        return getEntityData().get(ANIM_V_Y);
    }

    private void setAnimVY(float vy) {
        getEntityData().set(ANIM_V_Y, vy);
    }
}
