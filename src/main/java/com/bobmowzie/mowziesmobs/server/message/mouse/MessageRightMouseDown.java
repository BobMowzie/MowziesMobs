package com.bobmowzie.mowziesmobs.server.message.mouse;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.power.Power;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Josh on 5/25/2017.
 */
public class MessageRightMouseDown {
    public MessageRightMouseDown() {}

    public static void serialize(final MessageRightMouseDown message, final PacketBuffer buf) {

    }

    public static MessageRightMouseDown deserialize(final PacketBuffer buf) {
        final MessageRightMouseDown message = new MessageRightMouseDown();
        return message;
    }

    public static final class Handler implements BiConsumer<MessageRightMouseDown, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageRightMouseDown message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> this.accept(message, player));
            context.setPacketHandled(true);
        }

        private void accept(final MessageRightMouseDown message, final ServerPlayerEntity player) {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
                capability.setMouseRightDown(true);
                Power[] powers = capability.getPowers();
                for (int i = 0; i < powers.length; i++) {
                    powers[i].onRightMouseDown(player);
                }
            }
        }
    }
}