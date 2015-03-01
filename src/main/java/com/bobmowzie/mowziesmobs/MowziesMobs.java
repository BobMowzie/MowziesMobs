package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.gui.MMGuiHandler;
import com.bobmowzie.mowziesmobs.packet.PacketPlaySound;
import com.bobmowzie.mowziesmobs.packet.foliaath.PacketDecreaseTimer;
import com.bobmowzie.mowziesmobs.packet.foliaath.PacketIncreaseTimer;
import com.bobmowzie.mowziesmobs.packet.foliaath.PacketSetActiveFalse;
import com.bobmowzie.mowziesmobs.packet.foliaath.PacketSetActiveTrue;
import com.bobmowzie.mowziesmobs.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.llib.mod.BasicLLibMod;

@Mod(modid = MowziesMobs.MODID, name = MowziesMobs.NAME, version = MowziesMobs.VERSION, dependencies = "required-after:llib@[1.7.10-0.2.1-34,)")
public class MowziesMobs extends BasicLLibMod
{
    public static final String MODID = "mowziesmobs";
    public static final String NAME = "Mowzie's Mobs";
    public static final String VERSION = "Alpha v0.1";

    public static SimpleNetworkWrapper networkWrapper;

    @Instance(MowziesMobs.MODID)
    public static MowziesMobs instance;

    @SidedProxy(clientSide = "com.bobmowzie.mowziesmobs.proxy.ClientProxy", serverSide = "com.bobmowzie.mowziesmobs.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent preEvent)
    {
        getContentHandler().addContentProvider(new MMTabs());
        //		getContentHandler().addContentProvider(new MMBlocks());
        getContentHandler().addContentProvider(new MMItems());
        getContentHandler().addContentProvider(new MMEntities());
        //		getContentHandler().addContentProvider(new MMRecipes());
        getContentHandler().init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.itemRegistry();
        proxy.tileEntityRegistry();
        proxy.entityRegistry();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new MMGuiHandler());
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("mmNetworkWrapper");
        networkWrapper.registerMessage(PacketIncreaseTimer.class, PacketIncreaseTimer.class, 0, Side.CLIENT);
        networkWrapper.registerMessage(PacketDecreaseTimer.class, PacketDecreaseTimer.class, 1, Side.CLIENT);
        networkWrapper.registerMessage(PacketPlaySound.class, PacketPlaySound.class, 2, Side.SERVER);
        networkWrapper.registerMessage(PacketSetActiveTrue.class, PacketSetActiveTrue.class, 3, Side.SERVER);
        networkWrapper.registerMessage(PacketSetActiveFalse.class, PacketSetActiveFalse.class, 4, Side.SERVER);
    }

    public static String getModID()
    {
        return MowziesMobs.MODID + ":";
    }

    public static void playSound(int entityId, String soundName)
    {
        playSound(entityId, soundName, 1, 1);
    }

    public static void playSound(int entityId, String soundName, float volume, float pitch)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) networkWrapper.sendToServer(new PacketPlaySound(entityId, soundName, volume, pitch));
    }
}
