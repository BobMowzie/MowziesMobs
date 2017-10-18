package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageUnfreezeEntity extends AbstractMessage<MessageUnfreezeEntity> {
	private int entityID;

	public MessageUnfreezeEntity() {

	}

	public MessageUnfreezeEntity(EntityLivingBase entity) {
		entityID = entity.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
	}

	@Override
	public void onClientReceived(Minecraft client, MessageUnfreezeEntity message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID(message.entityID);
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) entity;
			MowzieLivingProperties livingProperties = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
			living.removeActivePotionEffect(PotionHandler.INSTANCE.frozen);
			living.dismountEntity(livingProperties.frozenController);
		}
	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageUnfreezeEntity message, EntityPlayer player, MessageContext messageContext) {

	}
}
