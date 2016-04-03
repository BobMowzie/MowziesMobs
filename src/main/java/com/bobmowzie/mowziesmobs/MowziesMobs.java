package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.common.ServerEventHandler;
import com.bobmowzie.mowziesmobs.common.ServerProxy;
import com.bobmowzie.mowziesmobs.common.biome.MMBiomeDictionarySpawns;
import com.bobmowzie.mowziesmobs.common.blocks.BlockHandler;
import com.bobmowzie.mowziesmobs.common.config.MMConfig;
import com.bobmowzie.mowziesmobs.common.creativetab.CreativeTabHandler;
import com.bobmowzie.mowziesmobs.common.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.common.gen.MMStructureGenerator;
import com.bobmowzie.mowziesmobs.common.gen.MMWorldGenerator;
import com.bobmowzie.mowziesmobs.common.item.ItemHandler;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSolarBeam;
import com.bobmowzie.mowziesmobs.common.message.MessagePlayerSummonSunstrike;
import com.bobmowzie.mowziesmobs.common.message.MessageSwingWroughtAxe;
import com.bobmowzie.mowziesmobs.common.potion.PotionHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.llibrary.server.config.ConfigHandler;
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
    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.common.ServerProxy")
    public static ServerProxy PROXY;

    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static MMConfig CONFIG;

    public static MMStructureGenerator gen = new MMStructureGenerator();

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MowziesMobs.CONFIG = ConfigHandler.INSTANCE.registerConfig(this, event.getSuggestedConfigurationFile(), new MMConfig());

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

        GameRegistry.registerWorldGenerator(new MMWorldGenerator(), 0);

        MowziesMobs.NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MowziesMobs.MODID);
        MowziesMobs.NETWORK_WRAPPER.registerMessage(MessageSwingWroughtAxe.class, MessageSwingWroughtAxe.class, 0, Side.CLIENT);
        MowziesMobs.NETWORK_WRAPPER.registerMessage(MessagePlayerSummonSunstrike.class, MessagePlayerSummonSunstrike.class, 1, Side.SERVER);
        MowziesMobs.NETWORK_WRAPPER.registerMessage(MessagePlayerSolarBeam.class, MessagePlayerSolarBeam.class, 2, Side.SERVER);
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        MMBiomeDictionarySpawns.init();
    }
}
