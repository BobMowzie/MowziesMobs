package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
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
public class MessageSunblockEffect {
    private int entityID;
    private boolean hasSunblock;

    public MessageSunblockEffect() {

    }

    public MessageSunblockEffect(LivingEntity entity, boolean activate) {
        entityID = entity.getId();
        this.hasSunblock = activate;
    }

    public static void serialize(final MessageSunblockEffect message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.hasSunblock);
    }

    public static MessageSunblockEffect deserialize(final FriendlyByteBuf buf) {
        final MessageSunblockEffect message = new MessageSunblockEffect();
        message.entityID = buf.readVarInt();
        message.hasSunblock = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<MessageSunblockEffect, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageSunblockEffect message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) entity;
                        LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(living, LivingCapability.LivingProvider.LIVING_CAPABILITY);
                        if (livingCapability != null) {
                            livingCapability.setHasSunblock(message.hasSunblock);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
