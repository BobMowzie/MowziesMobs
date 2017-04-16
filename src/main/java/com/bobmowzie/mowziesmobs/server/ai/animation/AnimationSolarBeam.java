package com.bobmowzie.mowziesmobs.server.ai.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;

public class AnimationSolarBeam<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    protected EntityLivingBase entityTarget;

    private EntitySolarBeam solarBeam;

    public AnimationSolarBeam(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entityTarget = animatingEntity.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        float radius1 = 1.7f;
        if (animatingEntity.getAnimationTick() == 4 && !animatingEntity.world.isRemote) {
            solarBeam = new EntitySolarBeam(animatingEntity.world, animatingEntity, animatingEntity.posX + radius1 * Math.sin(-animatingEntity.rotationYaw * Math.PI / 180), animatingEntity.posY + 1.4, animatingEntity.posZ + radius1 * Math.cos(-animatingEntity.rotationYaw * Math.PI / 180), (float) ((animatingEntity.rotationYawHead + 90) * Math.PI / 180), (float) (-animatingEntity.rotationPitch * Math.PI / 180), 55);
            animatingEntity.world.spawnEntity(solarBeam);
        }
        if (animatingEntity.getAnimationTick() >= 4) {
            float radius2 = 1.2f;
            double x = animatingEntity.posX + radius1 * Math.sin(-animatingEntity.rotationYaw * Math.PI / 180) + radius2 * Math.sin(-animatingEntity.rotationYawHead * Math.PI / 180) * Math.cos(-animatingEntity.rotationPitch * Math.PI / 180);
            double y = animatingEntity.posY + 1.4 + radius2 * Math.sin(-animatingEntity.rotationPitch * Math.PI / 180);
            double z = animatingEntity.posZ + radius1 * Math.cos(-animatingEntity.rotationYaw * Math.PI / 180) + radius2 * Math.cos(-animatingEntity.rotationYawHead * Math.PI / 180) * Math.cos(-animatingEntity.rotationPitch * Math.PI / 180);
            solarBeam.setPosition(x, y, z);

            float yaw = animatingEntity.rotationYawHead + 90;
            float pitch = -animatingEntity.rotationPitch;
            solarBeam.setYaw((float) (yaw * Math.PI / 180));
            solarBeam.setPitch((float) (pitch * Math.PI / 180));
        }
        if (animatingEntity.getAnimationTick() >= 22) {
            if (entityTarget != null) {
                animatingEntity.getLookHelper().setLookPosition(entityTarget.posX, entityTarget.posY + entityTarget.height / 2, entityTarget.posZ, 2, 90);
            }
        }
    }
}
