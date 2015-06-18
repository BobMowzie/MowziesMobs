package com.bobmowzie.mowziesmobs.common.message.foliaath;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageSetActiveFalse extends AbstractMessage<MessageSetActiveFalse>
{
    public int entityId;

    public MessageSetActiveFalse()
    {

    }

    public MessageSetActiveFalse(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(MessageSetActiveFalse message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath) entity).active = false;
    }

    public void handleServerMessage(MessageSetActiveFalse message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath) entity).active = false;
    }

    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
    }
}
