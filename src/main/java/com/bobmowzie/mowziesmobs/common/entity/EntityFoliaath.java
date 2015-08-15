package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.common.animation.AnimBasicAttack;
import com.bobmowzie.mowziesmobs.common.animation.AnimDie;
import com.bobmowzie.mowziesmobs.common.animation.AnimTakeDamage;
import com.bobmowzie.mowziesmobs.common.animation.MMAnimation;
import com.bobmowzie.mowziesmobs.common.item.MMItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

public class EntityFoliaath extends MMEntityBase
{
    private static final int CAN_DESPAWN_ID = 30;
    private static final int ACTIVATE_TARGET_ID = 31;

    private static final int ACTIVATE_DURATION = 30;

    public IntermittentAnimation openMouth = new IntermittentAnimation(this, 15, 30, 50, !worldObj.isRemote);
    public ControlledAnimation activate = new ControlledAnimation(ACTIVATE_DURATION);
    public ControlledAnimation deathFlail = new ControlledAnimation(5);
    public ControlledAnimation stopDance = new ControlledAnimation(10);
    public int lastTimeDecrease = 0;
    int resettingTargetTimer = 0;
    private double prevOpenMouth;
    private double prevActivate;
    private int activateTarget;

    public EntityFoliaath(World world)
    {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new AnimBasicAttack(this, 1, 14, "mowziesmobs:foliaathbite1", "", 2, 4.5F, 1, 3));
        tasks.addTask(1, new AnimTakeDamage(this, 10));
        tasks.addTask(1, new AnimDie(this, deathLength));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCreature.class, 0, true));
        setSize(0.5F, 2.5F);
        experienceValue = 10;
        deathLength = 50;
        addIntermittentAnimation(openMouth);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(CAN_DESPAWN_ID, (byte) 1);
        dataWatcher.addObject(ACTIVATE_TARGET_ID, (byte) 0);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10);
    }

    @Override
    public int getAttack()
    {
        return 10;
    }

    @Override
    public String getHurtSound()
    {
        return "mowziesmobs:foliaathhurt";
    }

    @Override
    public String getDeathSound()
    {
        return "mowziesmobs:foliaathdie";
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        // Open mouth animation
        if (getAnimID() == 0 && !activate.canIncreaseTimer())
        {
            openMouth.update();
        }
        else
        {
            openMouth.stop();
        }

        if (activate.getAnimationFraction() >= 0.8F)
        {
            if (!active)
            {
                active = true;
            }
        }
        else if (activate.getAnimationFraction() < 0.8F)
        {
            if (active)
            {
                active = false;
            }
        }

        // Sounds
        if (frame % 13 == 3 && getAnimID() != MMAnimation.DIE.animID())
        {
            if (openMouth.getTimeRunning() >= 10)
            {
                playSound("mowziesmobs:foliaathpant1", 1, 1);
            }
            else if (activate.getTimer() >= 25)
            {
                playSound("mowziesmobs:foliaathpant2", 1, 1);
            }
        }

        int openMouthTime = openMouth.getTimeRunning();
        if (prevOpenMouth - openMouthTime < 0)
        {
            if (openMouthTime == 1)
            {
                playSound("mowziesmobs:foliaathrustle", 1, 1);
            }
            else if (openMouthTime == 13)
            {
                playSound("mowziesmobs:foliaathgrunt", 1, 1);
            }
        }

        prevOpenMouth = openMouthTime;

        int activateTime = activate.getTimer();
        if (!worldObj.isRemote)
        {
            String sound = null;
            if (prevActivate - activateTime < 0)
            {
                switch (activateTime)
                {
                case 1:
                    sound = "mowziesmobs:foliaathrustle";
                    break;
                case 5:
                    sound = "mowziesmobs:foliaathemerge";
                    break;
                }
            }
            else if (prevActivate - activateTime > 0)
            {
                switch (activateTime)
                {
                case 24:
                    sound = "mowziesmobs:foliaathretreat";
                    break;
                case 28:
                    sound = "mowziesmobs:foliaathrustle";
                    break;
                }
            }
            if (sound != null)
            {
                playSound(sound, 1, 1);
            }
        }

        prevActivate = activateTime;

        // Targetting, attacking, and activating
        renderYawOffset = 0;
        rotationYaw = 0;

        if (getAttackTarget() instanceof EntityFoliaath || getAttackTarget() instanceof EntityBabyFoliaath)
        {
            setAttackTarget(null);
        }

        if (resettingTargetTimer > 0)
        {
            if (!worldObj.isRemote)
            {
                rotationYawHead = prevRotationYawHead;
            }
        }

        if (getAttackTarget() != null)
        {
            
            rotationYawHead = targetAngle;

            if (targetDistance <= 4.5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 2 && getAnimID() == 0 && active)
            {
                AnimationAPI.sendAnimPacket(this, 1);
            }

            if (targetDistance <= 11)
            {
                setActivateTarget(ACTIVATE_DURATION);
                lastTimeDecrease = 0;
            }
            else if (lastTimeDecrease <= 30 && getAnimID() == 0)
            {
                setActivateTarget(0);
                lastTimeDecrease++;
            }
        }
        else if (!worldObj.isRemote && lastTimeDecrease <= 30 && getAnimID() == 0 && resettingTargetTimer == 0)
        {
            setActivateTarget(0);
            lastTimeDecrease++;
        }

        if (getAnimID() == MMAnimation.DIE.animID())
        {
            if (getAnimTick() <= 12)
            {
                deathFlail.increaseTimer();
            }
            else
            {
                deathFlail.decreaseTimer();
            }
            stopDance.increaseTimer();
            setActivateTarget(ACTIVATE_DURATION);
        }

        if (resettingTargetTimer > 0)
        {
            resettingTargetTimer--;
        }

        if (getAttackTarget() != null && frame % 20 == 0 && getAnimID() == 0)
        {
            setAttackTarget(null);
            resettingTargetTimer = 2;
        }
        if (activateTarget == activateTime)
        {
            activateTarget = getActivateTarget();
        }
        if (activateTime < activateTarget && activate.canIncreaseTimer() || activateTime > activateTarget && activate.canDecreaseTimer())
        {
            activate.increaseTimer(activateTime < activateTarget ? 1 : -2);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount)
    {
        return active && super.attackEntityFrom(damageSource, amount);
    }

    @Override
    public void applyEntityCollision(Entity entity)
    {
        if (entity.riddenByEntity != this && entity.ridingEntity != this)
        {
            double dx = entity.posX - posX;
            double dz = entity.posZ - posZ;
            double majorAxis = MathHelper.abs_max(dx, dz);

            if (majorAxis >= 0.009999999)
            {
                majorAxis = MathHelper.sqrt_double(majorAxis);
                dx /= majorAxis;
                dz /= majorAxis;
                double reciprocal = 1 / majorAxis;

                if (reciprocal > 1)
                {
                    reciprocal = 1;
                }

                dx *= reciprocal;
                dz *= reciprocal;
                dx *= 0.05;
                dz *= 0.05;
                dx *= 1 - entityCollisionReduction;
                dz *= 1 - entityCollisionReduction;
                addVelocity(dx, 0, dz);
                entity.addVelocity(dx, 0, dz);
            }
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        // return active;
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        if (worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox))
        {
            int x = MathHelper.floor_double(posX);
            int y = MathHelper.floor_double(boundingBox.minY);
            int z = MathHelper.floor_double(posZ);

            if (y < 63)
            {
                return false;
            }

            Block block = worldObj.getBlock(x, y - 1, z);

            if (block == Blocks.grass || block.isLeaves(worldObj, x, y - 1, z))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onKillEntity(EntityLivingBase entity)
    {
        addPotionEffect(new PotionEffect(Potion.regeneration.id, 300, 1, true));
    }

    @Override
    protected Item getDropItem()
    {
        return rand.nextBoolean() ? MMItems.itemFoliaathSeed : null;
    }

    public boolean canDespawn()
    {
        return dataWatcher.getWatchableObjectByte(CAN_DESPAWN_ID) == 1;
    }

    public void setCanDespawn(boolean canDespawn)
    {
        dataWatcher.updateObject(CAN_DESPAWN_ID, (byte) (canDespawn ? 1 : 0));
    }

    public void setActivateTarget(int activateTarget)
    {
        dataWatcher.updateObject(ACTIVATE_TARGET_ID, (byte) activateTarget);
    }

    public int getActivateTarget()
    {
        return dataWatcher.getWatchableObjectByte(ACTIVATE_TARGET_ID);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("canDespawn", canDespawn());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setCanDespawn(compound.getBoolean("canDespawn"));
    }
}