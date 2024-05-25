package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public abstract class HeliomancyAbilityBase extends PlayerAbility {
    public HeliomancyAbilityBase(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
    }

    public HeliomancyAbilityBase(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack) {
        super(abilityType, user, sectionTrack);
    }

    @Override
    public void start() {
        super.start();
        getUser().removeEffect(EffectHandler.SUNS_BLESSING.get());
        getUser().addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING.get(), ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get() * 60 * 20, 0, false, false));
    }

    @Override
    public boolean canUse() {
        if (getUser() == null || !getUser().getInventory().getSelected().isEmpty()) return false;
        return getUser().hasEffect(EffectHandler.SUNS_BLESSING.get()) && super.canUse();
    }
}
