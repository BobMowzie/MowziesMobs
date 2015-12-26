package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.EntityTribeLeader;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeVillager;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

/**
 * Created by jnad325 on 12/7/15.
 */
public class AnimSpawnBarakoa extends MMAnimBase {
    public AnimSpawnBarakoa(IAnimatedEntity entity, int id, int duration) {
        super(entity, id, duration);
        setMutexBits(8);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        ((EntityTribeLeader)animatingEntity).barakoaSpawnCount++;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        if (((EntityTribeLeader)animatingEntity).barakoaSpawnCount < 3 && ((EntityTribeLeader)animatingEntity).targetDistance > 5) AnimationAPI.sendAnimPacket(animatingEntity, 6);
        else ((EntityTribeLeader)animatingEntity).barakoaSpawnCount = 0;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (animatingEntity.getAnimTick() == 1) animatingEntity.playSound("mowziesmobs:barakoaInhale", 1.2f, 0.5f);
        if (animatingEntity.getAnimTick() == 13) {
            animatingEntity.playSound("mowziesmobs:barakoBelly", 1.5f, 1);
            animatingEntity.playSound("mowziesmobs:barakoaBlowdart", 1.5f, 0.5f);
            double angle = animatingEntity.rotationYawHead;
            if (angle - animatingEntity.rotationYaw > 70) angle = 70 + animatingEntity.rotationYaw;
            else if (angle - animatingEntity.rotationYaw < -70) angle = -70 + animatingEntity.rotationYaw;
            EntityTribeVillager tribesman = new EntityTribeVillager(animatingEntity.worldObj);
            tribesman.yOffset = tribesman.height / 2;
            tribesman.setPositionAndRotation(animatingEntity.posX + 2 * Math.sin(-angle * Math.PI / 180), animatingEntity.posY + 1.5, animatingEntity.posZ + 2 * Math.cos(-angle * Math.PI / 180), animatingEntity.rotationYawHead, 0);
            tribesman.setActive(0);
            tribesman.active = false;
            animatingEntity.worldObj.spawnEntityInWorld(tribesman);
            tribesman.motionX = 0.7 * Math.sin(-angle * Math.PI / 180);
            tribesman.motionY = 0.5;
            tribesman.motionZ = 0.7 * Math.cos(-angle * Math.PI / 180);
        }
    }
}
