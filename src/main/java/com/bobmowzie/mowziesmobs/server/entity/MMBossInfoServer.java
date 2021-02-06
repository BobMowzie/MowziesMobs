package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerBossInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MMBossInfoServer extends ServerBossInfo {
    private final MowzieEntity entity;

    private final Set<ServerPlayerEntity> unseen = new HashSet<>();

    public MMBossInfoServer(MowzieEntity entity) {
        super(entity.getDisplayName(), entity.bossBarColor(), Overlay.PROGRESS);
        this.setVisible(entity.hasBossBar());
        this.entity = entity;
    }

    public void update() {
        this.setPercent(this.entity.getHealth() / this.entity.getMaxHealth());
        Iterator<ServerPlayerEntity> it = this.unseen.iterator();
        while (it.hasNext()) {
            ServerPlayerEntity player = it.next();
            if (this.entity.getEntitySenses().canSee(player)) {
                super.addPlayer(player);
                it.remove();
            }
        }
    }

    @Override
    public void addPlayer(ServerPlayerEntity player) {
        if (this.entity.getEntitySenses().canSee(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayerEntity player) {
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}
