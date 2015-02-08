package com.bobmowzie.mowziesmobs.packet;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketDecreaseTimer extends AbstractPacket<PacketDecreaseTimer> {

    public int entityId;

    @Override
    public void handleClientMessage(PacketDecreaseTimer message, EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.theWorld.getEntityByID(message.entityId);
        if (entity != null && entity instanceof EntityFoliaath)
        {
            ((EntityFoliaath) entity).active.decreaseTimer();
        }
        else System.out.println("Entity null..... I cri evrtiem");
    }

    @Override
    public void handleServerMessage(PacketDecreaseTimer message, EntityPlayer player) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    public PacketDecreaseTimer(int entityId) {
        this.entityId = entityId;
    }

    public PacketDecreaseTimer() {
    }
}