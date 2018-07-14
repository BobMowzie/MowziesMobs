package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.client.sound.BlackPinkSound;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class MessageBlackPinkInYourArea extends AbstractMessage<MessageBlackPinkInYourArea> {
    private int entityId;

    public MessageBlackPinkInYourArea() {}

    public MessageBlackPinkInYourArea(EntityMinecart minecart) {
        this(minecart.getEntityId());
    }

    private MessageBlackPinkInYourArea(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientReceived(Minecraft client, MessageBlackPinkInYourArea message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = client.world.getEntityByID(message.entityId);
        if (entity instanceof EntityMinecart) {
            client.getSoundHandler().playSound(new BlackPinkSound((EntityMinecart) entity));
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageBlackPinkInYourArea message, EntityPlayer player, MessageContext messageContext) {}
}
