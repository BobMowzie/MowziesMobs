package com.bobmowzie.mowziesmobs.common.message;

import com.bobmowzie.mowziesmobs.common.entity.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.common.potion.MMPotions;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class MessagePlayerSolarBeam extends AbstractMessage<MessagePlayerSolarBeam>
{
    public MessagePlayerSolarBeam()
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void handleClientMessage(MessagePlayerSolarBeam message, EntityPlayer player)
    {
    }

    @Override
    public void handleServerMessage(MessagePlayerSolarBeam message, EntityPlayer player)
    {
        EntitySolarBeam solarBeam = new EntitySolarBeam(player.worldObj, player, player.posX, player.posY + 1, player.posZ, (float) ((player.rotationYawHead + 90) * Math.PI/180), (float) (-player.rotationPitch * Math.PI/180), 55);
        solarBeam.setPlayer(true);
        player.worldObj.spawnEntityInWorld(solarBeam);
        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 80, 2, true));

        int duration = player.getActivePotionEffect(MMPotions.sunsBlessing).getDuration();
        player.removePotionEffect(MMPotions.sunsBlessing.getId());
        if (duration - 2400 > 0) player.addPotionEffect(new PotionEffect(MMPotions.sunsBlessing.getId(), duration - 2400, 1, false));
    }
}
