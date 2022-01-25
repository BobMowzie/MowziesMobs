package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class BossMusicPlayer {
    public static BossMusicSound bossMusic;

    public static void playBossMusic(MowzieEntity entity) {
        SoundEvent soundEvent = entity.getBossMusic();
        if (soundEvent != null && entity.isAlive()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (bossMusic != null) {
                float f2 = Minecraft.getInstance().gameSettings.getSoundLevel(SoundCategory.MUSIC);
                if (f2 <= 0) {
                    bossMusic = null;
                }
                else if (bossMusic.getBoss() == entity && !entity.canPlayerHearMusic(player)) {
                    bossMusic.setBoss(null);
                }
            }
            else {
                if (entity.canPlayerHearMusic(player)) {
                    if (bossMusic == null) {
                        bossMusic = new BossMusicSound(entity.getBossMusic(), entity);
                    }
                    else if (bossMusic.getBoss() == null && bossMusic.getSoundEvent() == soundEvent) {
                        bossMusic.setBoss(entity);
                    }
                }
            }
            if (bossMusic != null && !Minecraft.getInstance().getSoundHandler().isPlaying(bossMusic)) {
                Minecraft.getInstance().getSoundHandler().play(bossMusic);
            }
        }
    }

    public static void stopBossMusic(MowzieEntity entity) {
        if (bossMusic != null && bossMusic.getBoss() == entity)
            bossMusic.setBoss(null);
    }
}
