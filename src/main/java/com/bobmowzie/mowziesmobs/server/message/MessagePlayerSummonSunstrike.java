package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlayerSummonSunstrike extends AbstractMessage<MessagePlayerSummonSunstrike> {
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike() {

    }

    private static RayTraceResult rayTrace(LivingEntity entity, double reach) {
        Vec3d pos = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        Vec3d segment = entity.getLookVec();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.world.rayTraceBlocks(pos, segment, false, true, true);
    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void onClientReceived(Minecraft client, MessagePlayerSummonSunstrike message, PlayerEntity player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePlayerSummonSunstrike message, PlayerEntity player, MessageContext messageContext) {
        RayTraceResult raytrace = rayTrace(player, REACH);
        if (raytrace != null && raytrace.typeOfHit == RayTraceResult.Type.BLOCK && raytrace.sideHit == Direction.UP && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(PotionHandler.SUNS_BLESSING)) {
            BlockPos hit = raytrace.getBlockPos();
            EntitySunstrike sunstrike = new EntitySunstrike(player.world, player, hit.getX(), hit.getY(), hit.getZ());
            sunstrike.onSummon();
            player.world.spawnEntity(sunstrike);
        }
    }
}
