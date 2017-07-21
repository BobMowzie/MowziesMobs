package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by Josh on 7/20/2017.
 */
public class EntityFrozenController extends Entity {
    public EntityFrozenController(World world) {
        super(world);
        setSize(0, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted >= 70) setDead();
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

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return 0;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger))
        {
            if (passenger instanceof EntityPlayer) passenger.setPosition(this.posX, this.posY, this.posZ);
            else passenger.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
}
