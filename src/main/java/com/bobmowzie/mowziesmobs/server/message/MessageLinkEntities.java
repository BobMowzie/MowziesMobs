package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 10/28/2016.
 */
public class MessageLinkEntities {
    private int sourceID;
    private int targetID;

    public MessageLinkEntities() {

    }

    public MessageLinkEntities(Entity source, Entity target) {
        if (source instanceof ILinkedEntity) {
            sourceID = source.getId();
            targetID = target.getId();
        }
    }

    public static void serialize(final MessageLinkEntities message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.sourceID);
        buf.writeVarInt(message.targetID);
    }

    public static MessageLinkEntities deserialize(final FriendlyByteBuf buf) {
        final MessageLinkEntities message = new MessageLinkEntities();
        message.sourceID = buf.readVarInt();
        message.targetID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageLinkEntities, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageLinkEntities message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level != null) {
                    Entity entitySource = Minecraft.getInstance().level.getEntity(message.sourceID);
                    Entity entityTarget = Minecraft.getInstance().level.getEntity(message.targetID);
                    if (entitySource instanceof ILinkedEntity && entityTarget != null) {
                        ((ILinkedEntity) entitySource).link(entityTarget);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
