package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SunblockSound extends AbstractTickableSoundInstance {
    private final LivingEntity entity;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public SunblockSound(LivingEntity entity) {
        super(MMSounds.ENTITY_UMVUTHANA_HEAL_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.entity = entity;
        volume = 4F;
        pitch = 1f;
        x = (float) entity.getX();
        y = (float) entity.getY();
        z = (float) entity.getZ();
        volumeControl = new ControlledAnimation(10);
        looping = true;
    }

    @Override
    public void tick() {
        if (active) volumeControl.increaseTimer();
        else volumeControl.decreaseTimer();
        volume = volumeControl.getAnimationFraction();
        if (volumeControl.getAnimationFraction() <= 0.05)
            stop();
        if (entity != null) {
            active = true;
            x = (float) entity.getX();
            y = (float) entity.getY();
            z = (float) entity.getZ();
            boolean umvuthanaHealing = false;
            if (entity instanceof EntityUmvuthana) {
                EntityUmvuthana umvuthana = (EntityUmvuthana) entity;
//                umvuthanaHealing = umvuthana.getAnimation() == EntityBarakoa.HEAL_LOOP_ANIMATION || umvuthana.getAnimation() == EntityBarakoa.HEAL_START_ANIMATION; TODO
            }
            boolean hasSunblock = entity.hasEffect(EffectHandler.SUNBLOCK.get());
            active = umvuthanaHealing || hasSunblock;
            if (!entity.isAlive()) {
                active = false;
            }
        }
        else {
            active = false;
        }
        ticksExisted++;
    }
}
