package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.*;
import com.bobmowzie.mowziesmobs.client.model.item.BarakoaMaskModel;
import com.bobmowzie.mowziesmobs.client.model.item.WroughtAxeModel;
import com.bobmowzie.mowziesmobs.client.model.item.WroughtHelmModel;
import com.bobmowzie.mowziesmobs.client.particle.MowzieParticle;
import com.bobmowzie.mowziesmobs.client.particle.OrbParticle;
import com.bobmowzie.mowziesmobs.client.render.*;
import com.bobmowzie.mowziesmobs.client.sound.SunstrikeSound;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeElite;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeHunter;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeLeader;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeVillager;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    private static final WroughtHelmModel WROUGHT_HELM_MODEL = new WroughtHelmModel();
    private static final BarakoaMaskModel BARAKOA_MASK_MODEL = new BarakoaMaskModel();

    @Override
    public void onInit() {
        super.onInit();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        /*if (MowziesMobs.isDebugging()) {
            MinecraftForge.EVENT_BUS.register(ModelGrapher.INSTANCE);
            FMLCommonHandler.instance().bus().register(ModelGrapher.INSTANCE);
        }*/
        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, new FoliaathBabyRenderer(new FoliaathBabyModel(), 0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new FoliaathRenderer(new FoliaathModel(), 0));
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, new WroughtnautRenderer(new WroughtnautModel(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeLeader.class, new TribeLeaderRenderer(new TribeLeaderModel(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeElite.class, new TribesmanRenderer(new TribesmanModel(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeHunter.class, new TribesmanRenderer(new TribesmanModel(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeVillager.class, new TribesmanRenderer(new TribesmanModel(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new DartRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntitySunstrike.class, new SunstrikeRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, new SolarBeamRenderer());

        RenderHelper.registerItem3dRenderer(ItemHandler.INSTANCE.wrought_axe, new WroughtAxeModel(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtAxe.png"));
        RenderHelper.registerItem3dRenderer(ItemHandler.INSTANCE.wrought_helmet, new WroughtHelmModel(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtHelm.png"));
        ItemBarakoaMask[] masks = ItemHandler.INSTANCE.barakoa_masks;
        for (int i = 0; i < masks.length; i++) {
            RenderHelper.registerItem3dRenderer(masks[i], new BarakoaMaskModel(), new ResourceLocation(MowziesMobs.MODID, String.format("textures/entity/textureTribesman%s.png", i + 1)));
        }
        MinecraftForge.EVENT_BUS.register(MowzieParticle.IconStitcher.INSTANCE);
    }

    @Override
    public ModelBiped getArmorModel(int type) {
        switch (type) {
            default:
            case 0:
                return WROUGHT_HELM_MODEL;
            case 1:
                return BARAKOA_MASK_MODEL;
        }
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SunstrikeSound(strike));
    }

    @Override
    public void spawnOrbFX(World world, double x, double y, double z, double targetX, double targetZ) {
        EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
        effectRenderer.addEffect(new OrbParticle(world, x, y, z, targetX, targetZ));
    }

    @Override
    public void spawnOrbFX(World world, double x, double y, double z, double targetX, double targetY, double targetZ, double speed) {
        EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
        effectRenderer.addEffect(new OrbParticle(world, x, y, z, targetX, targetY, targetZ, speed));
    }
}
