package com.bobmowzie.mowziesmobs.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

public class SunstrikeSound extends MovingSound {
    private EntitySunstrike sunstrike;

    public SunstrikeSound(EntitySunstrike sunstrike) {
        super(MMSounds.SUNSTRIKE, SoundCategory.NEUTRAL);
        this.sunstrike = sunstrike;
        volume = 1.5F;
        pitch = 1.1F;
        xPosF = (float) sunstrike.posX;
        yPosF = (float) sunstrike.posY;
        zPosF = (float) sunstrike.posZ;
    }

    @Override
    public void update() {
        if (sunstrike.isDead) {
            this.donePlaying = true;
        }
    }
}
