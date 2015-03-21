package com.bobmowzie.mowziesmobs.packet.foliaath;

import com.bobmowzie.mowziesmobs.entity.EntityBabyFoliaath;
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
        System.out.println("Packet is sent");
        Entity entity = player.worldObj.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath)
        {
            EntityFoliaath foliaath = (EntityFoliaath) entity;
            foliaath.activate.decreaseTimer(2);
            return;
        }
        if (entity != null && entity instanceof EntityBabyFoliaath)
        {
            System.out.println("Closing Mouth");
            EntityBabyFoliaath foliaath = (EntityBabyFoliaath) entity;
            foliaath.activate.decreaseTimer();
            return;
        }
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