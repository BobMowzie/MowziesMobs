package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.entity.LivingEntity;

// Ability type class defining behaviors and attributes of ability
public abstract class Ability<T extends AbilityInstance> {
    private final AbilitySection[] sectionTrack;
    private final int cooldown;

    protected Ability(AbilitySection[] sectionTrack) {
        this(sectionTrack, 0);
    }

    protected Ability(AbilitySection[] sectionTrack, int cooldown) {
        this.sectionTrack = sectionTrack;
        this.cooldown = cooldown;
    }

    protected void start(T abilityInstance) {
    }

    public void tick(T abilityInstance) {
    }

    protected void end(T abilityInstance) {
    }

    public void interrupt(T abilityInstance) {

    }

    public void complete(T abilityInstance) {

    }

    /**
     * Server-only check to see if the user can use this ability. Checked before packet is sent.
     * @param user User of the ability
     * @return Whether or not the ability can be used
     */
    public boolean canUse(LivingEntity user) {
        return true;
    }

    /**
     * Both sides check and behavior when user tries to use this ability. Ability only starts if this returns true.
     * Called after packet is received.
     * @param abilityInstance This ability's instance
     * @return Whether or not the ability try succeeded
     */
    public boolean tryAbility(T abilityInstance) {
        return true;
    }

    protected boolean canContinueUsing(T abilityInstance) {
        return true;
    }

    public abstract T makeInstance(LivingEntity user);

    public AbilitySection[] getSectionTrack() {
        return sectionTrack;
    }

    public int getCooldown() {
        return cooldown;
    }
}
