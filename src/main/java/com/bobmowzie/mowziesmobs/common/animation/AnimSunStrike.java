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
        if (!animatingEntity.worldObj.isRemote && animatingEntity.getAnimTick() == 13) {
            int x = MathHelper.floor_double(entityTarget.posX);
            int y = MathHelper.floor_double(entityTarget.posY) - 1;
            int z = MathHelper.floor_double(entityTarget.posZ);
            EntitySunstrike sunstrike = new EntitySunstrike(animatingEntity.worldObj, animatingEntity, x, y, z);
            sunstrike.onSummon();
            animatingEntity.worldObj.spawnEntityInWorld(sunstrike);
        }
    }
}
