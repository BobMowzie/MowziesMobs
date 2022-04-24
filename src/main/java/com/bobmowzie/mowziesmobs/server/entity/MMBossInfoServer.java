package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MMBossEventServer extends ServerBossEvent {
    private final MowzieEntity entity;

    private final Set<ServerPlayer> unseen = new HashSet<>();

    public MMBossEventServer(MowzieEntity entity) {
        super(entity.getDisplayName(), entity.bossBarColor(), Overlay.PROGRESS);
        this.setVisible(entity.hasBossBar());
        this.entity = entity;
    }

    public void update() {
        this.setPercent(this.entity.getHealth() / this.entity.getMaxHealth());
        Iterator<ServerPlayer> it = this.unseen.iterator();
        while (it.hasNext()) {
            ServerPlayer player = it.next();
            if (this.entity.getEntitySenses().canSee(player)) {
                super.addPlayer(player);
                it.remove();
            }
        }
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        if (this.entity.getEntitySenses().canSee(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}
