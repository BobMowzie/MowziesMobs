package com.bobmowzie.mowziesmobs.packet;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketIncreaseTimer extends AbstractPacket<PacketIncreaseTimer> {

    public int entityId;

    @Override
    public void handleClientMessage(PacketIncreaseTimer message, EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.theWorld.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath)
        {
            ((EntityFoliaath) entity).active.increaseTimer();
        }
        else System.out.println("Entity null..... I cri evrtiem");
    }

    @Override
    public void handleServerMessage(PacketIncreaseTimer message, EntityPlayer player) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    public PacketIncreaseTimer(int entityId) {
        this.entityId = entityId;
    }

    public PacketIncreaseTimer() {
    }
}