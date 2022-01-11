package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SunblockSound extends TickableSound {
    private final LivingEntity entity;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public SunblockSound(LivingEntity entity) {
        super(MMSounds.ENTITY_BARAKOA_HEAL_LOOP.get(), SoundCategory.NEUTRAL);
        this.entity = entity;
        volume = 4F;
        pitch = 1f;
        x = (float) entity.getPosX();
        y = (float) entity.getPosY();
        z = (float) entity.getPosZ();
        volumeControl = new ControlledAnimation(10);
        repeat = true;
    }

    @Override
    public void tick() {
        if (active) volumeControl.increaseTimer();
        else volumeControl.decreaseTimer();
        volume = volumeControl.getAnimationFraction();
        if (volumeControl.getAnimationFraction() <= 0.05)
            finishPlaying();
        if (entity != null) {
            active = true;
            x = (float) entity.getPosX();
            y = (float) entity.getPosY();
            z = (float) entity.getPosZ();
            boolean barakoaHealing = false;
            if (entity instanceof EntityBarakoa) {
                EntityBarakoa barakoa = (EntityBarakoa) entity;
                barakoaHealing = barakoa.getAnimation() == EntityBarakoa.HEAL_LOOP_ANIMATION || barakoa.getAnimation() == EntityBarakoa.HEAL_START_ANIMATION;
            }
            boolean hasSunblock = entity.isPotionActive(EffectHandler.SUNBLOCK);
            active = barakoaHealing || hasSunblock;
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
