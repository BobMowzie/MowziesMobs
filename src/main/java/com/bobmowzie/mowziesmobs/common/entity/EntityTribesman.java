package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 7/9/15.
 */

public class EntityTribesman extends MMEntityBase {
    public EntityTribesman[] pack;
    public EntityTribesman(World world) {
        super(world);
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 1, true));
        tasks.addTask(5, new EntityAIWander(this, 0.4));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        setType(1);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (getAttackTarget() != null)
        {
            if (targetDistance > 7) getNavigator().tryMoveToXYZ(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 0.6);
            else
            {
                circleEntity(getAttackTarget(), 7, 0.3f, true);
            }
        }
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        if (getType() == 1) pack = new EntityTribesman[]{new EntityTribesman(worldObj), new EntityTribesman(worldObj), new EntityTribesman(worldObj), new EntityTribesman(worldObj)};
        else pack = new EntityTribesman[]{};
        for(int i = 0; i < pack.length; i++)
        {
            pack[i].setType(0);
            pack[i].setPosition(posX + 0.1 * i, posY, posZ);
            worldObj.spawnEntityInWorld(pack[i]);
        }
        return super.onSpawnWithEgg(p_110161_1_);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(29, 0);
    }

    public int getType()
    {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setType(Integer type)
    {
        dataWatcher.updateObject(29, type);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("type", getType());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setType(compound.getInteger("type"));
    }
}
