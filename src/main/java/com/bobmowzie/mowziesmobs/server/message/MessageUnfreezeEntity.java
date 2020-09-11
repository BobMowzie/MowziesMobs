package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageUnfreezeEntity extends AbstractMessage<MessageUnfreezeEntity> {
    private int entityID;

    public MessageUnfreezeEntity() {

    }

    public MessageUnfreezeEntity(LivingEntity entity) {
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
    public void onClientReceived(Minecraft client, MessageUnfreezeEntity message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.entityID);
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            living.removeActivePotionEffect(PotionHandler.FROZEN);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUnfreezeEntity message, PlayerEntity player, MessageContext messageContext) {

    }
}
