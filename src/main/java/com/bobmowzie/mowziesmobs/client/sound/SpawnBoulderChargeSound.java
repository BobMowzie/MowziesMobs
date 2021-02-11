package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;

public class SpawnBoulderChargeSound extends TickableSound {
    private PlayerEntity player;

    public SpawnBoulderChargeSound(PlayerEntity player) {
        super(MMSounds.EFFECT_GEOMANCY_BOULDER_CHARGE.get(), SoundCategory.PLAYERS);
        this.player = player;
        volume = 1F;
        pitch = 0.95f;
        x = (float) player.getPosX();
        y = (float) player.getPosY();
        z = (float) player.getPosZ();
    }

    @Override
    public void tick() {
        PlayerCapability.IPlayerCapability capability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (!capability.getGeomancy().isSpawningBoulder()) {
            this.donePlaying = true;
        }
    }
}
