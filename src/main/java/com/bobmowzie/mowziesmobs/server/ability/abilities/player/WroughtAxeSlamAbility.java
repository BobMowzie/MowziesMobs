package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.world.entity.player.Player;

import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_VER;

public class WroughtAxeSlamAbility extends PlayerAbility {
    private EntityAxeAttack axeAttack;

    public WroughtAxeSlamAbility(AbilityType<Player, WroughtAxeSlamAbility> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, SWING_DURATION_VER / 2 - 2),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SWING_DURATION_VER / 2 + 2)
        });
    }

    @Override
    public void start() {
        super.start();
        if (!getUser().level.isClientSide()) {
            EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK.get(), getUser().level, getUser(), true);
            axeAttack.absMoveTo(getUser().getX(), getUser().getY(), getUser().getZ(), getUser().getYRot(), getUser().getXRot());
            getUser().level.addFreshEntity(axeAttack);
            this.axeAttack = axeAttack;
        }
        else {
            playAnimation("axe_swing_vertical", false);
            heldItemMainHandVisualOverride = getUser().getMainHandItem();
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getTicksInUse() == SWING_DURATION_VER && getUser() instanceof Player) {
            Player player = (Player) getUser();
            player.resetAttackStrengthTicker();
        }
    }

    @Override
    public void end() {
        super.end();
        if (axeAttack != null) {
            this.axeAttack.discard() ;
        }
    }

    @Override
    public boolean preventsAttacking() {
        return false;
    }
}
