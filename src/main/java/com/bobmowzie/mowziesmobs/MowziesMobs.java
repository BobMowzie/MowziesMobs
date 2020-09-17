package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.compat.Thaumcraft;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.gui.GuiHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageLeftMouseUp;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseDown;
import com.bobmowzie.mowziesmobs.server.message.mouse.MessageRightMouseUp;
import com.bobmowzie.mowziesmobs.server.net.NetBuilder;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.recipe.RecipeHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import com.bobmowzie.mowziesmobs.server.world.MowzieWorldGenerator;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import com.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.ClientModLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

    public void init(FMLCommonSetupEvent event) {
//        EntityPropertiesHandler.INSTANCE.registerProperties(MowziePlayerProperties.class);
//        EntityPropertiesHandler.INSTANCE.registerProperties(MowzieLivingProperties.class);
//        GameRegistry.registerWorldGenerator(new MowzieWorldGenerator(), 0);
//        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        SpawnHandler.INSTANCE.registerSpawnPlacementTypes();

        AdvancementHandler.preInit();
    }

    private void init(FMLLoadCompleteEvent event) {
        SpawnHandler.INSTANCE.registerSpawns();
    }
}
