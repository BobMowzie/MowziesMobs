package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
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
public class MessageUmvuthiTrade {
    private int entityID;

    public MessageUmvuthiTrade() {

    }

    public MessageUmvuthiTrade(LivingEntity sender) {
        entityID = sender.getId();
    }

    public static void serialize(final MessageUmvuthiTrade message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageUmvuthiTrade deserialize(final FriendlyByteBuf buf) {
        final MessageUmvuthiTrade message = new MessageUmvuthiTrade();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUmvuthiTrade, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUmvuthiTrade message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                if (player != null) {
                    Entity entity = player.level.getEntity(message.entityID);
                    if (!(entity instanceof EntityUmvuthi)) {
                        return;
                    }
                    EntityUmvuthi barako = (EntityUmvuthi) entity;
                    if (barako.getCustomer() != player) {
                        return;
                    }
                    AbstractContainerMenu container = player.containerMenu;
                    if (!(container instanceof ContainerUmvuthiTrade)) {
                        return;
                    }
                    boolean satisfied = barako.hasTradedWith(player);
                    if (!satisfied) {
                        if (satisfied = barako.fulfillDesire(container.getSlot(0))) {
                            barako.rememberTrade(player);
                            ((ContainerUmvuthiTrade) container).returnItems();
                            container.broadcastChanges();
                        }
                    }
                    if (satisfied) {
                        player.addEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING.get(), ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration.get() * 60 * 20, 0, false, false));
                        if (barako.getActiveAbilityType() != EntityUmvuthi.BLESS_ABILITY) {
//                            barako.setAnimationTick(0); TODO
                            barako.sendAbilityMessage(EntityUmvuthi.BLESS_ABILITY);
                            barako.playSound(MMSounds.ENTITY_UMVUTHI_BLESS.get(), 2, 1);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
