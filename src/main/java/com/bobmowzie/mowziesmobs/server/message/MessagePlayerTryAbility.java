package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerTryAbility {
    private int index;

    public MessagePlayerTryAbility() {

    }

    public MessagePlayerTryAbility(int index) {
        this.index = index;
    }

    public static void serialize(final MessagePlayerTryAbility message, final PacketBuffer buf) {
        buf.writeVarInt(message.index);
    }

    public static MessagePlayerTryAbility deserialize(final PacketBuffer buf) {
        final MessagePlayerTryAbility message = new MessagePlayerTryAbility();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerTryAbility, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerTryAbility message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(player, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
                if (abilityCapability != null) {
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, abilityCapability.getAbilities().get(message.index));
                }
            });
            context.setPacketHandled(true);
        }
    }
}
