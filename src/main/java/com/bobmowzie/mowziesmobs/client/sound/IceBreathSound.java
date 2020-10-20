package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;

public class IceBreathSound extends TickableSound {
    private Entity iceBreath;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public IceBreathSound(Entity icebreath) {
        super(MMSounds.ENTITY_FROSTMAW_ICEBREATH.get(), SoundCategory.NEUTRAL);
        this.iceBreath = icebreath;
        volume = 2F;
        pitch = 1f;
        x = (float) icebreath.posX;
        y = (float) icebreath.posY;
        z = (float) icebreath.posZ;
        volumeControl = new ControlledAnimation(10);
        repeat = true;
    }

    @Override
    public void tick() {
        if (active) volumeControl.increaseTimer();
        else volumeControl.decreaseTimer();
        volume = volumeControl.getAnimationFraction();
        if (iceBreath != null) {
            active = true;
            x = (float) iceBreath.posX;
            y = (float) iceBreath.posY;
            z = (float) iceBreath.posZ;
            if (!iceBreath.isAlive()) {
                active = false;
                if (volumeControl.getAnimationFraction() <= 0.05) donePlaying = true;
            }
        }
        ticksExisted++;
    }
}
