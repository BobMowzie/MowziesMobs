package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 10/28/2016.
 */
public class MessagePlayerAttackMob {
    private int entityID;

    public MessagePlayerAttackMob() {

    }

    public MessagePlayerAttackMob(LivingEntity target) {
        entityID = target.getEntityId();
    }

    public static void serialize(final MessagePlayerAttackMob message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessagePlayerAttackMob deserialize(final PacketBuffer buf) {
        final MessagePlayerAttackMob message = new MessagePlayerAttackMob();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerAttackMob, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerAttackMob message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.world.getEntityByID(message.entityID);
                    if (entity != null) player.attackTargetEntityWithCurrentItem(entity);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
