package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.common.animation.AnimBasicAttack;
import com.bobmowzie.mowziesmobs.common.animation.AnimDie;
import com.bobmowzie.mowziesmobs.common.animation.AnimTakeDamage;
import com.bobmowzie.mowziesmobs.common.animation.MMAnimation;
import com.bobmowzie.mowziesmobs.common.item.MMItems;
import com.bobmowzie.mowziesmobs.common.message.foliaath.MessageDecreaseTimer;
import com.bobmowzie.mowziesmobs.common.message.foliaath.MessageIncreaseTimer;
import com.bobmowzie.mowziesmobs.common.message.foliaath.MessageSetActiveFalse;
import com.bobmowzie.mowziesmobs.common.message.foliaath.MessageSetActiveTrue;
import cpw.mods.fml.common.FMLCommonHandler;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
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
    public IntermittentAnimation openMouth = new IntermittentAnimation(15, 30, 50);
    public ControlledAnimation activate = new ControlledAnimation(30);
    public ControlledAnimation deathFlail = new ControlledAnimation(5);
    public ControlledAnimation stopDance = new ControlledAnimation(10);
    public int lastTimeDecrease = 0;
    int resettingTargetTimer = 0;
    private double prevOpenMouth;
    private double prevActivate;

    public EntityFoliaath(World world)
    {
        super(world);
        deathLength = 50;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new AnimBasicAttack(this, 14, "mowziesmobs:foliaathbite1", 2F, 4.5F));
        tasks.addTask(1, new AnimTakeDamage(this, 10));
        tasks.addTask(1, new AnimDie(this, deathLength));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCreature.class, 0, true));
        experienceValue = 10;
        setSize(0.5F, 2.5F);
    }

    public int getAttack()
    {
        return 10;
    }

    public String getHurtSound()
    {
        return "mowziesmobs:foliaathhurt";
    }

    public String getDeathSound()
    {
        return "mowziesmobs:foliaathdie";
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10);
    }

    public void onUpdate()
    {
        super.onUpdate();
        //Open mouth animation
        if (worldObj.isRemote)
        {
            if (getAnimID() == 0 && activate.getAnimationFraction() == 1) openMouth.runAnimation();
            else openMouth.stopAnimation();
        }

        if (worldObj.isRemote && activate.getAnimationFraction() >= 0.8)
        {
            if (!active)
            {

                sendPacket2(new MessageSetActiveTrue(getEntityId()));
                active = true;
            }
        }
        else if (worldObj.isRemote && activate.getAnimationFraction() < 0.8)
        {
            if (active)
            {
                sendPacket2(new MessageSetActiveFalse(getEntityId()));
                active = false;
            }
        }

        //Sounds
        if (frame % 13 == 3 && getAnimID() != MMAnimation.DIE.animID())
        {
            if (openMouth.getTimer() >= 10) MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathpant1");
            else if (activate.getTimer() >= 25) MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathpant2");
        }

        if (openMouth.getTimer() == 1 && prevOpenMouth - openMouth.getTimer() < 0)
            MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathrustle");
        if (openMouth.getTimer() == 13 && prevOpenMouth - openMouth.getTimer() < 0)
            MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathgrunt");
        prevOpenMouth = openMouth.getTimer();

        if (activate.getTimer() == 1 && prevActivate - activate.getTimer() < 0)
            MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathrustle");
        if (activate.getTimer() == 5 && prevActivate - activate.getTimer() < 0)
            MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathemerge");
        if (activate.getTimer() == 28 && prevActivate - activate.getTimer() > 0)
            MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathrustle");
        if (activate.getTimer() == 24 && prevActivate - activate.getTimer() > 0)
            MowziesMobs.playSound(getEntityId(), "mowziesmobs:foliaathretreat");
        prevActivate = activate.getTimer();

        //Targetting, attacking, and activating
        renderYawOffset = 0;
        rotationYaw = 0;

        if (getAttackTarget() instanceof EntityFoliaath || getAttackTarget() instanceof EntityBabyFoliaath)
            setAttackTarget(null);
        if (resettingTargetTimer > 0) if (FMLCommonHandler.instance().getSide().isClient()) setRotationYawHead(prevRotationYawHead);
        if (getAttackTarget() != null)
        {
            if (FMLCommonHandler.instance().getSide().isClient()) setRotationYawHead(targetAngle);

            if (targetDistance <= 4.5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 2 && getAnimID() == 0 && active)
            {
                AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
            }

            if (targetDistance <= 11)
            {
                sendPacket(new MessageIncreaseTimer(getEntityId()));
                lastTimeDecrease = 0;
            }
            else if (lastTimeDecrease <= 30 && getAnimID() == 0)
            {
                sendPacket(new MessageDecreaseTimer(getEntityId()));
                lastTimeDecrease++;
            }
        }
        else if (lastTimeDecrease <= 30 && getAnimID() == 0 && resettingTargetTimer == 0)
        {
            sendPacket(new MessageDecreaseTimer(getEntityId()));
            lastTimeDecrease++;
        }

        if (getAnimID() == MMAnimation.DIE.animID() && getAnimTick() <= 12) deathFlail.increaseTimer();
        if (getAnimID() == MMAnimation.DIE.animID() && getAnimTick() > 12) deathFlail.decreaseTimer();
        if (getAnimID() == MMAnimation.DIE.animID())
        {
            stopDance.increaseTimer();
            activate.increaseTimer();
        }

        if (resettingTargetTimer > 0) resettingTargetTimer--;

        if (getAttackTarget() != null && frame % 20 == 0 && getAnimID() == 0)
        {
            setAttackTarget(null);
            resettingTargetTimer = 2;
        }
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        return active && super.attackEntityFrom(p_70097_1_, p_70097_2_);
    }

    public void applyEntityCollision(Entity p_70108_1_)
    {
        if (p_70108_1_.riddenByEntity != this && p_70108_1_.ridingEntity != this)
        {
            double d0 = p_70108_1_.posX - posX;
            double d1 = p_70108_1_.posZ - posZ;
            double d2 = MathHelper.abs_max(d0, d1);

            if (d2 >= 0.009999999776482582D)
            {
                d2 = (double) MathHelper.sqrt_double(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= (double) (1.0F - entityCollisionReduction);
                d1 *= (double) (1.0F - entityCollisionReduction);
                addVelocity(d0, 0.0D, d1);
                p_70108_1_.addVelocity(d0, 0.0D, d1);
            }
        }
    }

    public boolean canBeCollidedWith()
    {
        return active;
    }

    public void sendPacket2(AbstractMessage packet)
    {
        MowziesMobs.networkWrapper.sendToServer(packet);
    }

    public boolean getCanSpawnHere()
    {
        if (worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox))
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(boundingBox.minY);
            int k = MathHelper.floor_double(posZ);

            if (j < 63)
            {
                return false;
            }

            Block block = worldObj.getBlock(i, j - 1, k);

            if (block == Blocks.grass || block.isLeaves(worldObj, i, j - 1, k))
            {
                return true;
            }
        }
        return false;
    }

    public void onKillEntity(EntityLivingBase entity)
    {
        addPotionEffect(new PotionEffect(Potion.regeneration.id, 300, 1, true));
    }

    protected Item getDropItem()
    {
        int i = rand.nextInt(2);
        System.out.println(i);
        if (i == 1) return MMItems.itemFoliaathSeed;
        else return null;
    }

    protected boolean canDespawn()
    {
        return getCanDespawn() == 1;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(30, (byte) 1);
    }

    public byte getCanDespawn()
    {
        return dataWatcher.getWatchableObjectByte(30);
    }

    public void setCanDespawn(byte canDespawn)
    {
        dataWatcher.updateObject(30, canDespawn);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("canDespawn", getCanDespawn());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setCanDespawn(compound.getByte("canDespawn"));
    }
}