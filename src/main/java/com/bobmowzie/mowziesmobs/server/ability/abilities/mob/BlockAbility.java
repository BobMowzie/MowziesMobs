package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animation.RawAnimation;

public class BlockAbility<T extends MowzieGeckoEntity> extends SimpleAnimationAbility<T> {
    public BlockAbility(AbilityType<T, ? extends BlockAbility<T>> abilityType, T user, RawAnimation animation, int duration) {
        super(abilityType, user, animation, duration);
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
