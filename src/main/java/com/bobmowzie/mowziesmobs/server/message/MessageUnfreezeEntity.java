package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageUnfreezeEntity {
    private int entityID;

    public MessageUnfreezeEntity() {

    }

    public MessageUnfreezeEntity(LivingEntity entity) {
        entityID = entity.getEntityId();
    }


    public static void serialize(final MessageUnfreezeEntity message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageUnfreezeEntity deserialize(final PacketBuffer buf) {
        final MessageUnfreezeEntity message = new MessageUnfreezeEntity();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUnfreezeEntity, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUnfreezeEntity message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().world.getEntityByID(message.entityID);
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) entity;
                    living.removeActivePotionEffect(PotionHandler.FROZEN);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
