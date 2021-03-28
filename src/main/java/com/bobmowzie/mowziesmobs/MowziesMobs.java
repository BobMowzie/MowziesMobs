package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.MMModels;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.nio.file.Path;

@Mod(MowziesMobs.MODID)
public final class MowziesMobs {
    public static final String MODID = "mowziesmobs";
    public static ServerProxy PROXY;

    public static SimpleChannel NETWORK;

    public MowziesMobs() {
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CreativeTabHandler.INSTANCE.onInit();
        MMSounds.REG.register(bus);
        BlockHandler.REG.register(bus);
        EntityHandler.register();
        ParticleHandler.REG.register(bus);

        PROXY.init(bus);
        bus.<FMLCommonSetupEvent>addListener(this::init);
        bus.<ModelRegistryEvent>addListener(this::init);
        bus.<FMLLoadCompleteEvent>addListener(this::init);

        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }

    public void init(final FMLCommonSetupEvent event) {
//        GameRegistry.registerWorldGenerator(new MowzieWorldGenerator(), 0);
//        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        CapabilityHandler.register();
        SpawnHandler.INSTANCE.registerSpawnPlacementTypes();
        PROXY.initNetwork();
        AdvancementHandler.preInit();
        LootTableHandler.init();
    }

    private void init(ModelRegistryEvent modelRegistryEvent) {

    }

    private void init(FMLLoadCompleteEvent event) {
        SpawnHandler.INSTANCE.registerSpawns();
        FeatureHandler.addStructureGeneration();
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.onLateInit(bus);
    }
}
