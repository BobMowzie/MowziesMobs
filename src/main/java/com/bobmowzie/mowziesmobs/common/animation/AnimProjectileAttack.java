package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;

/**
 * Created by jnad325 on 8/4/15.
 */
public class AnimProjectileAttack extends MMAnimBase
{
    private EntityLivingBase entityTarget;
    private int duration;
    private int attackFrame;
    private String attackSound;

    public AnimProjectileAttack(MMEntityBase entity, int id, int duration, int attackFrame, String attackSound)
    {
        super(entity, id, duration);
        this.entityTarget = null;
        this.duration = duration;
        this.attackFrame = attackFrame;
        this.attackSound = attackSound;
    }

    public void startExecuting()
    {
        super.startExecuting();
        this.entityTarget = animatingEntity.getAttackTarget();
    }

    public void updateTask()
    {
        super.updateTask();
        if (this.entityTarget != null)
        {
            animatingEntity.faceEntity(entityTarget, 100, 100);
            animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
            if (animatingEntity.getAnimTick() == attackFrame)
            {
                ((IRangedAttackMob) animatingEntity).attackEntityWithRangedAttack(entityTarget, 0);
                animatingEntity.playSound(attackSound, 1, 1);
            }
        }
    }

    public void resetTask()
    {
        super.resetTask();
    }
}

