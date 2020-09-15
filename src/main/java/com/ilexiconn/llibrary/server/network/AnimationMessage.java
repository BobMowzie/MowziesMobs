package com.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimationMessage extends AbstractMessage<AnimationMessage> {
    private int entityID;
    private int index;

    public AnimationMessage() {

    }

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, AnimationMessage message, EntityPlayer player, MessageContext messageContext) {
        IAnimatedEntity entity = (IAnimatedEntity) player.world.getEntityByID(message.entityID);
        if (entity != null) {
            if (message.index == -1) {
                entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
            } else {
                entity.setAnimation(entity.getAnimations()[message.index]);
            }
            entity.setAnimationTick(0);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, AnimationMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.entityID = byteBuf.readInt();
        this.index = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.entityID);
        byteBuf.writeInt(this.index);
    }
}
