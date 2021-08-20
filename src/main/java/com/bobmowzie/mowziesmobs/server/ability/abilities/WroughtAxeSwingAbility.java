package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.entity.LivingEntity;

import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_HOR;

public class WroughtAxeSwingAbility extends Ability {
    private EntityAxeAttack axeAttack;

    public WroughtAxeSwingAbility(AbilityType<WroughtAxeSwingAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, SWING_DURATION_HOR / 2 - 2),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SWING_DURATION_HOR / 2 + 2)
        });
    }

    @Override
    public void start() {
        super.start();
        if (!getUser().world.isRemote()) {
            EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK, getUser().world, getUser(), false);
            axeAttack.setPositionAndRotation(getUser().getPosX(), getUser().getPosY(), getUser().getPosZ(), getUser().rotationYaw, getUser().rotationPitch);
            getUser().world.addEntity(axeAttack);
            this.axeAttack = axeAttack;
        }
    }

    @Override
    public void end() {
        super.end();
        if (axeAttack != null) {
            this.axeAttack.remove();
        }
    }

    @Override
    public boolean preventsAttacking() {
        return false;
    }
}
