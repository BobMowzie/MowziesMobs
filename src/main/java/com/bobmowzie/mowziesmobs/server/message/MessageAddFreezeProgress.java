package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import io.netty.buffer.ByteBuf;
import com.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageAddFreezeProgress extends AbstractMessage<MessageAddFreezeProgress> {
    private int entityID;
    private float amount;

    public MessageAddFreezeProgress() {

    }

    public MessageAddFreezeProgress(LivingEntity entity, float amount) {
        entityID = entity.getEntityId();
        this.amount = amount;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeFloat(amount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
        amount = buf.readFloat();
    }

    @Override
    public void onClientReceived(Minecraft client, MessageAddFreezeProgress message, PlayerEntity player, NetworkEvent.Context messageContext) {
        Entity entity = player.world.getEntityByID(message.entityID);
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(living, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
            frozenCapability.setFreezeProgress(frozenCapability.getFreezeProgress() + amount);
            frozenCapability.setFreezeDecayDelay(FrozenCapability.MAX_FREEZE_DECAY_DELAY);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageAddFreezeProgress message, PlayerEntity player, NetworkEvent.Context messageContext) {

    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {

    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {

    }

    @Override
    public void processPacket(INetHandler handler) {

    }
}
