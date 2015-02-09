package com.bobmowzie.mowziesmobs.packet.foliaath;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketIncreaseTimer extends AbstractPacket<PacketIncreaseTimer>
{
    public int entityId;

    public PacketIncreaseTimer()
    {

    }

    public PacketIncreaseTimer(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(PacketIncreaseTimer message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath)
        {
            EntityFoliaath foliaath = (EntityFoliaath) entity;
            foliaath.activate.increaseTimer();
        }
    }

    public void handleServerMessage(PacketIncreaseTimer message, EntityPlayer player)
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