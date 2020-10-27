package com.ilexiconn.llibrary.server.network;

import com.bobmowzie.mowziesmobs.server.message.MessageUnfreezeEntity;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class AnimationMessage implements IPacket {
    private int entityID;
    private int index;

    public AnimationMessage() {

    }

    public AnimationMessage(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityID = buf.readInt();
        this.index = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeInt(this.entityID);
        buf.writeInt(this.index);
    }

    @Override
    public void processPacket(INetHandler handler) {
        IAnimatedEntity entity = (IAnimatedEntity) Minecraft.getInstance().world.getEntityByID(entityID);
        if (entity != null) {
            if (index == -1) {
                entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
            } else {
                entity.setAnimation(entity.getAnimations()[index]);
            }
            entity.setAnimationTick(0);
        }
    }
}
