package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Optional;

public class EntityFallingBlock extends Entity {
    public static float GRAVITY = 0.1f;
    public double prevMotionX, prevMotionY, prevMotionZ;

    private static final DataParameter<Optional<BlockState>> BLOCK_STATE = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Integer> DURATION = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TICKS_EXISTED = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.VARINT);

    public EntityFallingBlock(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        setBlock(Blocks.DIRT.getDefaultState());
        setDuration(70);
    }

    public EntityFallingBlock(EntityType<?> entityTypeIn, World worldIn, int duration, BlockState blockState) {
        super(entityTypeIn, worldIn);
        setBlock(blockState);
        setDuration(duration);
    }

    @Override
    public void onAddedToWorld() {
        if (getMotion().getX() > 0 || getMotion().getZ() > 0) rotationYaw = (float) ((180f/Math.PI) * Math.atan2(getMotion().getX(), getMotion().getZ()));
        rotationPitch += rand.nextFloat() * 360;
        super.onAddedToWorld();
    }

    @Override
    public void tick() {
        prevMotionX = getMotion().x;
        prevMotionY = getMotion().y;
        prevMotionZ = getMotion().z;
        super.tick();
        setMotion(getMotion().subtract(0, GRAVITY, 0));
        if (onGround) setMotion(getMotion().scale(0.7));
        else rotationPitch += 15;
        this.move(MoverType.SELF, this.getMotion());

        if (ticksExisted > getDuration()) remove();
    }

    @Override
    protected void registerData() {
        getDataManager().register(BLOCK_STATE, Optional.of(Blocks.DIRT.getDefaultState()));
        getDataManager().register(DURATION, 70);
        getDataManager().register(TICKS_EXISTED, 0);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        INBT blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NBTUtil.readBlockState((CompoundNBT) blockStateCompound);
            setBlock(blockState);
        }
        setDuration(compound.getInt("duration"));
        ticksExisted = compound.getInt("ticksExisted");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        BlockState blockState = getBlock();
        if (blockState != null) compound.put("block", NBTUtil.writeBlockState(blockState));
        compound.putInt("duration", getDuration());
        compound.putInt("ticksExisted", ticksExisted);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockState getBlock() {
        Optional<BlockState> bsOp = getDataManager().get(BLOCK_STATE);
        return bsOp.orElse(null);
    }

    public void setBlock(BlockState block) {
        getDataManager().set(BLOCK_STATE, Optional.of(block));
    }

    public int getDuration() {
        return getDataManager().get(DURATION);
    }

    public void setDuration(int duration) {
        getDataManager().set(DURATION, duration);
    }

    public int getTicksExisted() {
        return getDataManager().get(TICKS_EXISTED);
    }

    public void setTicksExisted(int ticksExisted) {
        getDataManager().set(TICKS_EXISTED, ticksExisted);
    }
}
