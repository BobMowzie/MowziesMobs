package com.bobmowzie.mowziesmobs.server.message.mouse;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.power.Power;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 5/25/2017.
 */
public class MessageRightMouseUp {
    public MessageRightMouseUp() {}

    public static void serialize(final MessageRightMouseUp message, final FriendlyByteBuf buf) {

    }

    public static MessageRightMouseUp deserialize(final FriendlyByteBuf buf) {
        final MessageRightMouseUp message = new MessageRightMouseUp();
        return message;
    }

    public static final class Handler implements BiConsumer<MessageRightMouseUp, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageRightMouseUp message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> this.accept(message, player));
            context.setPacketHandled(true);
        }

        private void accept(final MessageRightMouseUp message, final ServerPlayer player) {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
                if (capability != null) {
                    capability.setMouseRightDown(false);
                    Power[] powers = capability.getPowers();
                    for (int i = 0; i < powers.length; i++) {
                        powers[i].onRightMouseUp(player);
                    }
                }
                AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
                if (abilityCapability != null) {
                    for (Ability ability : abilityCapability.getAbilities()) {
                        if (ability instanceof PlayerAbility) {
                            ((PlayerAbility) ability).onRightMouseUp(player);
                        }
                    }
                }
            }
        }
    }
}