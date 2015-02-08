package thehippomaster.AnimationAPI.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketAnim implements IMessage {
	
	private byte animID;
	private int entityID;
	
	public PacketAnim() {
	}
	
	public PacketAnim(byte anim, int entity) {
		animID = anim;
		entityID = entity;
	}
	
	@Override
	public void toBytes( ByteBuf buffer) {
		buffer.writeByte(animID);
		buffer.writeInt(entityID);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		animID = buffer.readByte();
		entityID = buffer.readInt();
	}

	public static class Handler implements IMessageHandler<PacketAnim, IMessage> {
		@Override
		public IMessage onMessage(PacketAnim packet, MessageContext ctx) {
			World world = AnimationAPI.proxy.getWorldClient();
			IAnimatedEntity entity = (IAnimatedEntity)world.getEntityByID(packet.entityID);
			if(entity != null && packet.animID != -1) {
				entity.setAnimID(packet.animID);
				if(packet.animID == 0) entity.setAnimTick(0);
			}
			return null;
		}
	}
}
