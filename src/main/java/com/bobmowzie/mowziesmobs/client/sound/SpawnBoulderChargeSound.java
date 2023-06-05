package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy.SpawnBoulderAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpawnBoulderChargeSound extends AbstractTickableSoundInstance {
    private final LivingEntity user;
    private final SpawnBoulderAbility ability;

    public SpawnBoulderChargeSound(LivingEntity user) {
        super(MMSounds.EFFECT_GEOMANCY_BOULDER_CHARGE.get(), SoundSource.PLAYERS);
        this.user = user;
        volume = 1F;
        pitch = 0.95f;
        x = (float) user.getX();
        y = (float) user.getY();
        z = (float) user.getZ();

        AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(user);
        if (capability != null) {
            ability = (SpawnBoulderAbility) capability.getAbilityMap().get(AbilityHandler.SPAWN_BOULDER_ABILITY);
        }
        else ability = null;
    }

    @Override
    public void tick() {
        if (ability == null) {
            stop();
            return;
        }
        if (!ability.isUsing() || ability.getCurrentSection().sectionType != AbilitySection.AbilitySectionType.STARTUP) {
            stop();
        }
    }
}
