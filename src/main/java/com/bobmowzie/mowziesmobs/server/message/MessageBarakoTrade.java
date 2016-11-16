package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Josh on 11/14/2016.
 */
public class MessageBarakoTrade extends AbstractMessage<MessageBarakoTrade> {
    private int entityID;

    public MessageBarakoTrade() {

    }

    public MessageBarakoTrade(EntityLivingBase sender) {
        entityID = sender.getEntityId();
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
    public void onClientReceived(Minecraft minecraft, MessageBarakoTrade messageBarakoTrade, EntityPlayer entityPlayer, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer minecraftServer, MessageBarakoTrade messageBarakoTrade, EntityPlayer entityPlayer, MessageContext messageContext) {
        Entity entity = entityPlayer.worldObj.getEntityByID(messageBarakoTrade.entityID);
        if (entity instanceof EntityBarako) {
            EntityBarako barako = (EntityBarako)entity;
            barako.getCustomer().addPotionEffect(new PotionEffect(PotionHandler.INSTANCE.sunsBlessing, 24000, 0, false, false));

            UUID uuid = EntityPlayer.getUUID(barako.getCustomer().getGameProfile());
            if (!barako.tradedPlayers.contains(uuid)) {
                barako.tradedPlayers.add(uuid);
                Container container = entityPlayer.openContainer;
                if (container instanceof ContainerBarakoTrade) {
                    ContainerBarakoTrade baraContainer = (ContainerBarakoTrade) container;
                    ItemStack input = baraContainer.getInventoryBarako().getStackInSlot(0);
                    input.stackSize -= barako.getDesires().stackSize;
                    if (input.stackSize <= 0) {
                        input = null;
                    }
                    baraContainer.getInventoryBarako().setInventorySlotContents(0, input);
                }
            }
        }
    }
}
