package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import thehippomaster.AnimationAPI.IAnimatedEntity;

/**
 * Created by jnad325 on 11/27/15.
 */
public class AnimSunStrike extends MMAnimBase {
    protected EntityLivingBase entityTarget;

    double prevX;
    double prevZ;

    public AnimSunStrike(IAnimatedEntity entity, int id, int duration) {
        super(entity, id, duration, false);
    }

    public void startExecuting()
    {
        super.startExecuting();
        entityTarget = animatingEntity.getAttackTarget();
    }

    public void updateTask()
    {
        super.updateTask();
        if (entityTarget == null) return;
        animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30, 30);

        if (!animatingEntity.worldObj.isRemote && animatingEntity.getAnimTick() == 12) {
            prevX = entityTarget.posX;
            prevZ = entityTarget.posZ;
        }
        if (!animatingEntity.worldObj.isRemote && animatingEntity.getAnimTick() == 13) {
            double x = entityTarget.posX;
            int y = MathHelper.floor_double(entityTarget.posY - 1);
            double z = entityTarget.posZ;
            double vx = x - prevX;
            double vz = z - prevZ;
            int t = EntitySunstrike.STRIKE_EXPLOSION;
            int newX = MathHelper.floor_double(x + vx * t);
            int newZ = MathHelper.floor_double(z + vz * t);
            EntitySunstrike sunstrike = new EntitySunstrike(animatingEntity.worldObj, animatingEntity, newX, y, newZ);
            sunstrike.onSummon();
            animatingEntity.worldObj.spawnEntityInWorld(sunstrike);
        }
    }
}
