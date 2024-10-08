package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;

public class BossMusic {
    protected MowzieEntity boss;
    protected SoundEvent soundEvent;
    protected BossMusicSound sound;
    protected boolean isPlaying;
    protected int ticksPlaying = 0;
    protected int timeUntilFade;

    public BossMusic(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        timeUntilFade = 80;
    }

    public void tick() {
        if (soundEvent != null) {
            // If the music should stop playing
            if (boss == null || !boss.isAlive() || boss.isSilent()) {
                // If the boss is dead, skip the fade timer and fade out right away
                if (boss != null && !boss.isAlive()) timeUntilFade = 0;
                boss = null;
                if (timeUntilFade > 0) timeUntilFade--;
                else sound.fadeOut();
            }
            // If the music should keep playing
            else {
                sound.fadeIn();
                timeUntilFade = 60;
            }

            if (sound.volumeControl.getAnimationFraction() < 0.025) {
                stop();
            }

            if (ticksPlaying % 100 == 0) {
                Minecraft.getInstance().getMusicManager().stopPlaying();
            }
            ticksPlaying++;
        }
    }

    public void play() {
        sound = new BossMusicSound(soundEvent, getBoss(), this);
        Minecraft.getInstance().getSoundManager().play(sound);
        isPlaying = true;
    }

    public void stop() {
        sound.doStop();
        isPlaying = false;
        BossMusicPlayer.currentMusic = null;
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

    protected void finishFadeOut() {
        isPlaying = false;
    }
}