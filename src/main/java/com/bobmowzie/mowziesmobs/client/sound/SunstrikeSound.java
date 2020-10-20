package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;

public class SunstrikeSound extends TickableSound {
    private EntitySunstrike sunstrike;

    public SunstrikeSound(EntitySunstrike sunstrike) {
        super(MMSounds.SUNSTRIKE.get(), SoundCategory.NEUTRAL);
        this.sunstrike = sunstrike;
        volume = 1.5F;
        pitch = 1.1F;
        x = (float) sunstrike.posX;
        y = (float) sunstrike.posY;
        z = (float) sunstrike.posZ;
    }

    @Override
    public void tick() {
        if (!sunstrike.isAlive()) {
            this.donePlaying = true;
        }
    }
}
