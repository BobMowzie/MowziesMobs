package com.bobmowzie.mowziesmobs.common.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.common.potion.MMPotions;

public class MessagePlayerSummonSunstrike extends AbstractMessage<MessagePlayerSummonSunstrike>
{
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike()
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
    public void handleClientMessage(MessagePlayerSummonSunstrike message, EntityPlayer player)
    {
    }

    @Override
    public void handleServerMessage(MessagePlayerSummonSunstrike message, EntityPlayer player)
    {
        MovingObjectPosition raytrace = rayTrace(player, REACH);
        if (raytrace != null && raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && raytrace.sideHit == 1 && player.inventory.getCurrentItem() == null && player.isPotionActive(MMPotions.sunsBlessing))
        {
            EntitySunstrike sunstrike = new EntitySunstrike(player.worldObj, player, raytrace.blockX, raytrace.blockY, raytrace.blockZ);
            sunstrike.onSummon();
            player.worldObj.spawnEntityInWorld(sunstrike);
        }
    }

    private static MovingObjectPosition rayTrace(EntityLivingBase entity, double reach)
    {
        Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3 segment = entity.getLookVec();
        segment = pos.addVector(segment.xCoord * reach, segment.yCoord * reach, segment.zCoord * reach);
        return entity.worldObj.func_147447_a(pos, segment, false, true, true);
    }
}
