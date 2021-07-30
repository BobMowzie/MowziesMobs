package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.entity.LivingEntity;

// Ability type class defining behaviors and attributes of ability
public abstract class Ability<T extends AbilityInstance> {
    private final AbilitySection[] sectionTrack;

    protected Ability(AbilitySection[] sectionTrack) {
        this.sectionTrack = sectionTrack;
    }

    protected void onStart(T abilityInstance) {
    }

    public void tick(T abilityInstance) {
    }

    protected void onEnd(T abilityInstance) {
    }

    public void onInterrupted(T abilityInstance) {

    }

    public void onCompleted(T abilityInstance) {

    }

    public boolean canUse(LivingEntity user) {
        return true;
    }

    protected boolean canContinueUsing(T abilityInstance) {
        return true;
    }

    public abstract T makeInstance(LivingEntity user);
}
