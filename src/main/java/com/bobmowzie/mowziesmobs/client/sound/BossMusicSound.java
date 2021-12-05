package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BossMusicSound extends TickableSound {
    private MowzieEntity boss;
    private int ticksExisted = 0;
    private int timeUntilFade;

    private final SoundEvent soundEvent;
    ControlledAnimation volumeControl;

    public BossMusicSound(SoundEvent sound, MowzieEntity boss) {
        super(sound, SoundCategory.MUSIC);
        this.boss = boss;
        this.soundEvent = sound;
        this.attenuationType = AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
        this.priority = true;
        this.x = boss.getPosX();
        this.y = boss.getPosY();
        this.z = boss.getPosZ();
        volume = 1;

        volumeControl = new ControlledAnimation(40);
        timeUntilFade = 60;
    }

    public boolean shouldPlaySound() {
        return !this.boss.isSilent() && MowzieEntity.bossMusic == this;
    }

    public void tick() {
        if (boss == null || !boss.isAlive() || boss.isSilent()) volumeControl.decreaseTimer();
        else volumeControl.increaseTimer();

        if (volumeControl.getAnimationFraction() < 0.025) {
            finishPlaying();
            MowzieEntity.bossMusic = null;
        }

        volume = volumeControl.getAnimationFraction();

        if (ticksExisted % 100 == 0) {
            Minecraft.getInstance().getMusicTicker().stop();
        }
        ticksExisted++;
    }

    public void setBoss(MowzieEntity boss) {
        this.boss = boss;
    }

    public MowzieEntity getBoss() {
        return boss;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}