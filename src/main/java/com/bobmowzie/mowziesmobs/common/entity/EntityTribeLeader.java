package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.animation.MMAnimBase;
import net.ilexiconn.llibrary.client.model.modelbase.ControlledAnimation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

/**
 * Created by jnad325 on 7/9/15.
 */
public class EntityTribeLeader extends MMEntityBase {
    int direction = 0;
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    private boolean blocksByFeet = true;
    public EntityTribeLeader(World world) {
        super(world);
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
        tasks.addTask(2, new MMAnimBase(this, 1, 40));
        setSize(1.5f, 2.4f);
        if (getDirection() == 0) setDirection(rand.nextInt(4) + 1);

    }

    public EntityTribeLeader(World world, int direction) {
        super(world);
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
        tasks.addTask(2, new MMAnimBase(this, 1, 40));
        setSize(1.5f, 2.4f);
        setDirection(direction);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1) direction = getDirection();
        repelEntities(2.5f, 2.5f, 2.5f, 2.5f);
        if (direction == 1) setRotation(0, 0);
        if (direction == 2) setRotation(90, 0);
        if (direction == 3) setRotation(180, 0);
        if (direction == 4) setRotation(270, 0);
        renderYawOffset = rotationYaw;

        if (ticksExisted % 20 == 0) {
            if (checkBlocksByFeet()) blocksByFeet = true;
            else blocksByFeet = false;
        }

        if (blocksByFeet) legsUp.increaseTimer();
        else legsUp.decreaseTimer();

        if (getAnimID() == 0 && rand.nextInt(200) == 0) AnimationAPI.sendAnimPacket(this, 1);
    }

    private boolean checkBlocksByFeet()
    {
        Block blockLeft;
        Block blockRight;
        if (direction == 1) {
            blockLeft = worldObj.getBlock((int) posX, Math.round((float)(posY - 1)), (int) posZ);
            blockRight = worldObj.getBlock((int) posX - 2, Math.round((float)(posY - 1)), (int) posZ);
        }
        else if (direction == 2) {
            blockLeft = worldObj.getBlock((int) posX - 2, Math.round((float)(posY - 1)), (int) posZ);
            blockRight = worldObj.getBlock((int) posX - 2, Math.round((float)(posY - 1)), (int) posZ - 2);
        }
        else if (direction == 3) {
            blockLeft = worldObj.getBlock((int) posX - 2, Math.round((float)(posY - 1)), (int) posZ - 2);
            blockRight = worldObj.getBlock((int) posX, Math.round((float)(posY - 1)), (int) posZ - 2);
        }
        else if (direction == 4) {
            blockLeft = worldObj.getBlock((int) posX, Math.round((float)(posY - 1)), (int) posZ - 2);
            blockRight = worldObj.getBlock((int) posX, Math.round((float)(posY - 1)), (int) posZ);
        }
        else return false;

        if (blockLeft instanceof BlockAir && blockRight instanceof BlockAir) return false;
        return true;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(28, 0);
    }

    public int getDirection()
    {
        return dataWatcher.getWatchableObjectInt(28);
    }

    public void setDirection(Integer direction)
    {
        dataWatcher.updateObject(28, direction);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("direction", getDirection());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setDirection(compound.getInteger("direction"));
    }
}
