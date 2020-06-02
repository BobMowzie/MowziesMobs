package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerBarakoTrade;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
    public void onClientReceived(Minecraft minecraft, MessageBarakoTrade msg, EntityPlayer player, MessageContext ctx) {}

    @Override
    public void onServerReceived(MinecraftServer server, MessageBarakoTrade msg, EntityPlayer player, MessageContext ctx) {
        Entity entity = player.world.getEntityByID(msg.entityID);
        if (!(entity instanceof EntityBarako)) {
            return;
        }
        EntityBarako barako = (EntityBarako) entity;
        if (barako.getCustomer() != player) {
            return;
        }
        Container container = player.openContainer;
        if (!(container instanceof ContainerBarakoTrade)) {
            return;
        }
        boolean satisfied = barako.hasTradedWith(player);
        if (!satisfied) {
            if (satisfied = barako.fulfillDesire(container.getSlot(0))) {
                barako.rememberTrade(player);
                container.detectAndSendChanges();
            }
        }
        if (satisfied) {
            player.addPotionEffect(new PotionEffect(PotionHandler.SUNS_BLESSING, 24000 * 3, 0, false, false));
            if (barako.getAnimation() != barako.BLESS_ANIMATION) {
                barako.setAnimationTick(0);
                AnimationHandler.INSTANCE.sendAnimationMessage(barako, barako.BLESS_ANIMATION);
                barako.playSound(MMSounds.ENTITY_BARAKO_BLESS, 2, 1);
            }
        }
    }
}
