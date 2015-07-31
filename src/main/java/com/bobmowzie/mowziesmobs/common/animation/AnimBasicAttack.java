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
    protected float damageMultiplier;
    protected int damageFrame;

    public AnimBasicAttack(MMEntityBase entity, int id, int duration, String sound, float knockback, float range, float damageMultiplier, int damageFrame)
    {
        super(entity, id, duration);
        setMutexBits(8);
        this.entity = entity;
        this.duration = duration;
        entityTarget = null;
        attackSound = sound;
        this.knockback = knockback;
        this.range = range;
        this.damageMultiplier = damageMultiplier;
        this.damageFrame = damageFrame;
    }

    public void startExecuting()
    {
        super.startExecuting();
        entityTarget = entity.getAttackTarget();
    }

    public void updateTask()
    {
        super.updateTask();
        if (entity.getAnimTick() < damageFrame && entityTarget != null)
            entity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
        if (entity.getAnimTick() == damageFrame)
        {
            float damage = (float) entity.getAttack();
            if (entityTarget != null && entity.targetDistance <= range)
            {
                entityTarget.attackEntityFrom(DamageSource.causeMobDamage(entity), damage * damageMultiplier);
                entityTarget.motionX *= knockback;
                entityTarget.motionZ *= knockback;
            }
            entity.playSound(attackSound, 1, 1);
        }
    }
}
