package com.ilexiconn.llibrary.server.network;

import com.bobmowzie.mowziesmobs.server.message.MessageUnfreezeEntity;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class AnimationMessage {
    private int entityID;
    private int index;

    public AnimationMessage() {

    }

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static void serialize(final AnimationMessage message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static AnimationMessage deserialize(final PacketBuffer buf) {
        final AnimationMessage message = new AnimationMessage();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<AnimationMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final AnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                IAnimatedEntity entity = (IAnimatedEntity) Minecraft.getInstance().world.getEntityByID(message.entityID);
                if (entity != null) {
                    if (message.index == -1) {
                        entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                    } else {
                        entity.setAnimation(entity.getAnimations()[message.index]);
                    }
                    entity.setAnimationTick(0);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
