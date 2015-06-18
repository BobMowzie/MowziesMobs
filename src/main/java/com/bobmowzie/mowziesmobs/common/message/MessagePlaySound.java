package com.bobmowzie.mowziesmobs.common.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;

public class MessagePlaySound extends AbstractMessage<MessagePlaySound>
{
    public int entityId;
    public String soundName;
    public float volume;
    public float pitch;

    public MessagePlaySound()
    {

    }

    public MessagePlaySound(int id, String s, float v, float p)
    {
        entityId = id;
        soundName = s;
        volume = v;
        pitch = p;
    }

    public void handleClientMessage(MessagePlaySound message, EntityPlayer player)
    {

    }

    public void handleServerMessage(MessagePlaySound message, EntityPlayer player)
    {
        if (player.worldObj.getEntityByID(message.entityId) != null)
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
