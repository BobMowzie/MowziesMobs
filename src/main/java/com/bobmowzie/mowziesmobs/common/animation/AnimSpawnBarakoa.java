package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.EntityTribeVillager;
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
    public void updateTask() {
        super.updateTask();
        if (animatingEntity.getAnimTick() == 15) {
            double angle = animatingEntity.rotationYawHead;
            if (angle - animatingEntity.rotationYaw > 70) angle = 70 + animatingEntity.rotationYaw;
            else if (angle - animatingEntity.rotationYaw < -70) angle = -70 + animatingEntity.rotationYaw;
            EntityTribeVillager tribesman = new EntityTribeVillager(animatingEntity.worldObj);
            tribesman.setPositionAndRotation(animatingEntity.posX + 1.5 * Math.sin(-angle * Math.PI / 180), animatingEntity.posY + 1.5, animatingEntity.posZ + 1.5 * Math.cos(-angle * Math.PI / 180), animatingEntity.rotationYawHead, 0);
            tribesman.setActive(0);
            tribesman.active = false;
            System.out.println(angle + ", " + animatingEntity.rotationYawHead);
            animatingEntity.worldObj.spawnEntityInWorld(tribesman);
            tribesman.setVelocity(0.7 * Math.sin(-angle * Math.PI / 180), 0.5, 0.7 * Math.cos(-angle * Math.PI / 180));
        }
    }
}
