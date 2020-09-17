package com.ilexiconn.llibrary.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.client.gui.SnackbarGUI;
import com.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTab;
import com.ilexiconn.llibrary.client.gui.survivaltab.SurvivalTabHandler;
import com.ilexiconn.llibrary.client.lang.LanguageHandler;
import com.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import com.ilexiconn.llibrary.client.render.entity.PartRenderer;
import com.ilexiconn.llibrary.server.ServerProxy;
import com.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import com.ilexiconn.llibrary.server.network.AbstractMessage;
import com.ilexiconn.llibrary.server.snackbar.Snackbar;
import com.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.Timer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();
    public static final int UPDATE_BUTTON_ID = "UPDATE_BUTTON_ID".hashCode();
    public static final List<SnackbarGUI> SNACKBAR_LIST = new ArrayList<>();
    public static final Set<String> PATRONS = new HashSet<>();
    public static final Timer TIMER = ReflectionHelper.getPrivateValue(Minecraft.class, ClientProxy.MINECRAFT, "timer", "field_71428_T", "aa");
    public static final SurvivalTab INVENTORY_TAB = SurvivalTabHandler.INSTANCE.create("container.inventory", GuiInventory.class);

    @Override
    public void onPreInit() {
        super.onPreInit();
        ListenableFuture<String> patronFuture = WebUtils.readURLAsync("https://gist.githubusercontent.com/gegy1000/7a6d39cf7a2c1f794ffb9037e8146adc/raw/llibrary_patrons.json");
        patronFuture.addListener(() -> {
            try {
                String result = patronFuture.get();
                if (result != null) {
                    Collections.addAll(PATRONS, new Gson().fromJson(result, String[].class));
                }
            } catch (Exception e) {
                LLibrary.LOGGER.error("Failed to load Patron list", e);
            }
        }, Runnable::run);

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        ModelLoaderRegistry.registerLoader(TabulaModelHandler.INSTANCE);
        TabulaModelHandler.INSTANCE.addDomain("com/ilexiconn/llibrary");
        RenderingRegistry.registerEntityRenderingHandler(PartEntity.class, new PartRenderer.Factory());

        Thread thread = new Thread(LanguageHandler.INSTANCE::load);
        thread.setName("LLibrary Language Download");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onPostInit() {
        super.onPostInit();
    }

    @Override
    public <T extends AbstractMessage<T>> void handleMessage(final T message, final MessageContext messageContext) {
        if (messageContext.side.isServer()) {
            super.handleMessage(message, messageContext);
        } else {
            ClientProxy.MINECRAFT.addScheduledTask(() -> message.onClientReceived(ClientProxy.MINECRAFT, message, ClientProxy.MINECRAFT.player, messageContext));
        }
    }

    @Override
    public float getPartialTicks() {
        return ClientProxy.TIMER.renderPartialTicks;
    }

    @Override
    public void showSnackbar(Snackbar snackbar) {
        ClientProxy.SNACKBAR_LIST.add(new SnackbarGUI(snackbar));
    }

    @Override
    public void setTPS(float tickRate) {
        ClientProxy.TIMER.tickLength = 1000.0F / tickRate;
    }
}
