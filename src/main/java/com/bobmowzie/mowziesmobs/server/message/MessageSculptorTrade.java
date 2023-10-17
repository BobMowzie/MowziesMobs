package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 11/14/2016.
 */
public class MessageSculptorTrade {
    private int entityID;

    public MessageSculptorTrade() {

    }

    public MessageSculptorTrade(LivingEntity sender) {
        entityID = sender.getId();
    }

    public static void serialize(final MessageSculptorTrade message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageSculptorTrade deserialize(final FriendlyByteBuf buf) {
        final MessageSculptorTrade message = new MessageSculptorTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageSculptorTrade, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageSculptorTrade message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level.getEntity(message.entityID);
                    if (!(entity instanceof EntitySculptor)) {
                        return;
                    }
                    EntitySculptor sculptor = (EntitySculptor) entity;
                    if (sculptor.getCustomer() != player) {
                        return;
                    }
                    AbstractContainerMenu container = player.containerMenu;
                    if (!(container instanceof ContainerSculptorTrade)) {
                        return;
                    }
                    boolean satisfied = sculptor.fulfillDesire(container.getSlot(0));
                    if (satisfied) {
                        ((ContainerSculptorTrade) container).returnItems();
                        container.broadcastChanges();
                        sculptor.sendAbilityMessage(EntitySculptor.START_TEST);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
