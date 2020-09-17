package com.ilexiconn.llibrary.server;

import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.server.capability.EntityDataCapabilityImplementation;
import com.ilexiconn.llibrary.server.capability.EntityDataCapabilityStorage;
import com.ilexiconn.llibrary.server.capability.IEntityDataCapability;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import com.ilexiconn.llibrary.server.network.AbstractMessage;
import com.ilexiconn.llibrary.server.network.SnackbarMessage;
import com.ilexiconn.llibrary.server.snackbar.Snackbar;
import com.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy {
    public void onPreInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(EntityPropertiesHandler.INSTANCE);
        CapabilityManager.INSTANCE.register(IEntityDataCapability.class, new EntityDataCapabilityStorage(), EntityDataCapabilityImplementation.class);
    }

    public void onInit() {
    }

    public void onPostInit() {
        if (LLibrary.CONFIG.hasVersionCheck()) {
            UpdateHandler.INSTANCE.searchForUpdates();
        }
    }

    public <T extends AbstractMessage<T>> void handleMessage(final T message, final MessageContext messageContext) {
        WorldServer world = (WorldServer) messageContext.getServerHandler().player.world;
        world.addScheduledTask(() -> message.onServerReceived(FMLCommonHandler.instance().getMinecraftServerInstance(), message, messageContext.getServerHandler().player, messageContext));
    }

    public float getPartialTicks() {
        return 0.0F;
    }

    public void showSnackbar(Snackbar snackbar) {
        LLibrary.NETWORK_WRAPPER.sendToAll(new SnackbarMessage(snackbar));
    }

    public void setTPS(float tickRate) {
    }
}
