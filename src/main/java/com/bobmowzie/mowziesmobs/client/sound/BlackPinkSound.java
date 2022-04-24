package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.world.entity.item.minecart.AbstractMinecart;
import net.minecraft.sounds.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BlackPinkSound extends TickableSound {
    private final AbstractMinecart minecart;

    public BlackPinkSound(AbstractMinecart minecart) {
        super(MMSounds.MUSIC_BLACK_PINK.get(), SoundCategory.NEUTRAL);
        this.minecart = minecart;
    }

    @Override
    public void tick() {
        if (minecart.isAlive()) {
            x = (float) minecart.getX();
            y = (float) minecart.getY();
            z = (float) minecart.getZ();
        } else {
            finishPlaying();
        }
    }
}
