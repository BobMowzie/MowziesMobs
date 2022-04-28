package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class EntityCameraShake extends Entity {
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(EntityCameraShake.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAGNITUDE = SynchedEntityData.defineId(EntityCameraShake.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntityCameraShake.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FADE_DURATION = SynchedEntityData.defineId(EntityCameraShake.class, EntityDataSerializers.INT);

    public EntityCameraShake(EntityType<?> type, Level world) {
        super(type, world);
    }

    public EntityCameraShake(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        super(EntityHandler.CAMERA_SHAKE.get(), world);
        setRadius(radius);
        setMagnitude(magnitude);
        setDuration(duration);
        setFadeDuration(fadeDuration);
        setPos(position.x(), position.y(), position.z());
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAmount(Player player, float delta) {
        float ticksDelta = tickCount + delta;
        float timeFrac = 1.0f - (ticksDelta - getDuration()) / (getFadeDuration() + 1.0f);
        float baseAmount = ticksDelta < getDuration() ? getMagnitude() : timeFrac * timeFrac * getMagnitude();
        Vec3 playerPos = player.getEyePosition(delta);
        float distFrac = (float) (1.0f - Mth.clamp(position().distanceTo(playerPos) / getRadius(), 0, 1));
        return baseAmount * distFrac * distFrac;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > getDuration() + getFadeDuration()) discard() ;
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(RADIUS, 10.0f);
        getEntityData().define(MAGNITUDE, 1.0f);
        getEntityData().define(DURATION, 0);
        getEntityData().define(FADE_DURATION, 5);
    }

    public float getRadius() {
        return getEntityData().get(RADIUS);
    }

    public void setRadius(float radius) {
        getEntityData().set(RADIUS, radius);
    }

    public float getMagnitude() {
        return getEntityData().get(MAGNITUDE);
    }

    public void setMagnitude(float magnitude) {
        getEntityData().set(MAGNITUDE, magnitude);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    public int getFadeDuration() {
        return getEntityData().get(FADE_DURATION);
    }

    public void setFadeDuration(int fadeDuration) {
        getEntityData().set(FADE_DURATION, fadeDuration);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        setRadius(compound.getFloat("radius"));
        setMagnitude(compound.getFloat("magnitude"));
        setDuration(compound.getInt("duration"));
        setFadeDuration(compound.getInt("fade_duration"));
        tickCount = compound.getInt("ticks_existed");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("radius", getRadius());
        compound.putFloat("magnitude", getMagnitude());
        compound.putInt("duration", getDuration());
        compound.putInt("fade_duration", getFadeDuration());
        compound.putInt("ticks_existed", tickCount);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void cameraShake(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        if (!world.isClientSide) {
            EntityCameraShake cameraShake = new EntityCameraShake(world, position, radius, magnitude, duration, fadeDuration);
            world.addFreshEntity(cameraShake);
        }
    }
}
