package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.client.entity.player.ClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityCameraShake extends Entity {
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityCameraShake.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> MAGNITUDE = EntityDataManager.createKey(EntityCameraShake.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> DURATION = EntityDataManager.createKey(EntityCameraShake.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FADE_DURATION = EntityDataManager.createKey(EntityCameraShake.class, DataSerializers.VARINT);

    public EntityCameraShake(EntityType<?> type, World world) {
        super(type, world);
    }

    public EntityCameraShake(World world, Vector3d position, float radius, float magnitude, int duration, int fadeDuration) {
        super(EntityHandler.CAMERA_SHAKE, world);
        setRadius(radius);
        setMagnitude(magnitude);
        setDuration(duration);
        setFadeDuration(fadeDuration);
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAmount(Player player, float delta) {
        float ticksDelta = ticksExisted + delta;
        float timeFrac = 1.0f - (ticksDelta - getDuration()) / (getFadeDuration() + 1.0f);
        float baseAmount = ticksDelta < getDuration() ? getMagnitude() : timeFrac * timeFrac * getMagnitude();
        Vector3d playerPos = player.getEyePosition(delta);
        float distFrac = (float) (1.0f - MathHelper.clamp(getPositionVec().distanceTo(playerPos) / getRadius(), 0, 1));
        return baseAmount * distFrac * distFrac;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted > getDuration() + getFadeDuration()) remove();
    }

    @Override
    protected void registerData() {
        getDataManager().register(RADIUS, 10.0f);
        getDataManager().register(MAGNITUDE, 1.0f);
        getDataManager().register(DURATION, 0);
        getDataManager().register(FADE_DURATION, 5);
    }

    public float getRadius() {
        return getDataManager().get(RADIUS);
    }

    public void setRadius(float radius) {
        getDataManager().set(RADIUS, radius);
    }

    public float getMagnitude() {
        return getDataManager().get(MAGNITUDE);
    }

    public void setMagnitude(float magnitude) {
        getDataManager().set(MAGNITUDE, magnitude);
    }

    public int getDuration() {
        return getDataManager().get(DURATION);
    }

    public void setDuration(int duration) {
        getDataManager().set(DURATION, duration);
    }

    public int getFadeDuration() {
        return getDataManager().get(FADE_DURATION);
    }

    public void setFadeDuration(int fadeDuration) {
        getDataManager().set(FADE_DURATION, fadeDuration);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        setRadius(compound.getFloat("radius"));
        setMagnitude(compound.getFloat("magnitude"));
        setDuration(compound.getInt("duration"));
        setFadeDuration(compound.getInt("fade_duration"));
        ticksExisted = compound.getInt("ticks_existed");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putFloat("radius", getRadius());
        compound.putFloat("magnitude", getMagnitude());
        compound.putInt("duration", getDuration());
        compound.putInt("fade_duration", getFadeDuration());
        compound.putInt("ticks_existed", ticksExisted);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void cameraShake(World world, Vector3d position, float radius, float magnitude, int duration, int fadeDuration) {
        if (!world.isRemote) {
            EntityCameraShake cameraShake = new EntityCameraShake(world, position, radius, magnitude, duration, fadeDuration);
            world.addEntity(cameraShake);
        }
    }
}
