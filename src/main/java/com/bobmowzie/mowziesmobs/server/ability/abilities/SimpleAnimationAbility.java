package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;

public class SimpleAnimationAbility extends Ability {
    private String animationName;
    private boolean separateLeftAndRight;
    private boolean lockHeldItemMainHand;

    public SimpleAnimationAbility(AbilityType<SimpleAnimationAbility> abilityType, LivingEntity user, String animationName, int duration) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        this.animationName = animationName;
    }

    public SimpleAnimationAbility(AbilityType<SimpleAnimationAbility> abilityType, LivingEntity user, String animationName, int duration, boolean separateLeftAndRight, boolean lockHeldItemMainHand) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        this.animationName = animationName;
        this.separateLeftAndRight = separateLeftAndRight;
        this.lockHeldItemMainHand = lockHeldItemMainHand;
    }

    @Override
    public void start() {
        super.start();
        if (separateLeftAndRight) {
            boolean handSide = getUser().getMainArm() == HumanoidArm.RIGHT;
            playAnimation(animationName + "_" + (handSide ? "right" : "left"), GeckoPlayer.Perspective.THIRD_PERSON, false);
            playAnimation(animationName, GeckoPlayer.Perspective.FIRST_PERSON, false);
        }
        else {
            playAnimation(animationName, false);
        }
        if (lockHeldItemMainHand)
            heldItemMainHandVisualOverride = getUser().getMainHandItem();
    }
}
