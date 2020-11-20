package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by Josh on 4/15/2017.
 */
public class EntityRing extends Entity {
    public float size;
    public float r, g, b;
    public float opacity;
    public boolean facesCamera;
    public Vec3d facing;
    public int duration;

    public EntityRing(EntityType<? extends EntityRing> type, World world) {
        super(type, world);
    }

    public EntityRing(EntityType<? extends EntityRing> type, World world, float x, float y, float z, Vec3d facing, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera) {
        this(type, world);
        this.setPosition(x, y, z);
        this.facing = facing;
        this.duration = duration;
        this.r = r;
        this.b = b;
        this.g = g;
        this.opacity = opacity;
        this.size = size;
        this.facesCamera = facesCamera;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted >= duration) remove();
        move(MoverType.SELF, getMotion());
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public float interpolate(float delta) {
        return (ticksExisted + delta)/duration;
    }
}
