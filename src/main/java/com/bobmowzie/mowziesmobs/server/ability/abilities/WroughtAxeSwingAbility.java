package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_HOR;

public class WroughtAxeSwingAbility extends PlayerAbility {
    private EntityAxeAttack axeAttack;

    public WroughtAxeSwingAbility(AbilityType<Player, WroughtAxeSwingAbility> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, SWING_DURATION_HOR / 2 - 2),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SWING_DURATION_HOR / 2 + 2 + 7)
        });
    }

    @Override
    public void start() {
        super.start();
        if (!getUser().level.isClientSide()) {
            EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK.get(), getUser().level, getUser(), false);
            axeAttack.absMoveTo(getUser().getX(), getUser().getY(), getUser().getZ(), getUser().getYRot(), getUser().getXRot());
            getUser().level.addFreshEntity(axeAttack);
            this.axeAttack = axeAttack;
        }
        else {
            boolean handSide = getUser().getMainArm() == HumanoidArm.RIGHT;
            playAnimation("axe_swing_start_" + (handSide ? "right" : "left"), GeckoPlayer.Perspective.THIRD_PERSON, false);
            playAnimation("axe_swing_start", GeckoPlayer.Perspective.FIRST_PERSON, false);
            heldItemMainHandVisualOverride = getUser().getMainHandItem();
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getTicksInUse() == SWING_DURATION_HOR && getUser() instanceof Player) {
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
