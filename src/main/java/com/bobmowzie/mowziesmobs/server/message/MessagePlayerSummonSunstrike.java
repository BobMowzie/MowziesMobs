package com.bobmowzie.mowziesmobs.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;

public class MessagePlayerSummonSunstrike extends AbstractMessage<MessagePlayerSummonSunstrike> {
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike() {

    }

    private static MovingObjectPosition rayTrace(EntityLivingBase entity, double reach) {
        Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 segment = entity.getLookVec();
        segment = pos.addVector(segment.xCoord * reach, segment.yCoord * reach, segment.zCoord * reach);
        return entity.worldObj.func_147447_a(pos, segment, false, true, true);
    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void onClientReceived(Minecraft client, MessagePlayerSummonSunstrike message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePlayerSummonSunstrike message, EntityPlayer player, MessageContext messageContext) {
        MovingObjectPosition raytrace = rayTrace(player, REACH);
        if (raytrace != null && raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && raytrace.sideHit == 1 && player.inventory.getCurrentItem() == null && player.isPotionActive(PotionHandler.INSTANCE.sunsBlessing)) {
            EntitySunstrike sunstrike = new EntitySunstrike(player.worldObj, player, raytrace.blockX, raytrace.blockY, raytrace.blockZ);
            sunstrike.onSummon();
            player.worldObj.spawnEntityInWorld(sunstrike);
        }
    }
}
