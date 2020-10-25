package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.net.NetBuilder;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(MowziesMobs.MODID)
public final class MowziesMobs {
    public static final String MODID = "mowziesmobs";
    public static ServerProxy PROXY;

    @SuppressWarnings("Convert2MethodRef")
    public static final SimpleChannel NETWORK = new NetBuilder(new ResourceLocation(MODID, "net"))
            .build();

//    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.server.ServerProxy")
//    public static ServerProxy PROXY;
//    @NetworkWrapper({
//            MessagePlayerSummonSunstrike.class,
//            MessagePlayerSolarBeam.class,
//            MessagePlayerAttackMob.class,
//            MessageBarakoTrade.class,
//            MessageAddFreezeProgress.class,
//            MessageUnfreezeEntity.class,
//            MessageLeftMouseDown.class,
//            MessageLeftMouseUp.class,
//            MessageRightMouseDown.class,
//            MessageRightMouseUp.class,
//            MessageBlackPinkInYourArea.class
//    })
//    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    public MowziesMobs() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        CreativeTabHandler.INSTANCE.onInit();
//        MMSounds.REG.register(bus);
//        BlockHandler.REG.register(bus);
//        EntityHandler.REG.register(bus);
//        ItemHandler.REG.register(bus);
//        RecipeHandler.REG.register(bus);
//        PotionHandler.REG.register(bus);
//        LootTableHandler.REG.register(bus);
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        PROXY.init(bus);
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
//        EntityPropertiesHandler.INSTANCE.registerProperties(MowziePlayerProperties.class);
//        EntityPropertiesHandler.INSTANCE.registerProperties(MowzieLivingProperties.class);
//        GameRegistry.registerWorldGenerator(new MowzieWorldGenerator(), 0);
//        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        CapabilityHandler.register();
        SpawnHandler.INSTANCE.registerSpawnPlacementTypes();

        AdvancementHandler.preInit();
    }

    private void init(FMLLoadCompleteEvent event) {
        SpawnHandler.INSTANCE.registerSpawns();
    }
}
