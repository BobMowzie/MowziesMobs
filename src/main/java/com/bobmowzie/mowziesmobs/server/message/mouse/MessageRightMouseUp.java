package com.bobmowzie.mowziesmobs.server.message.mouse;

import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Josh on 5/25/2017.
 */
public class MessageRightMouseUp extends AbstractMessage<MessageRightMouseUp> {
    public MessageRightMouseUp() {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void onClientReceived(Minecraft client, MessageRightMouseUp message, PlayerEntity player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageRightMouseUp message, PlayerEntity player, MessageContext messageContext) {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        property.mouseRightDown = false;
        if (property.usingIceBreath && property.icebreath != null) {
            property.usingIceBreath = false;
            property.icebreath.setDead();
        }
        for (int i = 0; i < property.powers.length; i++) {
            property.powers[i].onRightMouseUp(player);
        }
    }
}