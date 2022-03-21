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

        volumeControl = new ControlledAnimation(40);
        volumeControl.setTimer(20);
        volume = volumeControl.getAnimationFraction();
        timeUntilFade = 80;
    }

    public boolean shouldPlaySound() {
        return BossMusicPlayer.bossMusic == this;
    }

    public void tick() {
        // If the music should stop playing
        if (boss == null || !boss.isAlive() || boss.isSilent()) {
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
            finishPlaying();
            BossMusicPlayer.bossMusic = null;
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