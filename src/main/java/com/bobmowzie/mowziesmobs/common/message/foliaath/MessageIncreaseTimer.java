package com.bobmowzie.mowziesmobs.common.message.foliaath;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageIncreaseTimer extends AbstractMessage<MessageIncreaseTimer>
{
    public int entityId;

    public MessageIncreaseTimer()
    {

    }

    public MessageIncreaseTimer(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(MessageIncreaseTimer message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath)
        {
            EntityFoliaath foliaath = (EntityFoliaath) entity;
            foliaath.activate.increaseTimer();
        }
    }

    public void handleServerMessage(MessageIncreaseTimer message, EntityPlayer player)
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