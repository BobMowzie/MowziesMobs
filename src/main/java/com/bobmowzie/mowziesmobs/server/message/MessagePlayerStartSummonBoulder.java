package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.power.PowerGeomancy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerStartSummonBoulder {

    public MessagePlayerStartSummonBoulder() {

    }

    public static void serialize(final MessagePlayerStartSummonBoulder message, final FriendlyByteBuf buf) {
    }

    public static MessagePlayerStartSummonBoulder deserialize(final FriendlyByteBuf buf) {
        final MessagePlayerStartSummonBoulder message = new MessagePlayerStartSummonBoulder();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerStartSummonBoulder, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerStartSummonBoulder message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
                    if (playerCapability != null) {
                        PowerGeomancy geomancy = playerCapability.getGeomancy();
                        geomancy.startSpawningBoulder(player);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
