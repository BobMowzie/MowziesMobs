package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.biome.BiomeDictionaryHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.gen.MowzieStructureGenerator;
import com.bobmowzie.mowziesmobs.server.gen.MowzieWorldGenerator;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerSolarBeam;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerSummonSunstrike;
import com.bobmowzie.mowziesmobs.server.message.MessageSwingWroughtAxe;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = MowziesMobs.MODID, name = MowziesMobs.NAME, version = MowziesMobs.VERSION, dependencies = MowziesMobs.DEPENDENCIES)
public class MowziesMobs {
    public static final String MODID = "mowziesmobs";
    public static final String NAME = "Mowzie's Mobs";
    public static final String VERSION = "1.2.5";
    public static final String LLIBRARY_VERSION = "1.1.0";
    public static final String DEPENDENCIES = "required-after:llibrary@[" + MowziesMobs.LLIBRARY_VERSION + ",)";

    @Instance(MowziesMobs.MODID)
    public static MowziesMobs INSTANCE;
    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.server.ServerProxy")
    public static ServerProxy PROXY;

    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static ConfigHandler CONFIG;
    public static MowzieStructureGenerator GENERATOR;

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
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

        GameRegistry.registerWorldGenerator(new MowzieWorldGenerator(), 0);

        MowziesMobs.NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MowziesMobs.MODID);
        MowziesMobs.NETWORK_WRAPPER.registerMessage(MessageSwingWroughtAxe.class, MessageSwingWroughtAxe.class, 0, Side.CLIENT);
        MowziesMobs.NETWORK_WRAPPER.registerMessage(MessagePlayerSummonSunstrike.class, MessagePlayerSummonSunstrike.class, 1, Side.SERVER);
        MowziesMobs.NETWORK_WRAPPER.registerMessage(MessagePlayerSolarBeam.class, MessagePlayerSolarBeam.class, 2, Side.SERVER);
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        BiomeDictionaryHandler.INSTANCE.onInit();
    }

    private static ModContainer container;

    public static ModContainer getModContainer() {
        if (container == null) {
            container = FMLCommonHandler.instance().findContainerFor(MowziesMobs.MODID);
        }
        return container;
    }
}
