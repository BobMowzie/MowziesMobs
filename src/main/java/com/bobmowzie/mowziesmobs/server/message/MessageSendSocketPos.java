package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageSendSocketPos extends AbstractMessage<MessageSendSocketPos> {
	private int entityId;
	private int index;
	private Vec3d pos;

	public MessageSendSocketPos() {

	}

	public MessageSendSocketPos(MowzieEntity entity, int index, Vec3d pos) {
		entityId = entity.getEntityId();
		this.index = index;
		this.pos = pos;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeInt(index);
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityId = buf.readInt();
		index = buf.readInt();
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		pos = new Vec3d(x, y, z);
	}

	@Override
	public void onClientReceived(Minecraft client, MessageSendSocketPos message, EntityPlayer player, MessageContext messageContext) {

	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageSendSocketPos message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID(message.entityId);
		if (entity instanceof MowzieEntity) {
			((MowzieEntity) entity).socketPosArray[message.index] = message.pos;
		}
	}
}
