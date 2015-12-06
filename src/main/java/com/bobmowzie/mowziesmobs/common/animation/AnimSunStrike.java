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

    int newX;
    int newZ;
    int y;

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
        if (animatingEntity.getAnimTick() < 13) animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30, 30);

        if (animatingEntity.getAnimTick() == 9) {
            prevX = entityTarget.posX;
            prevZ = entityTarget.posZ;
        }
        if (animatingEntity.getAnimTick() == 10) {
            double x = entityTarget.posX;
            y = MathHelper.floor_double(entityTarget.posY - 1);
            double z = entityTarget.posZ;
            double vx = x - prevX;
            double vz = z - prevZ;
            int t = EntitySunstrike.STRIKE_EXPLOSION + 3;
            newX = MathHelper.floor_double(x + vx * t);
            newZ = MathHelper.floor_double(z + vz * t);
            for(int i = 0; i < 5; i++) {
                if (!animatingEntity.worldObj.canBlockSeeTheSky(newX, y, newZ)) y++;
                else break;
            }
        }
        if (!animatingEntity.worldObj.isRemote && animatingEntity.getAnimTick() == 13) {
            EntitySunstrike sunstrike = new EntitySunstrike(animatingEntity.worldObj, animatingEntity, newX, y, newZ);
            sunstrike.onSummon();
            animatingEntity.worldObj.spawnEntityInWorld(sunstrike);
        }
        if (animatingEntity.getAnimTick() >= 10) animatingEntity.getLookHelper().setLookPosition(newX, y, newZ, 20, 20);
    }
}
