package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.compat.Thaumcraft;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import com.bobmowzie.mowziesmobs.server.world.MowzieWorldGenerator;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MowziesMobs.MODID)
public final class MowziesMobs {
    public static final String MODID = "mowziesmobs";

    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.server.ServerProxy")
    public static ServerProxy PROXY;
    @NetworkWrapper({
            MessagePlayerSummonSunstrike.class,
            MessagePlayerSolarBeam.class,
            MessagePlayerAttackMob.class,
            MessageBarakoTrade.class,
            MessageAddFreezeProgress.class,
            MessageUnfreezeEntity.class,
            MessageLeftMouseDown.class,
            MessageLeftMouseUp.class,
            MessageRightMouseDown.class,
            MessageRightMouseUp.class,
            MessageBlackPinkInYourArea.class
    })
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    public MowziesMobs() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        CreativeTabHandler.INSTANCE.onInit();
        DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy()).onInit();
    }

    @EventHandler
    public void init(FMLPreInitializationEvent event) {
        EntityPropertiesHandler.INSTANCE.registerProperties(MowziePlayerProperties.class);
        EntityPropertiesHandler.INSTANCE.registerProperties(MowzieLivingProperties.class);
        GameRegistry.registerWorldGenerator(new MowzieWorldGenerator(), 0);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        SpawnHandler.INSTANCE.registerSpawnPlacementTypes();

        AdvancementHandler.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MowziesMobs.PROXY.onLateInit();
    }

    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        SpawnHandler.INSTANCE.registerSpawns();
    }
}
