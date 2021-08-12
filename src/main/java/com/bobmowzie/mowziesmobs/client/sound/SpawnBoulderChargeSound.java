package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.abilities.SpawnBoulderAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpawnBoulderChargeSound extends TickableSound {
    private final LivingEntity user;
    private final SpawnBoulderAbility ability;

    public SpawnBoulderChargeSound(LivingEntity user) {
        super(MMSounds.EFFECT_GEOMANCY_BOULDER_CHARGE.get(), SoundCategory.PLAYERS);
        this.user = user;
        volume = 1F;
        pitch = 0.95f;
        x = (float) user.getPosX();
        y = (float) user.getPosY();
        z = (float) user.getPosZ();

        AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(user);
        if (capability != null) {
            ability = (SpawnBoulderAbility) capability.getAbilityMap().get(AbilityHandler.SPAWN_BOULDER_ABILITY);
        }
        else ability = null;
    }

    @Override
    public void tick() {
        if (ability == null) {
            finishPlaying();
            return;
        }
        if (!ability.isUsing() || ability.getCurrentSection().sectionType != AbilitySection.AbilitySectionType.STARTUP) {
            finishPlaying();
        }
    }
}
