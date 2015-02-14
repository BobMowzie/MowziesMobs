package com.bobmowzie.mowziesmobs.packet.foliaath;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSetActiveTrue extends AbstractPacket<PacketSetActiveTrue>
{
    public int entityId;

    public PacketSetActiveTrue()
    {

    }

    public PacketSetActiveTrue(int id)
    {
        entityId = id;
    }

    public void handleClientMessage(PacketSetActiveTrue message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath)entity).active = true;
    }

    public void handleServerMessage(PacketSetActiveTrue message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath)entity).active = true;
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
