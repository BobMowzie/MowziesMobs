package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.biome.BiomeDictionaryHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.world.MowzieStructureGenerator;
import com.bobmowzie.mowziesmobs.server.world.MowzieWorldGenerator;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerSolarBeam;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerSummonSunstrike;
import com.bobmowzie.mowziesmobs.server.message.MessageSwingWroughtAxe;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.ilexiconn.llibrary.server.config.Config;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = MowziesMobs.MODID, name = MowziesMobs.NAME, version = MowziesMobs.VERSION, dependencies = MowziesMobs.DEPENDENCIES)
public class MowziesMobs {
    public static final String MODID = "mowziesmobs";
    public static final String NAME = "Mowzie's Mobs";
    public static final String VERSION = "1.2.8";
    public static final String LLIBRARY_VERSION = "1.3.1";
    public static final String DEPENDENCIES = "required-after:llibrary@[" + MowziesMobs.LLIBRARY_VERSION + ",)";

    @Instance(MowziesMobs.MODID)
    public static MowziesMobs INSTANCE;
    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.server.ServerProxy")
    public static ServerProxy PROXY;
    @NetworkWrapper({MessageSwingWroughtAxe.class, MessagePlayerSummonSunstrike.class, MessagePlayerSolarBeam.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    @Config
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
        MowziesMobs.GENERATOR = new MowzieStructureGenerator();
        MowziesMobs.CONFIG = net.ilexiconn.llibrary.server.config.ConfigHandler.INSTANCE.registerConfig(this, event.getSuggestedConfigurationFile(), new ConfigHandler());
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
