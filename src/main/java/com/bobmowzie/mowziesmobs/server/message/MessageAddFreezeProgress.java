package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 5/31/2017.
 */
public class MessageAddFreezeProgress {
    private int entityID;
    private float amount;

    public MessageAddFreezeProgress() {

    }

    public MessageAddFreezeProgress(LivingEntity entity, float amount) {
        entityID = entity.getEntityId();
        this.amount = amount;
    }

    public static void serialize(final MessageAddFreezeProgress message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
        buf.writeFloat(message.amount);
    }

    public static MessageAddFreezeProgress deserialize(final PacketBuffer buf) {
        final MessageAddFreezeProgress message = new MessageAddFreezeProgress();
        message.entityID = buf.readVarInt();
        message.amount = buf.readFloat();
        return message;
    }

    public static class Handler implements BiConsumer<MessageAddFreezeProgress, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageAddFreezeProgress message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().world.getEntityByID(message.entityID);
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) entity;
                    FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(living, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
                    if (frozenCapability != null) {
                        frozenCapability.setFreezeProgress(frozenCapability.getFreezeProgress() + message.amount);
                        frozenCapability.setFreezeDecayDelay(FrozenCapability.MAX_FREEZE_DECAY_DELAY);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
