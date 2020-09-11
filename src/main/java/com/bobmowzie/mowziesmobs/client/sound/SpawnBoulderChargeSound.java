package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;

public class SpawnBoulderChargeSound extends TickableSound {
    private PlayerEntity player;

    public SpawnBoulderChargeSound(PlayerEntity player) {
        super(MMSounds.EFFECT_GEOMANCY_BOULDER_CHARGE, SoundCategory.PLAYERS);
        this.player = player;
        volume = 1F;
        pitch = 0.95f;
        xPosF = (float) player.posX;
        yPosF = (float) player.posY;
        zPosF = (float) player.posZ;
    }

    @Override
    public void update() {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        if (!property.geomancy.isSpawningBoulder()) {
            this.donePlaying = true;
        }
    }
}
