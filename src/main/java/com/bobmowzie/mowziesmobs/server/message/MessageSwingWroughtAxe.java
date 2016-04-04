package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.property.WroughtAxeSwingProperty;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class MessageSwingWroughtAxe extends AbstractMessage<MessageSwingWroughtAxe> {
    private int entityID;

    public MessageSwingWroughtAxe() {

    }

    public MessageSwingWroughtAxe(EntityPlayer player) {
        entityID = player.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
    }

    @Override
    public void onClientReceived(Minecraft client, MessageSwingWroughtAxe message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.worldObj.getEntityByID(message.entityID);
        if (entity instanceof EntityPlayer) {
            WroughtAxeSwingProperty.getProperty((EntityPlayer) entity).swing();
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSwingWroughtAxe message, EntityPlayer player, MessageContext messageContext) {

    }
}
