package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by jnad325 on 7/23/15.
 */
public class EntityTribeHunter extends EntityTribesman
{
    private static final String ABSENT_LEADER = "";

    private static final int LEADER_UUID_ID = 31;

    private EntityTribeElite leader;

    public int index;

    public EntityTribeHunter(World world)
    {
        super(world);
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCow.class, 0, true));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPig.class, 0, true));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true));
        this.leader = null;
    }

    public EntityTribeHunter(World world, EntityTribeElite leader)
    {
        super(world);
        this.leader = leader;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(LEADER_UUID_ID, ABSENT_LEADER);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (!worldObj.isRemote)
        {
            if (leader == null && getLeaderUUID() != ABSENT_LEADER)
            {
                leader = getLeader();
                if (leader != null)
                {
                    leader.addPackMember(this);
                }
            }
            if (leader != null)
            {
                setAttackTarget(leader.getAttackTarget());
            }
        }
    }

    @Override
    protected void updateCircling() {
        if (leader != null) {
            if (!attacking && targetDistance < 5) circleEntity(getAttackTarget(), 7, 0.3f, true, leader.frame, (float) ((index + 1) * (Math.PI * 2) / (leader.getPackSize() + 1)), 1.75f);
            else circleEntity(getAttackTarget(), 7, 0.3f, true, leader.frame, (float) ((index + 1) * (Math.PI * 2) / (leader.getPackSize() + 1)), 1);
        }
        else super.updateCircling();
    }

    @Override
    public void onDeath(DamageSource damageSource)
    {
        super.onDeath(damageSource);
        if (leader != null)
        {
            leader.removePackMember(this);
        }
    }

    public void setLeaderUUID(String uuid)
    {
        dataWatcher.updateObject(LEADER_UUID_ID, uuid);
    }

    public String getLeaderUUID()
    {
        return dataWatcher.getWatchableObjectString(LEADER_UUID_ID);
    }

    public EntityTribeElite getLeader()
    {
        try
        {
            UUID uuid = UUID.fromString(getLeaderUUID());
            if (uuid == null)
            {
                return null;
            }
            List<EntityTribeElite> potentialLeaders = worldObj.getEntitiesWithinAABB(EntityTribeElite.class, boundingBox.expand(32, 32, 32));
            for (EntityTribeElite elite : potentialLeaders)
            {
                if (uuid.equals(elite.getUniqueID()))
                {
                    return elite;
                }
            }
            return null;
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    public void removeLeader()
    {
        setLeaderUUID(ABSENT_LEADER);
        leader = null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("mask", getMask());
        compound.setString("leaderUUID", getLeaderUUID());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setMask(compound.getInteger("mask"));
        setLeaderUUID(compound.getString("leaderUUID"));
    }
}
