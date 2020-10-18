package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;

/**
 * Created by Josh on 1/9/2019.
 */
public class NagaSwoopSound extends TickableSound {
    private Entity naga;
    int ticksExisted = 0;
    boolean active = true;

    public NagaSwoopSound(Entity naga) {
        super(MMSounds.ENTITY_NAGA_SWOOP.get(), SoundCategory.HOSTILE);
        this.naga = naga;
        volume = 2F;
        pitch = 1.2f;
        xPosF = (float) naga.posX;
        yPosF = (float) naga.posY;
        zPosF = (float) naga.posZ;
        repeat = false;
    }

    @Override
    public void update() {
        if (naga != null) {
            active = true;
            xPosF = (float) naga.posX;
            yPosF = (float) naga.posY;
            zPosF = (float) naga.posZ;
            if (naga.isDead) {
                active = false;
                donePlaying = true;
            }
        }
        ticksExisted++;
    }
}
