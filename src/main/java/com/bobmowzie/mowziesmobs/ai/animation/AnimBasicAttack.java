package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimBasicAttack extends AIAnimation
{
    private MMEntityBase entity;
    private int duration;
    private EntityLivingBase entityTarget;
    private String attackSound;
    private float knockback = 1;
    private float range;

    public AnimBasicAttack(MMEntityBase entity, int duration, String sound, float knockback, float range)
    {
        super(entity);
        setMutexBits(8);
        this.entity = entity;
        this.duration = duration;
        entityTarget = null;
        attackSound = sound;
        this.knockback = knockback;
        this.range = range;
    }

    @Override
    public int getAnimID()
    {
        return MMAnimation.ATTACK.animID();
    }

    @Override
    public boolean isAutomatic()
    {
        return true;
    }

    @Override
    public int getDuration()
    {
        return this.duration;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        this.entityTarget = this.entity.getAttackTarget();
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
        if (entity.getAnimTick() < ((this.duration / 2) - 4) && entityTarget != null)
            entity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
        if (this.entity.getAnimTick() == ((this.duration / 2) - 4))
        {
            float damage = (float) this.entity.getAttack();
            if (entityTarget != null && entity.targetDistance <= range)
            {
                this.entityTarget.attackEntityFrom(DamageSource.causeMobDamage(this.entity), damage);
                entityTarget.motionX *= knockback;
                entityTarget.motionZ *= knockback;
            }
            entity.playSound(attackSound, 1, 1);
        }
    }
}
