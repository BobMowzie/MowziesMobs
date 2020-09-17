package com.ilexiconn.llibrary.server.network;

import io.netty.buffer.ByteBuf;
import com.ilexiconn.llibrary.server.capability.EntityDataHandler;
import com.ilexiconn.llibrary.server.capability.IEntityData;
import com.ilexiconn.llibrary.server.entity.EntityProperties;
import com.ilexiconn.llibrary.server.entity.PropertiesTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PropertiesMessage extends AbstractMessage<PropertiesMessage> {
    private String propertyID;
    private NBTTagCompound compound;
    private int entityID;

    public PropertiesMessage() {

    }

    public PropertiesMessage(PropertiesTracker<?> tracker, Entity entity) {
        this.propertyID = tracker.getProperties().getID();
        this.compound = tracker.getTrackingTag().getChangedCopy();
        this.entityID = entity.getEntityId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, PropertiesMessage message, EntityPlayer player, MessageContext messageContext) {
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
    public void onServerReceived(MinecraftServer server, PropertiesMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.propertyID = ByteBufUtils.readUTF8String(buf);
        this.compound = ByteBufUtils.readTag(buf);
        this.entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.propertyID);
        ByteBufUtils.writeTag(buf, this.compound);
        buf.writeInt(this.entityID);
    }
}
