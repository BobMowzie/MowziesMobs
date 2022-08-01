package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
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
public class MessageBarakoTrade {
    private int entityID;

    public MessageBarakoTrade() {

    }

    public MessageBarakoTrade(LivingEntity sender) {
        entityID = sender.getId();
    }

    public static void serialize(final MessageBarakoTrade message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageBarakoTrade deserialize(final FriendlyByteBuf buf) {
        final MessageBarakoTrade message = new MessageBarakoTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageBarakoTrade, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageBarakoTrade message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level.getEntity(message.entityID);
                    if (!(entity instanceof EntityBarako)) {
                        return;
                    }
                    EntityBarako barako = (EntityBarako) entity;
                    if (barako.getCustomer() != player) {
                        return;
                    }
                    AbstractContainerMenu container = player.containerMenu;
                    if (!(container instanceof ContainerBarakoTrade)) {
                        return;
                    }
                    boolean satisfied = barako.hasTradedWith(player);
                    if (!satisfied) {
                        if (satisfied = barako.fulfillDesire(container.getSlot(0))) {
                            barako.rememberTrade(player);
                            ((ContainerBarakoTrade) container).returnItems();
                            container.broadcastChanges();
                        }
                    }
                    if (satisfied) {
                        player.addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get() * 60 * 20, 0, false, false));
                        if (barako.getAnimation() != EntityBarako.BLESS_ANIMATION) {
                            barako.setAnimationTick(0);
                            AnimationHandler.INSTANCE.sendAnimationMessage(barako, EntityBarako.BLESS_ANIMATION);
                            barako.playSound(MMSounds.ENTITY_BARAKO_BLESS.get(), 2, 1);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
