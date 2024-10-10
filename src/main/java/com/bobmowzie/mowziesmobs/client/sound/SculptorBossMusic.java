package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SculptorBossMusic extends BossMusic<EntitySculptor> {
    protected static SoundEvent soundEventIntro = MMSounds.MUSIC_SCULPTOR_THEME_INTRO.get();
    protected static SoundEvent soundEventLevel1 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL1.get();
    protected static SoundEvent soundEventLevel2 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL2.get();
    protected static SoundEvent soundEventLevel3 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL3.get();
    protected static SoundEvent soundEventTransition = MMSounds.MUSIC_SCULPTOR_THEME_TRANSITION.get();
    protected static SoundEvent soundEventLevel4 = MMSounds.MUSIC_SCULPTOR_THEME_LEVEL4.get();
    protected static SoundEvent soundEventEnding = MMSounds.MUSIC_SCULPTOR_THEME_ENDING.get();
    protected static SoundEvent soundEventOutro = MMSounds.MUSIC_SCULPTOR_THEME_OUTRO.get();

    protected BossMusicSound soundIntro;
    protected BossMusicSound soundTransition;
    protected BossMusicSound soundEnding;
    protected BossMusicSound soundOutro;
    protected BossMusicSound currentSound;

    private int ticksInSection;

    private enum SculptorMusicSection {
        INTRO,
        LEVEL1,
        LEVEL2,
        LEVEL3,
        TRANSITION,
        LEVEL4,
        ENDING,
        OUTRO
    }

    private static final SortedMap<SculptorMusicSection, Float> SECTION_HEIGHTS = new TreeMap<>();
    static {
        SECTION_HEIGHTS.put(SculptorMusicSection.LEVEL1, 0.0f);
        SECTION_HEIGHTS.put(SculptorMusicSection.LEVEL2, 0.1f);
        SECTION_HEIGHTS.put(SculptorMusicSection.LEVEL3, 0.35f);
        SECTION_HEIGHTS.put(SculptorMusicSection.LEVEL4, 0.65f);
        SECTION_HEIGHTS.put(SculptorMusicSection.ENDING, 0.98f);
    }
    private static final Map<SculptorMusicSection, SoundEvent> SECTION_SOUNDS = new HashMap<>();
    static {
        SECTION_SOUNDS.put(SculptorMusicSection.INTRO, soundEventIntro);
        SECTION_SOUNDS.put(SculptorMusicSection.LEVEL1, soundEventLevel1);
        SECTION_SOUNDS.put(SculptorMusicSection.LEVEL2, soundEventLevel2);
        SECTION_SOUNDS.put(SculptorMusicSection.LEVEL3, soundEventLevel3);
        SECTION_SOUNDS.put(SculptorMusicSection.TRANSITION, soundEventTransition);
        SECTION_SOUNDS.put(SculptorMusicSection.LEVEL4, soundEventLevel4);
        SECTION_SOUNDS.put(SculptorMusicSection.ENDING, soundEventEnding);
        SECTION_SOUNDS.put(SculptorMusicSection.OUTRO, soundEventOutro);
    }

    private SculptorMusicSection currentSection;

    public SculptorBossMusic() {
        super(null);
    }

    @Override
    public void tick() {
        super.tick();
        ticksInSection++;

        if (getBoss() != null) {
            if (currentSection == SculptorMusicSection.INTRO && ticksInSection == 35) {
                startMainTrack();
                return;
            }

            if (currentSection == SculptorMusicSection.TRANSITION) {
                if (ticksInSection == 512) {
                    changeLevelSection(SculptorMusicSection.LEVEL4);
                    return;
                }
            }

            if (currentSection == SculptorMusicSection.ENDING) {
                if (ticksInSection % 64 == 0 && getBoss().isTestPassed()) {
                    changeLevelSection(SculptorMusicSection.OUTRO, false);
                    return;
                }
            }

            if (
                    currentSection == SculptorMusicSection.LEVEL1 ||
                    currentSection == SculptorMusicSection.LEVEL2 ||
                    currentSection == SculptorMusicSection.LEVEL3 ||
                    currentSection == SculptorMusicSection.TRANSITION ||
                    currentSection == SculptorMusicSection.LEVEL4 ||
                    currentSection == SculptorMusicSection.ENDING
            ) {
                if (ticksInSection % 128 == 0) {
                    measureBreak();
                }
            }
        }
    }

    private void startMainTrack() {
        ticksInSection = 0;
        changeLevelSection(SculptorMusicSection.LEVEL1);
    }

    private void measureBreak() {
        if (getBoss().isTestPassed()) {
            changeLevelSection(SculptorMusicSection.OUTRO, false);
            return;
        }

        SculptorMusicSection currentSectionIgnoreTransition = currentSection;
        if (currentSection == SculptorMusicSection.TRANSITION) currentSectionIgnoreTransition = SculptorMusicSection.LEVEL4;

        float playerProgress = getBoss().playerProgress();
        float currentSectionHeight = SECTION_HEIGHTS.get(currentSectionIgnoreTransition);
        SculptorMusicSection nextSection = SculptorMusicSection.LEVEL1;
        for (Map.Entry<SculptorMusicSection, Float> sectionHeight : SECTION_HEIGHTS.entrySet()) {
            SculptorMusicSection section = sectionHeight.getKey();
            float height = sectionHeight.getValue();
            // If the current section is above the height we are checking, then the player moved down. We add a slight buffer before switching tracks.
            if (currentSectionHeight >= height) {
                height -= 0.05;
            }
            // If the player is in this height range, play the associated section
            if (playerProgress > height) {
                nextSection = section;
            }
        }
        if (nextSection != currentSectionIgnoreTransition) {
            if (nextSection == SculptorMusicSection.LEVEL4) {
                nextSection = SculptorMusicSection.TRANSITION;
            }
            changeLevelSection(nextSection);
        }
    }

    private void changeLevelSection(SculptorMusicSection section) {
        changeLevelSection(section, true);
    }

    private void changeLevelSection(SculptorMusicSection section, boolean loop) {
        if (currentSound != null) {
            currentSound.fadeOut();
        }
        SoundEvent requestedSoundEvent = SECTION_SOUNDS.get(section);
        currentSound = new BossMusicSound(requestedSoundEvent, getBoss(), this, loop);
        Minecraft.getInstance().getSoundManager().play(currentSound);
        currentSection = section;
        ticksInSection = 0;
    }

    public void play() {
        super.play();
        currentSection = SculptorMusicSection.INTRO;
        soundIntro = new BossMusicSound(soundEventIntro, getBoss(), this, false);
        Minecraft.getInstance().getSoundManager().play(soundIntro);
        ticksInSection = 0;
    }

    @Override
    public void stop() {
        if (soundIntro != null) soundIntro.doStop();
        if (soundTransition != null) soundTransition.doStop();
        if (soundEnding != null) soundEnding.doStop();
        if (soundOutro != null) soundOutro.doStop();
        if (currentSound != null) currentSound.doStop();
        super.stop();
    }
}
