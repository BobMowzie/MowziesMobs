package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.world.entity.LivingEntity;

public class BlockAbility<T extends MowzieGeckoEntity> extends SimpleAnimationAbility<T> {
    public BlockAbility(AbilityType<T, ? extends BlockAbility<T>> abilityType, T user, String animationName, int duration) {
        super(abilityType, user, animationName, duration);
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        LivingEntity blockingEntity = getUser().blockingEntity;
        if (blockingEntity != null) {
            getUser().lookAt(blockingEntity, 100, 100);
            getUser().getLookControl().setLookAt(blockingEntity, 200F, 30F);
        }
    }

    @Override
    public boolean canCancelActiveAbility() {
        return super.canCancelActiveAbility() || getUser().getActiveAbility() instanceof BlockAbility<?> || getUser().getActiveAbility() instanceof HurtAbility<?>;
    }

    @Override
    public boolean canCancelSelf() {
        return true;
    }
}
