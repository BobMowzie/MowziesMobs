package com.bobmowzie.mowziesmobs.server.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;

public class ServerMessageContext extends MessageContext {
    public ServerMessageContext(final NetworkEvent.Context context) {
        super(context);
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }

    public MinecraftServer getServer() {
        return this.getPlayer().server;
    }

    public ServerWorld getWorld() {
        return this.getPlayer().server.getWorlds().iterator().next();   // TODO: Not sure if this is right. What replaces getPlayer().getServerWorld()?
    }

    public ServerPlayerEntity getPlayer() {
        return Objects.requireNonNull(this.context.getSender());
    }
}
