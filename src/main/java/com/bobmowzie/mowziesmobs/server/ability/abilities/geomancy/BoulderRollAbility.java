package com.bobmowzie.mowziesmobs.server.ability.abilities.geomancy;

import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BoulderRollAbility extends PlayerAbility {
    private static int START_UP = 15;

    public BoulderRollAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user,  new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 15),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 12)
        });
    }

    @Override
    public void start() {
        super.start();
        playAnimation("boulder_roll_start", false);
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            playAnimation("boulder_roll_loop", true);
        }
    }


    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            getUser().setDeltaMovement(getUser().getViewVector(1f).normalize().multiply(0.3d,0d,0.3d));
        }
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            //playAnimation("boulder_roll_loop", true);
            getUser().setDeltaMovement(getUser().getViewVector(1f).normalize().multiply(1d,0d,1d));
        }
    }

     @Override
    public boolean tryAbility() {
        return super.tryAbility();
    }

    @Override
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        super.onRightClickEmpty(event);
        AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.BOULDER_ROLL_ABILITY);
    }

    @Override
    public void onRightMouseUp(Player player) {
        super.onRightMouseUp(player);
        nextSection();
    }

    @Override
    public boolean canUse() {
        if (getUser() instanceof Player && !((Player)getUser()).getInventory().getSelected().isEmpty()) return false;
        return getUser().hasEffect(EffectHandler.GEOMANCY) && getUser().isSprinting() && super.canUse();
    }


}
