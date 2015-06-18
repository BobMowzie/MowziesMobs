package com.bobmowzie.mowziesmobs.common.message.foliaath;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageSetActiveTrue extends AbstractMessage<MessageSetActiveTrue>
{
    public int entityId;

    public MessageSetActiveTrue()
    {

    }

    public MessageSetActiveTrue(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(MessageSetActiveTrue message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath) entity).active = true;
    }

    public void handleServerMessage(MessageSetActiveTrue message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath) entity).active = true;
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
