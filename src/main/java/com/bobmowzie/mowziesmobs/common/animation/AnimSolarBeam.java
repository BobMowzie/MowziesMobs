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
        float radius1 = 1.7f;
        if (animatingEntity.getAnimTick() == 4 && !animatingEntity.worldObj.isRemote)
        {
            solarBeam = new EntitySolarBeam(animatingEntity.worldObj, animatingEntity, animatingEntity.posX + radius1 * Math.sin(-animatingEntity.rotationYaw * Math.PI / 180), animatingEntity.posY + 1.4, animatingEntity.posZ + radius1 * Math.cos(-animatingEntity.rotationYaw * Math.PI / 180), (float) ((animatingEntity.rotationYawHead + 90) * Math.PI/180), (float) (-animatingEntity.rotationPitch * Math.PI/180), 55);
            animatingEntity.worldObj.spawnEntityInWorld(solarBeam);
        }
        if (animatingEntity.getAnimTick() >= 4) {
            float radius2 = 1.2f;
            double x = animatingEntity.posX + radius1 * Math.sin(-animatingEntity.rotationYaw * Math.PI / 180) + radius2 * Math.sin(-animatingEntity.rotationYawHead * Math.PI/180) * Math.cos(-animatingEntity.rotationPitch * Math.PI/180);
            double y = animatingEntity.posY + 1.4 + radius2 * Math.cos(-animatingEntity.rotationYawHead * Math.PI/180) * Math.cos(-animatingEntity.rotationPitch * Math.PI/180);
            double z = animatingEntity.posZ + radius1 * Math.cos(-animatingEntity.rotationYaw * Math.PI / 180) + radius2 * Math.sin(-animatingEntity.rotationPitch * Math.PI/180);
            solarBeam.setPosition(x, y, z);

            float yaw = animatingEntity.rotationYawHead + 90;
            float pitch = -animatingEntity.rotationPitch;
            solarBeam.setYaw((float) (yaw * Math.PI / 180));
            solarBeam.setPitch((float) (pitch * Math.PI/180));
        }
        if (animatingEntity.getAnimTick() >= 22) {
            if (entityTarget != null) animatingEntity.getLookHelper().setLookPosition(entityTarget.posX, entityTarget.posY + entityTarget.height / 2, entityTarget.posZ, 1, 90);
        }
    }
}
