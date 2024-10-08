package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class BossMusicSound extends AbstractTickableSoundInstance {
    private MowzieEntity boss;
    private BossMusic music;

    private final SoundEvent soundEvent;
    ControlledAnimation volumeControl;

    private boolean shouldPlay;

    public BossMusicSound(SoundEvent sound, MowzieEntity boss, BossMusic music) {
        super(sound, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.soundEvent = sound;
        this.boss = boss;
        this.music = music;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.x = boss.getX();
        this.y = boss.getY();
        this.z = boss.getZ();

        volumeControl = new ControlledAnimation(40);
        volumeControl.setTimer(40);
        volume = volumeControl.getAnimationFraction() * music.volumeControl.getAnimationFraction();

        shouldPlay = true;
    }

    public boolean canPlaySound() {
        return true;
    }

    public void tick() {
        if (shouldPlay) {
            volumeControl.increaseTimer();
        }
        else {
            volumeControl.decreaseTimer();
        }

        if (music.volumeControl.getAnimationFraction() < 0.025) {
            stop();
        }

        volume = volumeControl.getAnimationFraction() * music.volumeControl.getAnimationFraction();
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

    public void doStop() {
        stop();
    }

    public void fadeOut() {
        shouldPlay = false;
    }

    public void fadeIn() {
        shouldPlay = true;
    }

    public void cutIn() {
        shouldPlay = true;
        volumeControl.setTimer(40);
    }

    public void cutOut() {
        shouldPlay = false;
        volumeControl.setTimer(0);
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}