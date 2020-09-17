package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.client.sound.BlackPinkSound;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import io.netty.buffer.ByteBuf;
import com.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class MessageBlackPinkInYourArea extends AbstractMessage<MessageBlackPinkInYourArea> {
    private int entityId;

    public MessageBlackPinkInYourArea() {}

    public MessageBlackPinkInYourArea(AbstractMinecartEntity minecart) {
        this(minecart.getEntityId());
    }

    private MessageBlackPinkInYourArea(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientReceived(Minecraft client, MessageBlackPinkInYourArea message, PlayerEntity player, MessageContext messageContext) {
        World world = client.world;
        Entity entity = world.getEntityByID(message.entityId);
        if (entity instanceof AbstractMinecartEntity) {
            AbstractMinecartEntity minecart = (AbstractMinecartEntity) entity;
            client.getSoundHandler().playSound(new BlackPinkSound(minecart));
            BlockState state = BlockHandler.GROTTOL.getDefaultState()
                .withProperty(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK);
            BlockPos pos = new BlockPos(minecart);
            final float scale = 0.75F;
            double x = minecart.posX,
                y = minecart.posY + 0.375F + 0.5F + (minecart.getDefaultDisplayTileOffset() - 8) / 16.0F * scale,
                z = minecart.posZ;
            SoundType sound = state.getBlock().getSoundType(state, world, pos, minecart);
            world.playSound(
                x, y, z,
                sound.getBreakSound(),
                minecart.getSoundCategory(),
                (sound.getVolume() + 1.0F) / 2.0F,
                sound.getPitch() * 0.8F,
                false
            );
            final int size = 3;
            float offset =  -0.5F * scale;
            for (int ix = 0; ix < size; ix++) {
                for (int iy = 0; iy < size; iy++) {
                    for (int iz = 0; iz < size; iz++) {
                        double dx = (double) ix / size * scale;
                        double dy = (double) iy / size * scale;
                        double dz = (double) iz / size * scale;
                        client.effectRenderer.addEffect(new DiggingParticle(
                            world,
                            x + dx + offset, y + dy + offset, z + dz + offset,
                            dx + minecart.motionX, dy + minecart.motionY, dz + minecart.motionZ,
                            state
                        ) {}.setBlockPos(pos));
                    }
                }
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageBlackPinkInYourArea message, PlayerEntity player, MessageContext messageContext) {}
}
