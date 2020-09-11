package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlayerSolarBeam extends AbstractMessage<MessagePlayerSolarBeam> {
    public MessagePlayerSolarBeam() {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void onClientReceived(Minecraft client, MessagePlayerSolarBeam message, PlayerEntity player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePlayerSolarBeam message, PlayerEntity player, MessageContext messageContext) {
        EntitySolarBeam solarBeam = new EntitySolarBeam(player.world, player, player.posX, player.posY + 1.2f, player.posZ, (float) ((player.rotationYawHead + 90) * Math.PI / 180), (float) (-player.rotationPitch * Math.PI / 180), 55);
        solarBeam.setHasPlayer(true);
        player.world.spawnEntity(solarBeam);
        player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 80, 2, false, false));
        int duration = player.getActivePotionEffect(PotionHandler.SUNS_BLESSING).getDuration();
        player.removePotionEffect(PotionHandler.SUNS_BLESSING);
        if (duration - 2400 > 0) {
            player.addPotionEffect(new EffectInstance(PotionHandler.SUNS_BLESSING, duration - 2400, 0, false, false));
        }
    }
}
