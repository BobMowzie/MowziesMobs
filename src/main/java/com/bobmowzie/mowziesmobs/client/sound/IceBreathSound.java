package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class IceBreathSound extends MovingSound {
    private Entity iceBreath;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public IceBreathSound(Entity icebreath) {
        super(MMSounds.ENTITY_FROSTMAW_ICEBREATH, SoundCategory.NEUTRAL);
        this.iceBreath = icebreath;
        volume = 2F;
        pitch = 1f;
        xPosF = (float) icebreath.posX;
        yPosF = (float) icebreath.posY;
        zPosF = (float) icebreath.posZ;
        volumeControl = new ControlledAnimation(10);
        repeat = true;
    }

    @Override
    public void update() {
        if (active) volumeControl.increaseTimer();
        else volumeControl.decreaseTimer();
        volume = volumeControl.getAnimationFraction();
        if (iceBreath != null) {
            active = true;
            xPosF = (float) iceBreath.posX;
            yPosF = (float) iceBreath.posY;
            zPosF = (float) iceBreath.posZ;
            if (iceBreath.isDead) {
                active = false;
                if (volumeControl.getAnimationFraction() <= 0.05) donePlaying = true;
            }
        }
        ticksExisted++;
    }
}
