package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;

public class SculptorBossMusic extends BossMusic {
    protected SoundEvent soundEventIntro;
    protected SoundEvent soundEventLevel1;
    protected SoundEvent soundEventLevel2;
    protected SoundEvent soundEventLevel3;
    protected SoundEvent soundEventTransition;
    protected SoundEvent soundEventLevel4;
    protected SoundEvent soundEventEnding;
    protected SoundEvent soundEventOutro;

    protected BossMusicSound soundIntro;
    protected BossMusicSound soundLevel1;
    protected BossMusicSound soundLevel2;
    protected BossMusicSound soundLevel3;
    protected BossMusicSound soundTransition;
    protected BossMusicSound soundLevel4;
    protected BossMusicSound soundEnding;
    protected BossMusicSound soundOutro;

    private enum SculptorMusicSection {
        INTRO,
        LEVEL1,
        LEVEL2,
        TRANSITION,
        LEVEL3,
        ENDING,
        OUTRO
    }

    private SculptorMusicSection section;

    public SculptorBossMusic() {
        super(null);
        soundEventIntro = MMSounds.MUSIC_SCULPTOR_THEME_INTRO.get();
        soundEventLevel1 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL1.get();
        soundEventLevel2 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL2.get();
        soundEventLevel3 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL3.get();
        soundEventTransition = MMSounds.MUSIC_SCULPTOR_THEME_TRANSITION.get();
        soundEventLevel4 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL4.get();
        soundEventEnding = MMSounds.MUSIC_SCULPTOR_THEME_ENDING.get();
        soundEventOutro = MMSounds.MUSIC_SCULPTOR_THEME_OUTRO.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (getBoss() instanceof EntitySculptor sculptor) {
            if (sculptor.isTesting()) {
                // Level 3
                if (sculptor.playerProgress() > 0.66) {
                    soundLevel3.fadeIn();
                    soundLevel2.fadeOut();
                    soundLevel1.fadeOut();
                }
                // Level 2
                else if (sculptor.playerProgress() > 0.33) {
                    soundLevel3.fadeOut();
                    soundLevel2.fadeIn();
                    soundLevel1.fadeOut();
                }
                // Level 1
                else {
                    soundLevel3.fadeOut();
                    soundLevel2.fadeOut();
                    soundLevel1.fadeIn();
                }
            }
        }
    }

    public void play() {
        super.play();
        section = SculptorMusicSection.INTRO;
        soundLevel1 = new BossMusicSound(soundEventLevel1, getBoss(), this);
        soundLevel2 = new BossMusicSound(soundEventLevel2, getBoss(), this);
        soundLevel2.cutOut();
        soundLevel3 = new BossMusicSound(soundEventLevel3, getBoss(), this);
        soundLevel3.cutOut();
        Minecraft.getInstance().getSoundManager().play(soundLevel1);
        Minecraft.getInstance().getSoundManager().play(soundLevel2);
        Minecraft.getInstance().getSoundManager().play(soundLevel3);
    }

    @Override
    public void stop() {
        if (soundLevel1 != null) soundLevel1.doStop();
        if (soundLevel2 != null) soundLevel2.doStop();
        if (soundLevel3 != null) soundLevel3.doStop();
        super.stop();
    }
}
