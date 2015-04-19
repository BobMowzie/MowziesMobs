package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.ai.animation.MMAnimBase;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import java.util.ArrayList;
import java.util.List;

public class MMEntityBase extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity
{
    private int animTick;
    private int animID;
    public int frame;
    public float targetDistance;
    public float targetAngle;
    public DamageSource dieSource;
    public MMAnimBase currentAnim = null;

    public MMEntityBase(World world)
    {
        super(world);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        //Server
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        //Server
    }

    @Override
    public ItemStack getHeldItem()
    {
        return null;
    }

    @Override
    public ItemStack getEquipmentInSlot(int p_71124_1_)
    {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_)
    {

    }

    @Override
    public ItemStack[] getLastActiveItems()
    {
        return new ItemStack[0];
    }

    @Override
    public void writeSpawnData(ByteBuf buf)
    {
        //Client
    }

    @Override
    public void readSpawnData(ByteBuf buf)
    {
        //Client
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        frame++;
        if (animID != 0) animTick++;

        if (this.getAttackTarget() != null)
        {
            targetDistance = (float) Math.sqrt((getAttackTarget().posZ - posZ) * (getAttackTarget().posZ - posZ) + (getAttackTarget().posX - posX) * (getAttackTarget().posX - posX));
            targetAngle = (float) (Math.atan2(getAttackTarget().posZ - posZ, getAttackTarget().posX - posX) * (180 / Math.PI) + 90);
        }
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public void setAnimID(int i)
    {
        animID = i;
    }

    @Override
    public void setAnimTick(int i)
    {
        animTick = i;
    }

    @Override
    public int getAnimID()
    {
        return animID;
    }

    @Override
    public int getAnimTick()
    {
        return animTick;
    }

    public int getAttack()
    {
        return 0;
    }

    public List<EntityPlayer> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityPlayer> listEntityPlayers = new ArrayList<EntityPlayer>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityPlayer && getDistanceToEntity(entityNeighbor) <= radius)
                listEntityPlayers.add((EntityPlayer) entityNeighbor);
        }
        return listEntityPlayers;
    }

    public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> listEntityLivingBase = new ArrayList<EntityLivingBase>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityLivingBase && getDistanceToEntity(entityNeighbor) <= radius)
                listEntityLivingBase.add((EntityLivingBase) entityNeighbor);
        }
        return listEntityLivingBase;
    }

    public void sendPacket(AbstractPacket packet)
    {
        if (!worldObj.isRemote)
        {
            MowziesMobs.networkWrapper.sendToAll(packet);
        }
    }
}
