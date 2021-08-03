package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.entity.LivingEntity;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection.*;

// Ability instance class tracking a specific entity's use of an ability type
public class AbilityInstance<M extends Ability> {
    private final M abilityType;
    private final LivingEntity user;
    private int ticksInUse;
    private int ticksInSection;
    private int currentSectionIndex;
    private boolean isUsing;
    private int cooldownTimer;

    public AbilityInstance(M abilityType, LivingEntity user) {
        this.abilityType = abilityType;
        this.user = user;
    }

    public void start() {
        ticksInUse = 0;
        ticksInSection = 0;
        currentSectionIndex = 0;
        isUsing = true;
        abilityType.start(this);
//        System.out.println("Start ability " + abilityType.getClass().getSimpleName());
    }

    public void tick() {
        if (isUsing()) {
            abilityType.tick(this);

            if (!canContinueUsing()) end();

            ticksInUse++;
            ticksInSection++;
            AbilitySection section = getCurrentSection();
            if (section instanceof AbilitySectionInstant) {
                nextSection();
            } else if (section instanceof AbilitySectionDuration) {
                AbilitySectionDuration sectionDuration = (AbilitySectionDuration) section;
                if (ticksInSection > sectionDuration.duration) nextSection();
            }
        }
        else {
            if (getCooldownTimer() > 0) cooldownTimer--;
        }
    }

    public void end() {
        abilityType.end(this);
        ticksInUse = 0;
        ticksInSection = 0;
        isUsing = false;
        cooldownTimer = abilityType.getCooldown();
        currentSectionIndex = 0;
//        System.out.println("End ability " + abilityType.getClass().getSimpleName());
    }

    public void interrupt() {
        abilityType.interrupt(this);
        end();
    }

    public void complete() {
        abilityType.complete(this);
        end();
    }

    /**
     * Server-only check to see if the user can use this ability. Checked before packet is sent.
     * @return Whether or not the ability can be used
     */
    public boolean canUse() {
        return !isUsing() && cooldownTimer == 0 && abilityType.canUse(user);
    }

    /**
     * Both sides check and behavior when user tries to use this ability. Ability only starts if this returns true.
     * Called after packet is received.
     * @return Whether or not the ability try succeeded
     */
    public boolean tryAbility() {
        return abilityType.tryAbility(this);
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

    public int getCooldownTimer() {
        return cooldownTimer;
    }

    public void nextSection() {
        jumpToSection(currentSectionIndex + 1);
        if (currentSectionIndex >= abilityType.getSectionTrack().length) {
            complete();
        }
    }

    public void jumpToSection(int sectionIndex) {
        currentSectionIndex = sectionIndex;
        ticksInSection = 0;
    }

    public AbilitySection getCurrentSection() {
        if (currentSectionIndex >= abilityType.getSectionTrack().length) return null;
        return abilityType.getSectionTrack()[currentSectionIndex];
    }
}
