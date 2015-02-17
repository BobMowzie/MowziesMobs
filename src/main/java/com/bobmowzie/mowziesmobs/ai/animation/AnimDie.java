package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.util.DamageSource;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimDie extends AIAnimation
{
    private int duration;
    private DamageSource dmgSource;
    private MMEntityBase dyingEntity;

    public AnimDie(MMEntityBase entity, int duration)
    {
        super(entity);
        setMutexBits(8);
        this.duration = duration;
        dyingEntity = entity;
    }

    @Override
    public int getAnimID()
    {
        return MMAnimation.DIE.animID();
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
        dmgSource = dyingEntity.dieSource;
    }

    @Override
    public void resetTask()
    {
        super.resetTask();
        dyingEntity.onDeath(dmgSource);
    }

    @Override
    public void updateTask()
    {
        super.updateTask();
    }
}
