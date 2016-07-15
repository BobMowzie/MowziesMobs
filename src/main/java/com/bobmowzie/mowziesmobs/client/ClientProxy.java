package com.bobmowzie.mowziesmobs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.client.model.entity.FoliaathBabyModel;
import com.bobmowzie.mowziesmobs.client.model.entity.FoliaathModel;
import com.bobmowzie.mowziesmobs.client.model.entity.TribeLeaderModel;
import com.bobmowzie.mowziesmobs.client.model.entity.TribesmanModel;
import com.bobmowzie.mowziesmobs.client.model.entity.WroughtnautModel;
import com.bobmowzie.mowziesmobs.client.model.item.BarakoaMaskModel;
import com.bobmowzie.mowziesmobs.client.model.item.WroughtHelmetModel;
import com.bobmowzie.mowziesmobs.client.particle.MowzieParticle;
import com.bobmowzie.mowziesmobs.client.particle.OrbParticle;
import com.bobmowzie.mowziesmobs.client.render.entity.DartRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.FoliaathBabyRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.FoliaathRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.SolarBeamRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.SunstrikeRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.TribeLeaderRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.TribesmanRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.WroughtnautRenderer;
import com.bobmowzie.mowziesmobs.client.render.item.BarakoaMaskRenderer;
import com.bobmowzie.mowziesmobs.client.render.item.WroughtAxeRenderer;
import com.bobmowzie.mowziesmobs.client.render.item.WroughtHelmetRenderer;
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

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    private static final WroughtHelmetModel WROUGHT_HELM_MODEL = new WroughtHelmetModel();
    private static final BarakoaMaskModel BARAKOA_MASK_MODEL = new BarakoaMaskModel();

    @Override
    public void onInit() {
        super.onInit();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
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

        MinecraftForgeClient.registerItemRenderer(ItemHandler.INSTANCE.wrought_axe, new WroughtAxeRenderer());
        MinecraftForgeClient.registerItemRenderer(ItemHandler.INSTANCE.wrought_helmet, new WroughtHelmetRenderer());
        ItemBarakoaMask[] masks = ItemHandler.INSTANCE.barakoa_masks;
        for (int i = 0; i < masks.length; i++) {
            MinecraftForgeClient.registerItemRenderer(masks[i], new BarakoaMaskRenderer(i));
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
