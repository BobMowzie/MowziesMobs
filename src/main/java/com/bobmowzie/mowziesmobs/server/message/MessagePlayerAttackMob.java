package com.bobmowzie.mowziesmobs.server.message;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
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
        entityID = target.getId();
    }

    public static void serialize(final MessagePlayerAttackMob message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessagePlayerAttackMob deserialize(final FriendlyByteBuf buf) {
        final MessagePlayerAttackMob message = new MessagePlayerAttackMob();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerAttackMob, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerAttackMob message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level.getEntity(message.entityID);
                    if (entity != null) player.attack(entity);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
