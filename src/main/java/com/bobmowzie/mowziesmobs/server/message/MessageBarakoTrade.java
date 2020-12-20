package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Josh on 11/14/2016.
 */
public class MessageBarakoTrade {
    private int entityID;

    public MessageBarakoTrade() {

    }

    public MessageBarakoTrade(LivingEntity sender) {
        entityID = sender.getEntityId();
    }

    public static void serialize(final MessageBarakoTrade message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageBarakoTrade deserialize(final PacketBuffer buf) {
        final MessageBarakoTrade message = new MessageBarakoTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageBarakoTrade, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageBarakoTrade message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayerEntity player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.world.getEntityByID(message.entityID);
                    if (!(entity instanceof EntityBarako)) {
                        return;
                    }
                    EntityBarako barako = (EntityBarako) entity;
                    if (barako.getCustomer() != player) {
                        return;
                    }
                    Container container = player.openContainer;
                    if (!(container instanceof ContainerBarakoTrade)) {
                        return;
                    }
                    boolean satisfied = barako.hasTradedWith(player);
                    if (!satisfied) {
                        if (satisfied = barako.fulfillDesire(container.getSlot(0))) {
                            barako.rememberTrade(player);
                            player.closeScreen();
                            container.detectAndSendChanges();
                        }
                    }
                    if (satisfied) {
                        player.addPotionEffect(new EffectInstance(PotionHandler.SUNS_BLESSING, 24000 * 3, 0, false, false));
                        if (barako.getAnimation() != barako.BLESS_ANIMATION) {
                            barako.setAnimationTick(0);
                            AnimationHandler.INSTANCE.sendAnimationMessage(barako, barako.BLESS_ANIMATION);
                            barako.playSound(MMSounds.ENTITY_BARAKO_BLESS.get(), 2, 1);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
