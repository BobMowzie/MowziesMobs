package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;

public class SunstrikeSound extends MovingSound {
    public static final ResourceLocation SOUND = new ResourceLocation(MowziesMobs.MODID, "sunstrike");

    private EntitySunstrike sunstrike;

    public SunstrikeSound(EntitySunstrike sunstrike) {
        super(SOUND);
        this.sunstrike = sunstrike;
        volume = 1.5F;
        field_147663_c = 1.1F;
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
