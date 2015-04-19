package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.ai.animation.AnimFWNAttack;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

public class EntityWroughtnaut extends MMEntityBase {
    public double walkFrame;
    public ControlledAnimation walkAnim = new ControlledAnimation(20);

    public EntityWroughtnaut(World world) {
        super(world);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimFWNAttack(this, 44, "", 3F, 6F, 90F));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 20;
        this.setSize(3F, 4F);
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
        else if (worldObj.isRemote) getNavigator().clearPathEntity();

        double walkFrameIncrement = 1.5 * Math.pow(Math.sin(0.15*(frame - 9)), 2) + 0.25;
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

//        if (getAnimID() == 0) AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}