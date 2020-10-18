package com.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import com.ilexiconn.llibrary.server.capability.EntityDataHandler;
import com.ilexiconn.llibrary.server.capability.IEntityData;
import com.ilexiconn.llibrary.server.entity.EntityProperties;
import com.ilexiconn.llibrary.server.entity.PropertiesTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;

public class PropertiesMessage extends AbstractMessage<PropertiesMessage> {
    private String propertyID;
    private CompoundNBT compound;
    private int entityID;

    public PropertiesMessage() {

    }

    public PropertiesMessage(PropertiesTracker<?> tracker, Entity entity) {
        this.propertyID = tracker.getProperties().getID();
        this.compound = tracker.getTrackingTag().getChangedCopy();
        this.entityID = entity.getEntityId();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, PropertiesMessage message, PlayerEntity player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.entityID);
        if (entity != null) {
            IEntityData<?> extendedProperties = EntityDataHandler.INSTANCE.getEntityData(entity, message.propertyID);
            if (extendedProperties instanceof EntityProperties) {
                EntityProperties<?> properties = (EntityProperties<?>) extendedProperties;
                properties.loadTrackingSensitiveData(message.compound);
                properties.onSync();
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, PropertiesMessage message, PlayerEntity player, MessageContext messageContext) {

    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.propertyID = ByteBufUtils.readUTF8String(buf);
        this.compound = ByteBufUtils.readTag(buf);
        this.entityID = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        ByteBufUtils.writeUTF8String(buf, this.propertyID);
        ByteBufUtils.writeTag(buf, this.compound);
        buf.writeInt(this.entityID);
    }
}
