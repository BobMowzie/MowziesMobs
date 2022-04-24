package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.util.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerSummonSunstrike {
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike() {

    }

    private static BlockHitResult rayTrace(LivingEntity entity, double reach) {
        Vec3 pos = entity.getEyePosition(0);
        Vec3 segment = entity.getLookVec();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.world.rayTraceBlocks(new RayTraceContext(pos, segment, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
    }

    public static void serialize(final MessagePlayerSummonSunstrike message, final FriendlyByteBuf buf) {
    }

    public static MessagePlayerSummonSunstrike deserialize(final FriendlyByteBuf buf) {
        final MessagePlayerSummonSunstrike message = new MessagePlayerSummonSunstrike();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerSummonSunstrike, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerSummonSunstrike message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                BlockHitResult raytrace = rayTrace(player, REACH);
                if (raytrace.getType() == HitResult.Type.BLOCK && raytrace.getFace() == Direction.UP && player.inventory.getCurrentItem().isEmpty() && player.isPotionActive(EffectHandler.SUNS_BLESSING)) {
                    BlockPos hit = raytrace.getPos();
                    EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, player.world, player, hit.x(), hit.y(), hit.z());
                    sunstrike.onSummon();
                    player.level.addFreshEntity(sunstrike);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
