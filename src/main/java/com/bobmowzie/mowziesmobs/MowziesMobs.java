package com.bobmowzie.mowziesmobs;

import net.ilexiconn.llibrary.common.content.ContentHelper;
import net.minecraftforge.common.MinecraftForge;

import com.bobmowzie.mowziesmobs.common.ServerEventHandler;
import com.bobmowzie.mowziesmobs.common.ServerProxy;
import com.bobmowzie.mowziesmobs.common.biome.MMBiomeDictionarySpawns;
import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import com.bobmowzie.mowziesmobs.common.entity.MMEntityRegistry;
import com.bobmowzie.mowziesmobs.common.gen.MMStructureGenerator;
import com.bobmowzie.mowziesmobs.common.gen.MMWorldGenerator;
import com.bobmowzie.mowziesmobs.common.item.MMItems;
import com.bobmowzie.mowziesmobs.common.message.MessageSwingWroughtAxe;

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

@Mod(modid = "mowziesmobs", name = "Mowzie's Mobs", version = "${version}", dependencies = "required-after:llibrary@[0.3.3,)")
public class MowziesMobs
{
    public static final MMStructureGenerator gen = new MMStructureGenerator();
    public static SimpleNetworkWrapper networkWrapper;
    @Instance("mowziesmobs")
    public static MowziesMobs instance;
    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.client.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.common.ServerProxy")
    public static ServerProxy proxy;

    public static String getModId()
    {
        return "mowziesmobs:";
    }

    public static boolean isDebugging()
    {
        return "${version}".equals("${" + "version" + "}");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ContentHelper.init(new MMTabs(), new MMItems(), new MMEntityRegistry());

        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();

        GameRegistry.registerWorldGenerator(new MMWorldGenerator(), 0);

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("mowziesMobs");
        networkWrapper.registerMessage(MessageSwingWroughtAxe.class, MessageSwingWroughtAxe.class, 0, Side.CLIENT);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        MMBiomeDictionarySpawns.init();
    }
}
