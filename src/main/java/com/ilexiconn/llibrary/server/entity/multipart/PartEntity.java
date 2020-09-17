package com.ilexiconn.llibrary.server.entity.multipart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PartEntity extends Entity {
    protected EntityLiving parent;

    protected float radius;
    protected float angleYaw;
    protected float offsetY;

    protected float damageMultiplier;

    public PartEntity(EntityLiving parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(parent.world);
        this.setSize(sizeX, sizeY);
        this.parent = parent;

        this.radius = radius;
        this.angleYaw = (angleYaw + 90.0F) * ((float) Math.PI / 180.0F);
        this.offsetY = offsetY;

        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public void onUpdate() {
        this.setPositionAndUpdate(this.parent.posX + this.radius * Math.cos(this.parent.renderYawOffset * (Math.PI / 180.0F) + this.angleYaw), this.parent.posY + this.offsetY, this.parent.posZ + this.radius * Math.sin(this.parent.renderYawOffset * (Math.PI / 180.0F) + this.angleYaw));
        if (!this.world.isRemote) {
            this.collideWithNearbyEntities();
        }
        if (this.parent.isDead) {
            this.world.removeEntityDangerously(this);
        }

        super.onUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        return this.parent.attackEntityFrom(source, damage * this.damageMultiplier);
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return this == entity || this.parent == entity;
    }

    @Override
    public void entityInit() {

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTag) {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTag) {

    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public void collideWithNearbyEntities() {
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        entities.stream().filter(entity -> entity != this.parent && !(entity instanceof PartEntity) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(this.parent));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }
}
