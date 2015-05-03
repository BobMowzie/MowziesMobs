package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.ai.animation.AnimFWNAttack;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

public class EntityWroughtnaut extends MMEntityBase {
    public double walkFrame;
    public ControlledAnimation walkAnim = new ControlledAnimation(10);
    public boolean swingDirection = false;

    public EntityWroughtnaut(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimFWNAttack(this, 50, "mowziesmobs:wroughtnautWhoosh", 4F, 5F, 100F));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 20;
        this.setSize(3F, 4F);
    }

    @Override
    public int getAttack() {
        return 50;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return super.attackEntityFrom(p_70097_1_, p_70097_2_);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (getAttackTarget() != null)
        {
            getNavigator().tryMoveToEntityLiving(this.getAttackTarget(), 0.2D);

            if (targetDistance <= 5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 3 && getAnimID() == 0)
            {
                AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
            }
        }
        if (getAnimID() == MMAnimation.ATTACK.animID() && getAnimTick() == 1)
        {
            int i = (int) (Math.random() + 0.5);
            if (i == 0) swingDirection = false;
            if (i == 1) swingDirection = true;
        }

        else if (worldObj.isRemote) getNavigator().clearPathEntity();

        double walkFrameIncrement = 1.5 * Math.pow(Math.sin((Math.PI * 0.05)*(frame - 9)), 2) + 0.25;
        walkFrame += walkFrameIncrement;

        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = (float) Math.sqrt(moveX*moveX + moveZ*moveZ);
        if (speed > 0.02)
        {
            if (getAnimID() == 0) walkAnim.increaseTimer();
        }
        else walkAnim.decreaseTimer();
        if (getAnimID() != 0) walkAnim.decreaseTimer(2);

        if (frame % 20 == 10 && speed > 0.03) playSound("mob.zombie.metal", 0.5F, 0.5F);

//        if (getAnimID() == 0) AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}