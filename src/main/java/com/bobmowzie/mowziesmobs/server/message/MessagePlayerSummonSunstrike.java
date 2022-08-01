package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerSummonSunstrike {
    private static final double REACH = 15;

    public MessagePlayerSummonSunstrike() {

    }

    private static BlockHitResult rayTrace(LivingEntity entity, double reach) {
        Vec3 pos = entity.getEyePosition(0);
        Vec3 segment = entity.getLookAngle();
        segment = pos.add(segment.x * reach, segment.y * reach, segment.z * reach);
        return entity.level.clip(new ClipContext(pos, segment, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
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
                if (raytrace.getType() == HitResult.Type.BLOCK && raytrace.getDirection() == Direction.UP && player.getInventory().getSelected().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING)) {
                    BlockPos hit = raytrace.getBlockPos();
                    EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE.get(), player.level, player, hit.getX(), hit.getY(), hit.getZ());
                    sunstrike.onSummon();
                    player.level.addFreshEntity(sunstrike);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
