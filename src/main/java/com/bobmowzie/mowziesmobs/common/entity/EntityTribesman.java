package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.animation.AnimBasicAttack;
import com.bobmowzie.mowziesmobs.common.animation.AnimDie;
import com.bobmowzie.mowziesmobs.common.animation.AnimTakeDamage;
import net.ilexiconn.llibrary.client.model.modelbase.ControlledAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

/**
 * Created by jnad325 on 7/9/15.
 */

public class EntityTribesman extends MMEntityBase {
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
        tasks.addTask(2, new AnimBasicAttack(this, 1, 22, "", 1, 3, 1, 9));
        tasks.addTask(3, new AnimTakeDamage(this, 10));
        tasks.addTask(1, new AnimDie(this, deathLength));
        setMask(0);
    }

    @Override
    public int getAttack() {
        return 3;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
    }

    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
        return super.attackEntityAsMob(p_70652_1_);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        attacking = true;
        return super.attackEntityFrom(source, damage);
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
                if (getAnimID() == 0) getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.5);
            }
            if (attacking && getAnimID() == 0 && targetDistance <= 3) {
                attacking = false;
                timeSinceAttack = 0;
                AnimationAPI.sendAnimPacket(this, 1);
            }
        }
        else attacking = false;
    }

    protected void updateCircling() {
        circleEntity(getAttackTarget(), 7, 0.3f, true, frame, 0);
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
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(29, 0);
    }

    public int getMask()
    {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setMask(Integer type)
    {
        dataWatcher.updateObject(29, type);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("mask", getMask());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setMask(compound.getInteger("mask"));
    }
}
