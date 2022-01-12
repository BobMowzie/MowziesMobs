package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.HandSide;

import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_HOR;
import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_VER;

public class WroughtAxeSlamAbility extends Ability {
    private EntityAxeAttack axeAttack;

    public WroughtAxeSlamAbility(AbilityType<WroughtAxeSlamAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, SWING_DURATION_VER / 2 - 2),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SWING_DURATION_VER / 2 + 2)
        });
    }

    @Override
    public void start() {
        super.start();
        if (!getUser().world.isClientSide()) {
            EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK, getUser().world, getUser(), true);
            axeAttack.setPositionAndRotation(getUser().getPosX(), getUser().getPosY(), getUser().getPosZ(), getUser().getYRot(), getUser().getXRot());
            getUser().world.addEntity(axeAttack);
            this.axeAttack = axeAttack;
        }
        else {
            playAnimation("axe_swing_vertical", false);
            heldItemMainHandVisualOverride = getUser().getHeldItemMainhand();
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getTicksInUse() == SWING_DURATION_VER && getUser() instanceof Player) {
            Player player = (Player) getUser();
            player.resetCooldown();
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
