package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.SoundCategory;

public final class BlackPinkSound extends MovingSound {
    private final EntityMinecart minecart;

    public BlackPinkSound(EntityMinecart minecart) {
        super(MMSounds.MUSIC_BLACK_PINK, SoundCategory.NEUTRAL);
        this.minecart = minecart;
    }

    @Override
    public void update() {
        if (minecart.isEntityAlive()) {
            xPosF = (float) minecart.posX;
            yPosF = (float) minecart.posY;
            zPosF = (float) minecart.posZ;
        } else {
            donePlaying = true;
        }
    }
}
