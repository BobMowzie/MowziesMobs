package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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

    public EntityRing(World world) {
        super(world);
    }

    public EntityRing(World world, float x, float y, float z, Vec3d facing, int duration, float r, float g, float b, float opacity, float size, boolean facesCamera) {
        this(world);
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

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted >= duration) setDead();
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    public float interpolate(float delta) {
        return (ticksExisted + delta)/duration;
    }
}
