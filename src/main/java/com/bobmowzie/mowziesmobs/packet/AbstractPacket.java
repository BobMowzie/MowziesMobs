package com.bobmowzie.mowziesmobs.packet;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractPacket<REQ extends AbstractPacket> implements IMessage, IMessageHandler<REQ, IMessage>
{
    public IMessage onMessage(REQ message, MessageContext ctx)
    {
        if (ctx.side.isClient()) handleClientMessage(message, MowziesMobs.proxy.getClientPlayer());
        else handleServerMessage(message, ctx.getServerHandler().playerEntity);
        return null;
    }

    public abstract void handleClientMessage(REQ message, EntityPlayer player);

    public abstract void handleServerMessage(REQ message, EntityPlayer player);
}