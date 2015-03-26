package com.bobmowzie.mowziesmobs.packet.foliaath;

import com.bobmowzie.mowziesmobs.entity.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.packet.AbstractPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncTickGrowth extends AbstractPacket<PacketSyncTickGrowth>
{
    public int entityId;
    public int tickGrowth;

    public PacketSyncTickGrowth()
    {

    }

    public PacketSyncTickGrowth(int id, int tick)
    {
        entityId = id;
        tickGrowth = tick;
    }

    public void handleClientMessage(PacketSyncTickGrowth message, EntityPlayer player)
    {
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityBabyFoliaath)
        {
            EntityBabyFoliaath foliaath = (EntityBabyFoliaath) entity;
            foliaath.tickGrowth = message.tickGrowth;
        }
    }

    public void handleServerMessage(PacketSyncTickGrowth message, EntityPlayer player)
    {

    }

    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        tickGrowth = buf.readInt();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeInt(tickGrowth);
    }
}
