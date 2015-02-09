package com.bobmowzie.mowziesmobs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPlaySound extends AbstractPacket<PacketPlaySound>
{
    public int entityId;
    public String soundName;
    public float volume;
    public float pitch;

    public PacketPlaySound()
    {

    }

    public PacketPlaySound(int id, String s)
    {
        this(id, s, 1, 1);
    }

    public PacketPlaySound(int id, String s, float v, float p)
    {
        entityId = id;
        soundName = s;
        volume = v;
        pitch = p;
    }

    public void handleClientMessage(PacketPlaySound message, EntityPlayer player)
    {

    }

    public void handleServerMessage(PacketPlaySound message, EntityPlayer player)
    {
        player.worldObj.playSoundAtEntity(player.worldObj.getEntityByID(message.entityId), message.soundName, message.volume, message.pitch);
    }

    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        soundName = ByteBufUtils.readUTF8String(buf);
        volume = buf.readFloat();
        pitch = buf.readFloat();
    }

    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, soundName);
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
    }
}
