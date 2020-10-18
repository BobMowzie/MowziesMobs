package com.ilexiconn.llibrary.server.network;

import com.ilexiconn.llibrary.LLibrary;
import javafx.geometry.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public abstract class AbstractMessage<T extends AbstractMessage<T>> implements IPacket, IMessageHandler<T, IPacket> {
    @Override
    public IPacket onMessage(T message, MessageContext messageContext) {
        LLibrary.PROXY.handleMessage(message, messageContext);

        return null;
    }

    /**
     * Executes when the message is received on CLIENT side. Never use fields directly from the class you're in, but
     * use data from the 'message' argument instead.
     *
     * @param client         the minecraft client instance.
     * @param message        The message instance with all variables.
     * @param player         The client player entity.
     * @param messageContext the message context.
     */
    @OnlyIn(Dist.CLIENT)
    public abstract void onClientReceived(Minecraft client, T message, PlayerEntity player, MessageContext messageContext);

    /**
     * Executes when the message is received on SERVER side. Never use fields directly from the class you're in, but
     * use data from the 'message' argument instead.
     *
     * @param server         the minecraft server instance.
     * @param message        The message instance with all variables.
     * @param player         The player who sent the message to the server.
     * @param messageContext the message context.
     */
    public abstract void onServerReceived(MinecraftServer server, T message, PlayerEntity player, MessageContext messageContext);

    /**
     * @param side the current side
     * @return whether this message should be registered on the given side. Only used for messages registered with {@link com.ilexiconn.llibrary.server.network.NetworkHandler#registerMessage(SimpleNetworkWrapper, Class)}
     */
    // TODO: 1.13: Make abstract and rename to canSideReceive
    public boolean registerOnSide(Side side) {
        return true;
    }
}
