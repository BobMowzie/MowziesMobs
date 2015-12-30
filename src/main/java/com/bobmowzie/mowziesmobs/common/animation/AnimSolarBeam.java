package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.EntitySolarBeam;
import net.minecraft.entity.EntityLivingBase;
import thehippomaster.AnimationAPI.IAnimatedEntity;

/**
 * Created by jnad325 on 12/29/15.
 */
public class AnimSolarBeam extends MMAnimBase {
    protected EntityLivingBase entityTarget;

    private EntitySolarBeam solarBeam;

    public AnimSolarBeam(IAnimatedEntity entity, int id, int duration) {
        super(entity, id, duration);
    }

    public void startExecuting()
    {
        super.startExecuting();
        entityTarget = animatingEntity.getAttackTarget();
    }

    public void updateTask() {
        super.updateTask();
        if (animatingEntity.getAnimTick() == 26)
        {
            float radius = -1.7f;
            solarBeam = new EntitySolarBeam(animatingEntity.worldObj, animatingEntity, animatingEntity.posX + radius * Math.sin(animatingEntity.rotationYaw * Math.PI / 180), animatingEntity.posY + 1.4, animatingEntity.posZ + radius * Math.cos(animatingEntity.rotationYaw * Math.PI / 180), (float) ((animatingEntity.rotationYawHead + 90) * Math.PI/180), animatingEntity.rotationPitch);
            if (!animatingEntity.worldObj.isRemote) animatingEntity.worldObj.spawnEntityInWorld(solarBeam);
        }
        if (animatingEntity.getAnimTick() >= 26) {
//            solarBeam.pitch = 3;
        }
    }
}
