package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MessageBlackPinkInYourArea {
    private int entityID;

    public MessageBlackPinkInYourArea() {}

    public MessageBlackPinkInYourArea(AbstractMinecartEntity minecart) {
        this(minecart.getEntityId());
    }

    private MessageBlackPinkInYourArea(int entityId) {
        this.entityID = entityId;
    }

    public static void serialize(final MessageBlackPinkInYourArea message, final PacketBuffer buf) {
        buf.writeVarInt(message.entityID);
    }

    public static MessageBlackPinkInYourArea deserialize(final PacketBuffer buf) {
        final MessageBlackPinkInYourArea message = new MessageBlackPinkInYourArea();
        message.entityID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageBlackPinkInYourArea, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageBlackPinkInYourArea message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ClientWorld world = Minecraft.getInstance().world;
                Entity entity = world.getEntityByID(message.entityID);
                if (entity instanceof AbstractMinecartEntity) {
                    AbstractMinecartEntity minecart = (AbstractMinecartEntity) entity;
                    MowziesMobs.PROXY.playBlackPinkSound(minecart);
                    BlockState state = Blocks.STONE.getDefaultState()
                            .with(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK);
                    BlockPos pos = minecart.getPosition();
                    final float scale = 0.75F;
                    double x = minecart.getPosX(),
                            y = minecart.getPosY() + 0.375F + 0.5F + (minecart.getDefaultDisplayTileOffset() - 8) / 16.0F * scale,
                            z = minecart.getPosZ();
                    SoundType sound = state.getBlock().getSoundType(state, world, pos, minecart);
                    world.playSound(
                            x, y, z,
                            sound.getBreakSound(),
                            minecart.getSoundCategory(),
                            (sound.getVolume() + 1.0F) / 2.0F,
                            sound.getPitch() * 0.8F,
                            false
                    );
                    MowziesMobs.PROXY.minecartParticles(world, minecart, scale, x, y, z, state, pos);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
