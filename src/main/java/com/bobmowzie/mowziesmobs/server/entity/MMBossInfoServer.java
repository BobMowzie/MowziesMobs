package com.bobmowzie.mowziesmobs.server.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;

public class MMBossInfoServer extends ServerBossEvent {
    private final MowzieEntity entity;
    private final ResourceLocation bossBarLocation;
    private final ResourceLocation bossBarOverlayLocation;

    private final Set<ServerPlayer> unseen = new HashSet<>();

    public MMBossInfoServer(MowzieEntity entity) {
        this(entity, null, null);
    }

    public MMBossInfoServer(MowzieEntity entity, ResourceLocation bossBarLocation, ResourceLocation bossBarOverlayLocation) {
        super(entity.getDisplayName(), entity.bossBarColor(), BossBarOverlay.PROGRESS);
        this.setVisible(entity.hasBossBar());
        this.entity = entity;
        this.bossBarLocation = bossBarLocation;
        this.bossBarOverlayLocation = bossBarOverlayLocation;
    }

    public void update() {
        this.setProgress(this.entity.getHealth() / this.entity.getMaxHealth());
        Iterator<ServerPlayer> it = this.unseen.iterator();
        while (it.hasNext()) {
            ServerPlayer player = it.next();
            if (this.entity.getSensing().hasLineOfSight(player)) {
                super.addPlayer(player);
                it.remove();
            }
        }
    }

    //FIXME
    @Override
    public void addPlayer(ServerPlayer player) {
        //MowziesMobs.NETWORK.sendTo(new MessageUpdateBossBar(this.getId(), bossBarLocation, bossBarOverlayLocation), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        if (this.entity.getSensing().hasLineOfSight(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        //MowziesMobs.NETWORK.sendTo(new MessageUpdateBossBar(this.getId(), null, null), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}
