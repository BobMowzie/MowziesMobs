package com.bobmowzie.mowziesmobs.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;

public class MessagePlayerSummonSunstrike extends AbstractMessage<MessagePlayerSummonSunstrike> {
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike() {

    }

    private static RayTraceResult rayTrace(EntityLivingBase entity, double reach) {
        Vec3d pos = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3d segment = entity.getLookVec();
        segment = pos.addVector(segment.xCoord * reach, segment.yCoord * reach, segment.zCoord * reach);
        return entity.world.rayTraceBlocks(pos, segment, false, true, true);
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
        RayTraceResult raytrace = rayTrace(player, REACH);
        if (raytrace != null && raytrace.typeOfHit == RayTraceResult.Type.BLOCK && raytrace.sideHit == EnumFacing.UP && player.inventory.getCurrentItem() == ItemStack.EMPTY && player.isPotionActive(PotionHandler.INSTANCE.sunsBlessing)) {
            BlockPos hit = raytrace.getBlockPos();
            EntitySunstrike sunstrike = new EntitySunstrike(player.world, player, hit.getX(), hit.getY(), hit.getZ());
            sunstrike.onSummon();
            player.world.spawnEntity(sunstrike);
        }
    }
}
