package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class BossMusicPlayer {
    public static BossMusic currentMusic;

    public static final BossMusic FERROUS_WROUGHTNAUT_MUSIC = new BossMusic(MMSounds.MUSIC_FERROUS_WROUGHTNAUT_THEME.get());
    public static final BossMusic UMVUTHI_MUSIC = new BossMusic(MMSounds.MUSIC_UMVUTHI_THEME.get());
    public static final BossMusic FROSTMAW_MUSIC = new BossMusic(MMSounds.MUSIC_FROSTMAW_THEME.get());
    public static final BossMusic SCULPTOR_MUSIC = new BossMusic(MMSounds.MUSIC_SCULPTOR_THEME_LEVEL1.get());
    private static final BossMusic[] BOSS_MUSICS = new BossMusic[] {
        FERROUS_WROUGHTNAUT_MUSIC,
        UMVUTHI_MUSIC,
        FROSTMAW_MUSIC,
        SCULPTOR_MUSIC
    };

    public static void requestBossMusic(MowzieEntity entity) {
        // Don't play if config has music turned off
        if (!ConfigHandler.CLIENT.playBossMusic.get()) return;

        // Get the music object for the boss theme from the entity
        BossMusic requestedMusic = entity.getBossMusic();

        if (requestedMusic != null && entity.isAlive()) {
            Player player = Minecraft.getInstance().player;
            // If there is boss music playing
            if (currentMusic != null) {
                // Don't play the music if the music settings volume is 0
                float f2 = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
                if (f2 <= 0) {
                    currentMusic = null;
                }
                // Stop the music if the player doesn't meet the criteria for hearing it
                else if (currentMusic.getBoss() == entity && !entity.canPlayerHearMusic(player)) {
                    currentMusic.setBoss(null);
                }
                // Otherwise, if the current music has no boss set and the requested music is the one currently playing, reset the current music's boss to this one
                // This is to handle cases where there are two bosses of the same type and the one handling the music dies
                else if (currentMusic.getBoss() == null && currentMusic == requestedMusic) {
                    currentMusic.setBoss(entity);
                }
            }
            // If there is no boss music playing
            else {
                // And players meet the criteria to hear this boss's music
                if (entity.canPlayerHearMusic(player)) {
                    // Then set the current music to the requested music
                    currentMusic = requestedMusic;
                    // And set its current entity to the one requesting music
                    currentMusic.setBoss(entity);
                }
            }

            // If the music exists and is not already playing, play it
            if (currentMusic != null && !currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }
    }

    public static void stopBossMusic(MowzieEntity entity) {
        if (!ConfigHandler.CLIENT.playBossMusic.get()) return;

        if (currentMusic != null && currentMusic.getBoss() == entity)
            currentMusic.stop();
    }

    public static void tick() {
        for (BossMusic music : BOSS_MUSICS) {
            if (music.isPlaying()) {
                music.tick();
            }
        }
    }
}
