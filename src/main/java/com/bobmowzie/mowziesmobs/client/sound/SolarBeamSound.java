package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SolarBeamSound extends AbstractTickableSoundInstance {
    private final EntitySolarBeam solarBeam;
    boolean endLocation = false;

    public SolarBeamSound(EntitySolarBeam solarBeam, boolean endLocation) {
        super(MMSounds.LASER.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.solarBeam = solarBeam;
        volume = 2F;
        pitch = 1.0F;
        this.endLocation = endLocation;
    }

    @Override
    public void tick() {
        if (!solarBeam.isAlive()) {
            stop();
        }
        if (endLocation && solarBeam.hasDoneRaytrace()) {
            x = (float) solarBeam.collidePosX;
            y = (float) solarBeam.collidePosY;
            z = (float) solarBeam.collidePosZ;
        }
        else {
            x = (float) solarBeam.getX();
            y = (float) solarBeam.getY();
            z = (float) solarBeam.getZ();
        }
    }
}