package thehippomaster.AnimationAPI.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.CommonProxy;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void initTimer() {
		mcTimer = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), AnimationAPI.fTimer);
	}
	
	@Override
	public float getPartialTick() {
		return mcTimer.renderPartialTicks;
	}
	
	@Override
	public World getWorldClient() {
		return FMLClientHandler.instance().getWorldClient();
	}
	
	private Timer mcTimer;
}
