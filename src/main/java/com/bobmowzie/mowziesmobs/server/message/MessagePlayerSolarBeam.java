package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerSolarBeam {
    public MessagePlayerSolarBeam() {

    }

    public static void serialize(final MessagePlayerSolarBeam message, final FriendlyByteBuf buf) {
    }

    public static MessagePlayerSolarBeam deserialize(final FriendlyByteBuf buf) {
        final MessagePlayerSolarBeam message = new MessagePlayerSolarBeam();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerSolarBeam, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerSolarBeam message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                EntitySolarBeam solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM, player.world, player, player.getPosX(), player.getPosY() + 1.2f, player.getPosZ(), (float) ((player.getYRot()Head + 90) * Math.PI / 180), (float) (-player.getXRot() * Math.PI / 180), 55);
                solarBeam.setHasPlayer(true);
                player.world.addEntity(solarBeam);
                player.addPotionEffect(new MobEffectInstance(MobEffects.SLOWNESS, 80, 2, false, false));
                int duration = player.getActivePotionEffect(EffectHandler.SUNS_BLESSING).getDuration();
                player.removePotionEffect(EffectHandler.SUNS_BLESSING);
                int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get() * 60 * 20;
                if (duration - solarBeamCost > 0) {
                    player.addPotionEffect(new MobEffectInstance(EffectHandler.SUNS_BLESSING, duration - solarBeamCost, 0, false, false));
                }
            });
            context.setPacketHandled(true);
        }
    }
}
