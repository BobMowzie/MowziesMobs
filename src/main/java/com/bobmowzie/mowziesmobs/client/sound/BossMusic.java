package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;

public class BossMusic {
    protected MowzieEntity boss;
    protected SoundEvent soundEvent;
    protected BossMusicSound sound;
    protected boolean isPlaying;
    protected int ticksPlaying = 0;
    protected int timeUntilFade;
    ControlledAnimation volumeControl;

    public BossMusic(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        timeUntilFade = 80;

        volumeControl = new ControlledAnimation(40);
        volumeControl.setTimer(20);
    }

    public void tick() {
        // If the music should stop playing
        if (boss == null || !boss.isAlive() || boss.isSilent() || boss.isRemoved()) {
            // If the boss is dead, skip the fade timer and fade out right away
            if (boss != null && !boss.isAlive()) timeUntilFade = 0;
            boss = null;
            if (timeUntilFade > 0) timeUntilFade--;
            else volumeControl.decreaseTimer();
        }
        // If the music should keep playing
        else {
            volumeControl.increaseTimer();
            timeUntilFade = 60;
        }

        if (volumeControl.getAnimationFraction() < 0.025) {
            stop();
        }

        if (ticksPlaying % 100 == 0) {
            Minecraft.getInstance().getMusicManager().stopPlaying();
        }
        ticksPlaying++;
    }

    public void play() {
        volumeControl.setTimer(20);
        isPlaying = true;
        ticksPlaying = 0;
        if (sound != null) {
            sound = new BossMusicSound(soundEvent, getBoss(), this);
            Minecraft.getInstance().getSoundManager().play(sound);
        }
    }

    public void stop() {
        if (sound != null) sound.doStop();
        isPlaying = false;
        BossMusicPlayer.currentMusic = null;
        ticksPlaying = 0;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public MowzieEntity getBoss() {
        return boss;
    }

    public void setBoss(MowzieEntity boss) {
        this.boss = boss;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}