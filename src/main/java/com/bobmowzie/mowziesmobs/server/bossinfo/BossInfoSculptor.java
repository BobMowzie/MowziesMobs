package com.bobmowzie.mowziesmobs.server.bossinfo;

import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.google.common.base.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerPlayer;

public class BossInfoSculptor extends MMBossInfoServer {
    private final EntitySculptor sculptor;
    private Component displayName;
    private static final Component TESTING_NAME = Component.translatable("entity.mowziesmobs.sculptor.boss_info.timer.text");

    public BossInfoSculptor(EntitySculptor entity) {
        super(entity);
        sculptor = entity;
    }

    public void update() {
        if (sculptor.isTesting()) {
            this.setProgress(1.0f - this.sculptor.getTestTimePassed() / (float) this.sculptor.getMaxTestTime());
            if (this.sculptor.getTestingPlayer() instanceof ServerPlayer) {
                this.addPlayer((ServerPlayer) this.sculptor.getTestingPlayer());
            }
            setDisplayName(TESTING_NAME);
            setVisible(true);
        }
        else {
            setVisible(sculptor.hasBossBar() && sculptor.isFighting());
            setDisplayName(name);
            super.update();
        }
    }

    @Override
    public Component getName() {
        return displayName;
    }

    public void setDisplayName(Component displayName) {
        if (!Objects.equal(displayName, this.displayName)) {
            this.displayName = displayName;

            if (this.isVisible()) {
                ClientboundBossEventPacket clientboundbosseventpacket = ClientboundBossEventPacket.createUpdateNamePacket(this);

                for (ServerPlayer serverplayer : this.getPlayers()) {
                    serverplayer.connection.send(clientboundbosseventpacket);
                }
            }
        }
    }
}
