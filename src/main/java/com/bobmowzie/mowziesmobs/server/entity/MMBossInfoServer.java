package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.BossInfoServer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MMBossInfoServer extends BossInfoServer {
    private final MowzieEntity entity;

    private final Set<EntityPlayerMP> unseen = new HashSet<>();

    public MMBossInfoServer(MowzieEntity entity) {
        super(entity.getDisplayName(), entity.bossBarColor(), Overlay.PROGRESS);
        this.setVisible(entity.hasBossBar());
        this.entity = entity;
    }

    public void update() {
        this.setPercent(this.entity.getHealth() / this.entity.getMaxHealth());
        Iterator<EntityPlayerMP> it = this.unseen.iterator();
        while (it.hasNext()) {
            EntityPlayerMP player = it.next();
            if (this.entity.getEntitySenses().canSee(player)) {
                super.addPlayer(player);
                it.remove();
            }
        }
    }

    @Override
    public void addPlayer(EntityPlayerMP player) {
        if (this.entity.getEntitySenses().canSee(player)) {
            super.addPlayer(player);
        } else {
            this.unseen.add(player);
        }
    }

    @Override
    public void removePlayer(EntityPlayerMP player) {
        super.removePlayer(player);
        this.unseen.remove(player);
    }
}
