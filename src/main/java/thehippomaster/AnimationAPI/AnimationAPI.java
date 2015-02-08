package thehippomaster.AnimationAPI;

import net.minecraft.entity.Entity;
import thehippomaster.AnimationAPI.packet.PacketAnim;
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
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "AnimationAPI", name = "AnimationAPI", version = "1.2.4")
public class AnimationAPI {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("AnimAPI");
		wrapper.registerMessage(PacketAnim.Handler.class, PacketAnim.class, 0, Side.CLIENT);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.initTimer();
	}
	
	public static boolean isClient() {
		return FMLCommonHandler.instance().getSide().isClient();
	}
	
	public static boolean isEffectiveClient() {
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	public static void sendAnimPacket(IAnimatedEntity entity, int animID) {
		if(isEffectiveClient()) return;
		entity.setAnimID(animID);
		wrapper.sendToAll(new PacketAnim((byte)animID, ((Entity)entity).getEntityId()));
	}
	
	@Instance("AnimationAPI")
	public static AnimationAPI instance;
	@SidedProxy(clientSide="thehippomaster.AnimationAPI.client.ClientProxy", serverSide="thehippomaster.AnimationAPI.CommonProxy")
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper wrapper;
	
	public static final String[] fTimer;
	
	static {
		fTimer = new String[] {"field_71428_T", "S", "timer"};
	}
}
