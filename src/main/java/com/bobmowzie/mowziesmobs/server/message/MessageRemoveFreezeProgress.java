package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public class MessageRemoveFreezeProgress {
    private int entityID;

    public MessageRemoveFreezeProgress() {

    }

    public MessageRemoveFreezeProgress(LivingEntity entity) {
        entityID = entity.getEntityId();
    }


    public static void serialize(final MessageRemoveFreezeProgress message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageRemoveFreezeProgress deserialize(final FriendlyByteBuf buf) {
        final MessageRemoveFreezeProgress message = new MessageRemoveFreezeProgress();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageRemoveFreezeProgress, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageRemoveFreezeProgress message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().world.getEntityByID(message.entityID);
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) entity;
                    living.removeActivePotionEffect(EffectHandler.FROZEN);
                    FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(living, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
                    if (frozenCapability != null) {
                        frozenCapability.setFreezeProgress(0);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
