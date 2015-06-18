package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class AnimBasicAttack extends MMAnimBase
{
    protected MMEntityBase entity;
    protected int duration;
    protected EntityLivingBase entityTarget;
    protected String attackSound;
    protected float knockback = 1;
    protected float range;

    public AnimBasicAttack(MMEntityBase entity, int duration, String sound, float knockback, float range)
    {
        super(entity, duration);
        setMutexBits(8);
        this.entity = entity;
        this.duration = duration;
        entityTarget = null;
        attackSound = sound;
        this.knockback = knockback;
        this.range = range;
    }

    public int getAnimID()
    {
        return MMAnimation.ATTACK.animID();
    }

    public void startExecuting()
    {
        super.startExecuting();
        entityTarget = entity.getAttackTarget();
    }

    public void updateTask()
    {
        super.updateTask();
        if (entity.getAnimTick() < ((duration / 2) - 4) && entityTarget != null)
            entity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
        if (entity.getAnimTick() == ((duration / 2) - 4))
        {
            float damage = (float) entity.getAttack();
            if (entityTarget != null && entity.targetDistance <= range)
            {
                entityTarget.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
                entityTarget.motionX *= knockback;
                entityTarget.motionZ *= knockback;
            }
            entity.playSound(attackSound, 1, 1);
        }
    }
}
