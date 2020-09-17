package com.bobmowzie.mowziesmobs.server.message;

import io.netty.buffer.ByteBuf;
import com.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Josh on 10/28/2016.
 */
public class MessagePlayerAttackMob extends AbstractMessage<MessagePlayerAttackMob> {
    private int entityID;

    public MessagePlayerAttackMob() {

    }

    public MessagePlayerAttackMob(LivingEntity target) {
        entityID = target.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, MessagePlayerAttackMob messagePlayerAttackMob, PlayerEntity entityPlayer, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, MessagePlayerAttackMob messagePlayerAttackMob, PlayerEntity entityPlayer, MessageContext messageContext) {
        Entity entity = entityPlayer.world.getEntityByID(messagePlayerAttackMob.entityID);
        if (entity != null) entityPlayer.attackTargetEntityWithCurrentItem(entity);
    }
}
