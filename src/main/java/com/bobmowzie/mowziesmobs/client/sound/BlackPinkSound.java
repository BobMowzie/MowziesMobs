package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.SoundCategory;

public final class BlackPinkSound extends TickableSound {
    private final AbstractMinecartEntity minecart;

    public BlackPinkSound(AbstractMinecartEntity minecart) {
        super(MMSounds.MUSIC_BLACK_PINK.get(), SoundCategory.NEUTRAL);
        this.minecart = minecart;
    }

    @Override
    public void tick() {
        if (minecart.isAlive()) {
            x = (float) minecart.posX;
            y = (float) minecart.posY;
            z = (float) minecart.posZ;
        } else {
            donePlaying = true;
        }
    }
}
