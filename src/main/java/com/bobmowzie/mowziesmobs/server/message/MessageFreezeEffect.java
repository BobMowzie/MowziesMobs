package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public class MessageFreezeEffect {
    private int entityID;
    private boolean isFrozen;

    public MessageFreezeEffect() {

    }

    public MessageFreezeEffect(LivingEntity entity, boolean activate) {
        entityID = entity.getId();
        this.isFrozen = activate;
    }

    public static void serialize(final MessageFreezeEffect message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.isFrozen);
    }

    public static MessageFreezeEffect deserialize(final FriendlyByteBuf buf) {
        final MessageFreezeEffect message = new MessageFreezeEffect();
        message.entityID = buf.readVarInt();
        message.isFrozen = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<MessageFreezeEffect, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageFreezeEffect message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) entity;
                        FrozenCapability.IFrozenCapability livingCapability = CapabilityHandler.getCapability(living, CapabilityHandler.FROZEN_CAPABILITY);
                        if (livingCapability != null) {
                            if (message.isFrozen) livingCapability.onFreeze(living);
                            else livingCapability.onUnfreeze(living);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
