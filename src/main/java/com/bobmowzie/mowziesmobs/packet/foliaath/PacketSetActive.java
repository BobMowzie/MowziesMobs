package com.bobmowzie.mowziesmobs.packet.foliaath;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSetActive extends AbstractPacket<PacketSetActive>
{
    public int entityId;
    public boolean b;

    public PacketSetActive()
    {

    }

    public PacketSetActive(int id, boolean b)
    {
        entityId = id;
        this.b = b;
    }

    public void handleClientMessage(PacketSetActive message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath)entity).active = b;
    }

    public void handleServerMessage(PacketSetActive message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath) ((EntityFoliaath)entity).active = b;
    }

    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        b = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeBoolean(b);
    }
}
