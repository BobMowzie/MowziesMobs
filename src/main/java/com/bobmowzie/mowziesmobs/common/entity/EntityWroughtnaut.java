package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.common.animation.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

import java.util.List;

public class EntityWroughtnaut extends MMEntityBase
{
    public double walkFrame;
    public ControlledAnimation walkAnim = new ControlledAnimation(10);
    public boolean swingDirection = false;
    public boolean vulnerable = false;
    private int attacksWithoutVertical = 0;

    public EntityWroughtnaut(World world)
    {
        super(world);
        deathLength = 130;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimFWNAttack(this, 50, "mowziesmobs:wroughtnautWhoosh", 4F, 5.5F, 100F));
        tasks.addTask(1, new AnimFWNVerticalAttack(this, 105, "mowziesmobs:wroughtnautWhoosh", 1F, 5.5F, 40F));
        tasks.addTask(1, new AnimTakeDamage(this, 15));
        tasks.addTask(1, new AnimDie(this, deathLength));
        tasks.addTask(1, new AnimActivate(this, 45));
        tasks.addTask(1, new AnimDeactivate(this, 15));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 30;
        setSize(2.7F, 3.7F);
        active = false;
    }

    public int getAttack()
    {
        return 30;
    }

    public String getHurtSound()
    {
        return "mowziesmobs:wroughtnautHurt1";
    }

    public String getDeathSound()
    {
        playSound("mowziesmobs:wroughtnautScream", 1, 1);
        return null;
    }

    protected String getLivingSound()
    {
        if (getAnimID() == 0 && getActive() == 1)
        {
            int i = MathHelper.getRandomIntegerInRange(rand, 0, 4);
            if (i == 0) playSound("mowziesmobs:wroughtnautGrunt1", 1, 1);
            if (i == 1) playSound("mowziesmobs:wroughtnautGrunt3", 1, 1);
            if (i == 2) playSound("mowziesmobs:wroughtnautShout1", 1, 1);
            if (i == 3) playSound("mowziesmobs:wroughtnautShout2", 1, 1);
            if (i == 4) playSound("mowziesmobs:wroughtnautShout3", 1, 1);
        }
        return null;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40);
    }

    public boolean attackEntityFrom(DamageSource source, float p_70097_2_)
    {
        if (vulnerable && source.getEntity() != null)
        {
            int arc = 220;
            Entity entitySource = source.getEntity();
            float entityHitAngle = (float) ((Math.atan2(entitySource.posZ - posZ, entitySource.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = renderYawOffset % 360;
            if (entityHitAngle < 0) entityHitAngle += 360;
            if (entityAttackingAngle < 0) entityAttackingAngle += 360;
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            if ((entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -arc + 90 / 2))
            {
                playSound("minecraft:random.anvil_land", 0.4F, 2F);
                return false;
            }
            else
            {
                if (currentAnim != null) currentAnim.resetTask();
                return super.attackEntityFrom(source, p_70097_2_);
            }
        }
        else playSound("minecraft:random.anvil_land", 0.4F, 2F);
        return false;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (getActive() == 0 && getAttackTarget() != null && targetDistance <= 5 && getAnimID() == 0)
        {
            AnimationAPI.sendAnimPacket(this, MMAnimation.ACTIVATE.animID());
            setActive((byte) 1);
        }

        if (isClientWorld() && getActive() == 1 && getAttackTarget() == null && moveForward == 0 && getAnimID() == 0 && Math.abs(posX - getRestPosX()) <= 4 && Math.abs(posY - getRestPosY()) <= 4 && Math.abs(posZ - getRestPosZ()) <= 4)
        {
            AnimationAPI.sendAnimPacket(this, MMAnimation.DEACTIVATE.animID());
            setActive((byte) 0);
        }

        if (getActive() == 0)
        {
            posX = prevPosX;
            posZ = prevPosZ;
            posY = prevPosY;
            rotationYaw = prevRotationYaw;
        }

        if (getAttackTarget() != null && getActive() == 1)
        {
            if (getAnimID() == 0) getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.2D);
            else getNavigator().clearPathEntity();

            if (targetDistance <= 3.5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 3 && getAnimID() == 0)
            {
                int i = (int) (3 * Math.random() + 0.5);
                if (attacksWithoutVertical == 4) i = 0;
                if (i == 0)
                {
                    AnimationAPI.sendAnimPacket(this, 5);
                    attacksWithoutVertical = 0;
                }
                else AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
            }
        }
        else if (!getNavigator().tryMoveToXYZ((double) getRestPosX(), (double) getRestPosY(), (double) getRestPosZ(), 0.2D))
        {
            setRestPosX((int) posX);
            setRestPosY((int) posY);
            setRestPosZ((int) posZ);
        }

        if (getAnimID() == MMAnimation.ATTACK.animID() && getAnimTick() == 1)
        {
            attacksWithoutVertical++;
            int i = (int) (Math.random() + 0.5);
            if (i == 0) swingDirection = false;
            if (i == 1) swingDirection = true;
        }

        if (getAnimID() == MMAnimation.ACTIVATE.animID())
        {
            if (getAnimTick() == 1) playSound("mowziesmobs:wroughtnautGrunt2", 1, 1);
            if (getAnimTick() == 27) playSound("mob.zombie.metal", 0.5F, 0.5F);
            if (getAnimTick() == 44) playSound("mob.zombie.metal", 0.5F, 0.5F);
        }

        if (getAnimID() == 5 && getAnimTick() == 29)
        {
            int i = MathHelper.floor_double(posX + 4 * Math.cos((renderYawOffset + 90) * Math.PI / 180));
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double) yOffset);
            int k = MathHelper.floor_double(posZ + 4 * Math.sin((renderYawOffset + 90) * Math.PI / 180));
            Block block = worldObj.getBlock(i, j, k);

            if (block.getMaterial() != Material.air)
            {
                for (int n = 0; n <= 20; n++)
                    worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + worldObj.getBlockMetadata(i, j, k), posX + 4.5 * Math.cos((renderYawOffset + 90) * Math.PI / 180) + ((double) rand.nextFloat() - 0.5D) * (double) width, boundingBox.minY + 0.1D, posZ + 4.5 * Math.sin((renderYawOffset + 90) * Math.PI / 180) + ((double) rand.nextFloat() - 0.5D) * (double) width, 4.0D * ((double) rand.nextFloat() - 0.5D), 3D, ((double) rand.nextFloat() - 0.5D) * 4.0D);
            }
        }

        double walkFrameIncrement = 1.5 * Math.pow(Math.sin((Math.PI * 0.05) * (frame - 9)), 2) + 0.25;
        walkFrame += walkFrameIncrement;

        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = (float) Math.sqrt(moveX * moveX + moveZ * moveZ);
        if (speed > 0.02)
        {
            if (getAnimID() == 0) walkAnim.increaseTimer();
        }
        else walkAnim.decreaseTimer();
        if (getAnimID() != 0) walkAnim.decreaseTimer(2);

        if (frame % 20 == 3 && speed > 0.03 && getAnimID() == 0 && active) playSound("mob.zombie.metal", 0.5F, 0.5F);

        List<EntityLivingBase> nearestEntities = getEntityLivingBaseNearby(2.2, 2.2, 4, 2.2);
        for (Entity entity : nearestEntities)
        {
            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            entity.motionX = -0.1 * Math.cos(angle);
            entity.motionZ = -0.1 * Math.sin(angle);
        }
    }

    public void onSpawn()
    {
        System.out.print("On Spawn");
        setRestPosX((int) posX);
        setRestPosY((int) posY);
        setRestPosZ((int) posZ);
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        onSpawn();
        return super.onSpawnWithEgg(p_110161_1_);
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(29, 0);
        dataWatcher.addObject(30, 0);
        dataWatcher.addObject(31, 0);
        dataWatcher.addObject(28, (byte) 0);
    }

    public int getRestPosX()
    {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setRestPosX(Integer restPosX)
    {
        dataWatcher.updateObject(29, restPosX);
    }

    public int getRestPosY()
    {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void setRestPosY(Integer restPosY)
    {
        dataWatcher.updateObject(30, restPosY);
    }

    public int getRestPosZ()
    {
        return dataWatcher.getWatchableObjectInt(31);
    }

    public void setRestPosZ(Integer restPosZ)
    {
        dataWatcher.updateObject(31, restPosZ);
    }

    public byte getActive()
    {
        return dataWatcher.getWatchableObjectByte(28);
    }

    public void setActive(Byte active)
    {
        dataWatcher.updateObject(28, active);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("restPosX", getRestPosX());
        compound.setInteger("restPosY", getRestPosY());
        compound.setInteger("restPosZ", getRestPosZ());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setRestPosX(compound.getInteger("restPosX"));
        setRestPosY(compound.getInteger("restPosY"));
        setRestPosZ(compound.getInteger("restPosZ"));
    }
}