package com.bobmowzie.mowziesmobs;

import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.biome.BiomeDictionaryHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerSolarBeam;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerSummonSunstrike;
import com.bobmowzie.mowziesmobs.server.message.MessageSwingWroughtAxe;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.world.MowzieStructureGenerator;
import com.bobmowzie.mowziesmobs.server.world.MowzieWorldGenerator;

@Mod(modid = MowziesMobs.MODID, name = MowziesMobs.NAME, version = MowziesMobs.VERSION, dependencies = MowziesMobs.DEPENDENCIES)
public class MowziesMobs {
    public static final String MODID = "mowziesmobs";
    public static final String NAME = "Mowzie's Mobs";
    public static final String VERSION = "1.3.0";
    public static final String LLIBRARY_VERSION = "1.4.0";
    public static final String DEPENDENCIES = "required-after:llibrary@[" + MowziesMobs.LLIBRARY_VERSION + ",)";

    @Instance(MowziesMobs.MODID)
    public static MowziesMobs INSTANCE;
    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.server.ServerProxy")
    public static ServerProxy PROXY;
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static ConfigHandler CONFIG;
    public static MowzieStructureGenerator GENERATOR;
    private static ModContainer container;

    public static ModContainer getModContainer() {
        if (container == null) {
            container = FMLCommonHandler.instance().findContainerFor(MowziesMobs.MODID);
        }
        return container;
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MowziesMobs.NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MowziesMobs.MODID);
        NetworkHandler.INSTANCE.registerMessage(MowziesMobs.NETWORK_WRAPPER, MessageSwingWroughtAxe.class);
        NetworkHandler.INSTANCE.registerMessage(MowziesMobs.NETWORK_WRAPPER, MessagePlayerSummonSunstrike.class);
        NetworkHandler.INSTANCE.registerMessage(MowziesMobs.NETWORK_WRAPPER, MessagePlayerSolarBeam.class);
        MowziesMobs.CONFIG = net.ilexiconn.llibrary.server.config.ConfigHandler.INSTANCE.registerConfig(this, event.getSuggestedConfigurationFile(), new ConfigHandler());
        MowziesMobs.GENERATOR = new MowzieStructureGenerator();
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(ServerEventHandler.INSTANCE);
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        CreativeTabHandler.INSTANCE.onInit();
        ItemHandler.INSTANCE.onInit();
        BlockHandler.INSTANCE.onInit();
        EntityHandler.INSTANCE.onInit();
        PotionHandler.INSTANCE.onInit();

        MowziesMobs.PROXY.onInit();

        EntityPropertiesHandler.INSTANCE.registerProperties(MowziePlayerProperties.class);
        GameRegistry.registerWorldGenerator(new MowzieWorldGenerator(), 0);
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        BiomeDictionaryHandler.INSTANCE.onInit();
    }
}
