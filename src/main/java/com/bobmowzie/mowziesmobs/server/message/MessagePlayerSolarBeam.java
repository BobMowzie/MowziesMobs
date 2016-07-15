package com.bobmowzie.mowziesmobs.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.bobmowzie.mowziesmobs.server.entity.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;

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
    public void onClientReceived(Minecraft client, MessagePlayerSolarBeam message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePlayerSolarBeam message, EntityPlayer player, MessageContext messageContext) {
        EntitySolarBeam solarBeam = new EntitySolarBeam(player.worldObj, player, player.posX, player.posY + 1.2f, player.posZ, (float) ((player.rotationYawHead + 90) * Math.PI / 180), (float) (-player.rotationPitch * Math.PI / 180), 55);
        solarBeam.setHasPlayer(true);
        player.worldObj.spawnEntityInWorld(solarBeam);
        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 80, 2, true));

        int duration = player.getActivePotionEffect(PotionHandler.INSTANCE.sunsBlessing).getDuration();
        player.removePotionEffect(PotionHandler.INSTANCE.sunsBlessing.getId());
        if (duration - 2400 > 0) {
            player.addPotionEffect(new PotionEffect(PotionHandler.INSTANCE.sunsBlessing.getId(), duration - 2400, 1, false));
        }
    }
}
