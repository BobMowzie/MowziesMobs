package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageUpdateBossBar {
    private UUID bossID;
    private ResourceLocation barTexture;
    private ResourceLocation overlayTexture;

    public MessageUpdateBossBar() {

    }

    public MessageUpdateBossBar(UUID bossID, ResourceLocation barTexture, ResourceLocation overlayTexture) {
        this.bossID = bossID;
        this.barTexture = barTexture;
        this.overlayTexture = overlayTexture;
    }

    public static void serialize(final MessageUpdateBossBar message, final FriendlyByteBuf buf) {
        buf.writeUUID(message.bossID);
        if (message.barTexture != null) buf.writeResourceLocation(message.barTexture);
        if (message.overlayTexture != null) buf.writeResourceLocation(message.overlayTexture);
    }

    public static MessageUpdateBossBar deserialize(final FriendlyByteBuf buf) {
        final MessageUpdateBossBar message = new MessageUpdateBossBar();
        message.bossID = buf.readUUID();
        message.barTexture = buf.readResourceLocation();
        message.overlayTexture = buf.readResourceLocation();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUpdateBossBar, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUpdateBossBar message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (message.barTexture == null) {
                    ClientProxy.bossBarResourceLocations.remove(message.bossID);
                }
                else {
                    ClientProxy.bossBarResourceLocations.put(message.bossID, Pair.of(message.barTexture, message.overlayTexture));
                }
            });
            context.setPacketHandled(true);
        }
    }
}
