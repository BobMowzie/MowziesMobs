package com.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import com.ilexiconn.llibrary.server.entity.block.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEntityMessage extends AbstractMessage<BlockEntityMessage> {
    private BlockPos pos;
    private NBTTagCompound compound;

    public BlockEntityMessage() {

    }

    public BlockEntityMessage(BlockEntity entity) {
        this.pos = entity.getPos();
        this.compound = new NBTTagCompound();
        entity.saveTrackingSensitiveData(this.compound);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, BlockEntityMessage message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = message.pos;
        if (player.world.isBlockLoaded(pos)) {
            BlockEntity blockEntity = (BlockEntity) player.world.getTileEntity(pos);
            blockEntity.loadTrackingSensitiveData(message.compound);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, BlockEntityMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.compound);
    }

    @Override
    public boolean registerOnSide(Side side) {
        return side.isClient();
    }
}
