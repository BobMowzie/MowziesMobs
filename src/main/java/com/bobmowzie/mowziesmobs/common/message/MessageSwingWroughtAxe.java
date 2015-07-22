package com.bobmowzie.mowziesmobs.common.message;

import com.bobmowzie.mowziesmobs.common.property.WroughtAxeSwingProperty;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageSwingWroughtAxe extends AbstractMessage<MessageSwingWroughtAxe>
{
    private int entityID;

    public MessageSwingWroughtAxe()
    {
    }

    public MessageSwingWroughtAxe(EntityPlayer player)
    {
        entityID = player.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityID);
    }

    @Override
    public void handleClientMessage(MessageSwingWroughtAxe message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityID);
        if (entity instanceof EntityPlayer)
        {
            WroughtAxeSwingProperty.getProperty((EntityPlayer) entity).swing();
        }
    }

    @Override
    public void handleServerMessage(MessageSwingWroughtAxe message, EntityPlayer player)
    {
    }
}
