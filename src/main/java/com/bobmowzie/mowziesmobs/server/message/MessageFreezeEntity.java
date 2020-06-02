package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageFreezeEntity extends AbstractMessage<MessageFreezeEntity> {
    private int entityID;

    public MessageFreezeEntity() {

    }

    public MessageFreezeEntity(EntityLivingBase entity) {
        entityID = entity.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
    }

    @Override
    public void onClientReceived(Minecraft client, MessageFreezeEntity message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.entityID);
        if (entity instanceof EntityLivingBase) {
            MowzieLivingProperties livingProperties = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
            livingProperties.onFreeze((EntityLivingBase) entity);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageFreezeEntity message, EntityPlayer player, MessageContext messageContext) {

    }
}
