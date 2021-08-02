package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.entity.LivingEntity;

// Ability instance class tracking a specific entity's use of an ability type
public class AbilityInstance {
    private final Ability<AbilityInstance> abilityType;
    private final LivingEntity user;
    private int ticksInUse;
    private int ticksInSection;
    private boolean isUsing;

    public AbilityInstance(Ability<AbilityInstance> abilityType, LivingEntity user) {
        this.abilityType = abilityType;
        this.user = user;
    }

    public void onStart() {
        ticksInUse = 0;
        ticksInSection = 0;
        isUsing = true;
        abilityType.onStart(this);
        System.out.println("Start ability");
    }

    public void tick() {
        ticksInUse++;
        ticksInSection++;
        abilityType.tick(this);
        System.out.println("tick ability " + ticksInUse);
    }

    public void onEnd() {
        ticksInUse = 0;
        ticksInSection = 0;
        isUsing = false;
        abilityType.onEnd(this);
    }

    public void onInterrupted() {
        abilityType.onInterrupted(this);
    }

    public void onCompleted() {
        abilityType.onCompleted(this);
    }

    public boolean canUse() {
        return abilityType.canUse(user);
    }

    protected boolean canContinueUsing() {
        return abilityType.canContinueUsing(this);
    }

    public boolean isUsing() {
        return isUsing;
    }

    public LivingEntity getUser() {
        return user;
    }

    public int getTicksInUse() {
        return ticksInUse;
    }
}
