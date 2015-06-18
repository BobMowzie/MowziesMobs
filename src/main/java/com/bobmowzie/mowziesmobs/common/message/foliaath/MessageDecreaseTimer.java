package com.bobmowzie.mowziesmobs.common.message.foliaath;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageDecreaseTimer extends AbstractMessage<MessageDecreaseTimer>
{
    public int entityId;

    public MessageDecreaseTimer()
    {

    }

    public MessageDecreaseTimer(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(MessageDecreaseTimer message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath)
        {
            EntityFoliaath foliaath = (EntityFoliaath) entity;
            foliaath.activate.decreaseTimer(2);
        }
    }

    public void handleServerMessage(MessageDecreaseTimer message, EntityPlayer player)
    {

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