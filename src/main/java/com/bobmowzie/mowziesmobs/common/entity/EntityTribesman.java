package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.animation.AnimBasicAttack;
import com.bobmowzie.mowziesmobs.common.animation.AnimDie;
import com.bobmowzie.mowziesmobs.common.animation.AnimProjectileAttack;
import com.bobmowzie.mowziesmobs.common.animation.AnimTakeDamage;
import net.ilexiconn.llibrary.client.model.modelbase.ControlledAnimation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

/**
 * Created by jnad325 on 7/9/15.
 */

public class EntityTribesman extends MMEntityBase implements IRangedAttackMob {
    protected boolean attacking = false;
    protected int timeSinceAttack = 0;
    public ControlledAnimation doWalk = new ControlledAnimation(3);
    public ControlledAnimation dancing = new ControlledAnimation(10);
    private int danceTimer = 0;

    public EntityTribesman(World world) {
        super(world);
        deathLength = 70;
        tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5D, false));
        tasks.addTask(5, new EntityAIWander(this, 0.4));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        tasks.addTask(2, new AnimBasicAttack(this, 1, 19, "", 1, 3, 1, 9));
        tasks.addTask(2, new AnimProjectileAttack(this, 2, 20, 9, ""));
        tasks.addTask(3, new AnimTakeDamage(this, 10));
        tasks.addTask(1, new AnimDie(this, deathLength));
        setMask(0);
        setSize(0.6f, 1.7f);
        stepHeight = 1;
    }

    @Override
    public int getAttack() {
        return 4;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20);
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
        return super.attackEntityAsMob(p_70652_1_);
    }

    protected void updateAttackAI() {
        if (timeSinceAttack < 80) timeSinceAttack ++;
        if (getAttackTarget() != null)
        {
            if (targetDistance > 7) getNavigator().tryMoveToXYZ(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 0.6);
            else
            {
                if (attacking == false) updateCircling();
            }
            if (rand.nextInt(80) == 0 && timeSinceAttack == 80)
            {
                attacking = true;
                if (getAnimID() == 0 && getWeapon() == 0) getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.5);
            }
            if (attacking && getAnimID() == 0) {
                if(targetDistance <= 3 && getWeapon() == 0)
                {
                    attacking = false;
                    timeSinceAttack = 0;
                    AnimationAPI.sendAnimPacket(this, 1);
                }
                if (getWeapon() == 1)
                {
                    AnimationAPI.sendAnimPacket(this, 2);
                }
            }
        }
        else attacking = false;
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
        if (!(this instanceof EntityTribeElite)) {
            int weapon = 0;
            if (rand.nextInt(3) == 0) weapon = 1;
            setWeapon(weapon);
        }
        return super.onSpawnWithEgg(p_110161_1_);
    }

    protected void updateCircling() {
        if (!attacking && targetDistance < 5) circleEntity(getAttackTarget(), 7, 0.3f, true, frame, 0, 1.75f);
        else circleEntity(getAttackTarget(), 7, 0.3f, true, frame, 0, 1);
        attacking = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        updateAttackAI();
        if (getAnimID() != 0) {
            getNavigator().clearPathEntity();
        }

        if (getAnimID() == 0) doWalk.increaseTimer();
        else doWalk.decreaseTimer();

        if (danceTimer != 0 && danceTimer != 50) {
            danceTimer++;
            dancing.increaseTimer();
        }
        else {
            danceTimer = 0;
            dancing.decreaseTimer();
        }
        if (danceTimer == 0 && rand.nextInt(800) == 0) danceTimer++;
        if (getAnimID() != 0) danceTimer = 0;

//        if (getAnimID() == 0) AnimationAPI.sendAnimPacket(this, 2);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(29, 0);
        dataWatcher.addObject(30, 0);
    }

    public int getMask()
    {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setMask(Integer type)
    {
        dataWatcher.updateObject(29, type);
    }

    public int getWeapon()
    {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void setWeapon(Integer type)
    {
        dataWatcher.updateObject(30, type);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("mask", getMask());
        compound.setInteger("weapon", getWeapon());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setMask(compound.getInteger("mask"));
        setWeapon(compound.getInteger("weapon"));
    }

    public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
    {
        EntityArrow entitydart = new EntityDart(this.worldObj, this, p_82196_1_, 1.6F, 1);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entitydart.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

        if (i > 0)
        {
            entitydart.setDamage(entitydart.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entitydart.setKnockbackStrength(j);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entitydart);
        attacking = false;
    }
}
