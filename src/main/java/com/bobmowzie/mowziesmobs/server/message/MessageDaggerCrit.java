package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Josh on 5/31/2017.
 */
public class MessageDaggerCrit {
    private int targetID;
    private int attackerID;

    public MessageDaggerCrit() {

    }

    public MessageDaggerCrit(LivingEntity target, LivingEntity attacker) {
        targetID = target.getEntityId();
        attackerID = attacker.getEntityId();
    }


    public static void serialize(final MessageDaggerCrit message, final PacketBuffer buf) {
        buf.writeVarInt(message.targetID);
        buf.writeVarInt(message.attackerID);
    }

    public static MessageDaggerCrit deserialize(final PacketBuffer buf) {
        final MessageDaggerCrit message = new MessageDaggerCrit();
        message.targetID = buf.readVarInt();
        message.attackerID = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageDaggerCrit, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageDaggerCrit message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Entity target = Minecraft.getInstance().world.getEntityByID(message.targetID);
                Entity attacker = Minecraft.getInstance().world.getEntityByID(message.attackerID);
                if (target != null && attacker != null) {
                    Vec3d ringOffset = attacker.getLookVec().scale(-target.getWidth()/2.f);
                    ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                    Vec3d pos = target.getPositionVec().add(0, target.getHeight() / 2f, 0).add(ringOffset);
                    AdvancedParticleBase.spawnParticle(target.world, ParticleHandler.RING_SPARKS.get(), pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{1f, 1f, 0f}, new float[]{0f, 0.5f, 1f}), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 15f), false)
                    });
                    Random rand = attacker.world.getRandom();
                    float explodeSpeed = 2.5f;
                    for (int i = 0; i < 10; i++) {
                        Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                        particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                        particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 8f + 15f;
                        ParticleVanillaCloudExtended.spawnVanillaCloud(target.world, pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                    }
                    for (int i = 0; i < 10; i++) {
                        Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                        particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                        particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 2.5f + 5f;
                        AdvancedParticleBase.spawnParticle(target.world, ParticleHandler.PIXEL.get(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false);
                    }
                    for (int i = 0; i < 6; i++) {
                        Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                        particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                        particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 5f + 10f;
                        AdvancedParticleBase.spawnParticle(target.world, ParticleHandler.BUBBLE.get(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
