package com.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.event.SurvivalTabClickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SurvivalTabMessage extends AbstractMessage<SurvivalTabMessage> {
    private String label;

    public SurvivalTabMessage() {

    }

    public SurvivalTabMessage(String label) {
        this.label = label;
    }

    @Override
    public void onClientReceived(Minecraft client, SurvivalTabMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, SurvivalTabMessage message, EntityPlayer player, MessageContext messageContext) {
        MinecraftForge.EVENT_BUS.post(new SurvivalTabClickEvent(message.label, player));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.label = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.label);
    }
}
