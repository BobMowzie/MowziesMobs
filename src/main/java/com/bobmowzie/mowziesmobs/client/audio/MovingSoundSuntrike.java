package com.bobmowzie.mowziesmobs.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;

public class MovingSoundSuntrike extends MovingSound
{
    private static final ResourceLocation SOUND = new ResourceLocation(MowziesMobs.MODID, "sunstrike");

    private EntitySunstrike sunstrike;

    public MovingSoundSuntrike(EntitySunstrike sunstrike)
    {
        super(SOUND);
        this.sunstrike = sunstrike;
        volume = 1.5F;
        field_147663_c = 1.1F;
        xPosF = MathHelper.floor_double(sunstrike.posX);
        yPosF = MathHelper.floor_double(sunstrike.posY);
        zPosF = MathHelper.floor_double(sunstrike.posZ);
    }

    @Override
    public void update()
    {
        if (sunstrike.isDead)
        {
            donePlaying = true;
        }
    }
}
