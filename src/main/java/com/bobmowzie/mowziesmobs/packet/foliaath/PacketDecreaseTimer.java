package com.bobmowzie.mowziesmobs.packet.foliaath;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketDecreaseTimer extends AbstractPacket<PacketDecreaseTimer>
{
    public int entityId;

    public PacketDecreaseTimer()
    {

    }

    public PacketDecreaseTimer(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(PacketDecreaseTimer message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath) entity).active.decreaseTimer(10);
    }

    public void handleServerMessage(PacketDecreaseTimer message, EntityPlayer player)
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