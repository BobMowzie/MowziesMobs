package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.audio.MovingSoundSuntrike;
import com.bobmowzie.mowziesmobs.client.model.entity.*;
import com.bobmowzie.mowziesmobs.client.model.item.ModelBarakoaMask;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtAxe;
import com.bobmowzie.mowziesmobs.client.model.item.ModelWroughtHelm;
import com.bobmowzie.mowziesmobs.client.particle.EntityMMFX;
import com.bobmowzie.mowziesmobs.client.particle.EntityOrbFX;
import com.bobmowzie.mowziesmobs.client.playeranimation.PlayerAnimationHandlerClient;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.entity.*;
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
import cpw.mods.fml.common.FMLCommonHandler;
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
    private static final ModelWroughtHelm modelWroughtHelm = new ModelWroughtHelm();
    private static final ModelBarakoaMask modelBarakoaMask = new ModelBarakoaMask();

    @Override
    public void onInit() {
        super.onInit();
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        /*if (MowziesMobs.isDebugging()) {
            MinecraftForge.EVENT_BUS.register(ModelGrapher.INSTANCE);
            FMLCommonHandler.instance().bus().register(ModelGrapher.INSTANCE);
        }*/
        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, new RenderBabyFoliaath(new ModelBabyFoliaath(), 0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, new RenderFoliaath(new ModelFoliaath(), 0));
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, new RenderWroughtnaut(new ModelWroughtnaut(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeLeader.class, new RenderTribeLeader(new ModelTribeLeader(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeElite.class, new RenderTribesman(new ModelTribesman(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeHunter.class, new RenderTribesman(new ModelTribesman(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeVillager.class, new RenderTribesman(new ModelTribesman(), 0.6F));
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, new RenderDart());
        RenderingRegistry.registerEntityRenderingHandler(EntitySunstrike.class, new RenderSunstrike());
        RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, new RenderSolarBeam());

        RenderHelper.registerItem3dRenderer(ItemHandler.INSTANCE.wrought_axe, new ModelWroughtAxe(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtAxe.png"));
        RenderHelper.registerItem3dRenderer(ItemHandler.INSTANCE.wrought_helmet, new ModelWroughtHelm(), new ResourceLocation(MowziesMobs.MODID, "textures/items/modeled/textureWroughtHelm.png"));
        ItemBarakoaMask[] masks = ItemHandler.INSTANCE.barakoa_masks;
        for (int i = 0; i < masks.length; i++) {
            RenderHelper.registerItem3dRenderer(masks[i], new ModelBarakoaMask(), new ResourceLocation(MowziesMobs.MODID, String.format("textures/entity/textureTribesman%s.png", i + 1)));
        }
        PlayerAnimationHandlerClient playerAnimationHandlerClient = new PlayerAnimationHandlerClient();
        FMLCommonHandler.instance().bus().register(playerAnimationHandlerClient);
        MinecraftForge.EVENT_BUS.register(playerAnimationHandlerClient);
        MinecraftForge.EVENT_BUS.register(EntityMMFX.Stitcher.INSTANCE);
    }

    @Override
    public ModelBiped getArmorModel(int type) {
        switch (type) {
            default:
            case 0:
                return modelWroughtHelm;
            case 1:
                return modelBarakoaMask;
        }
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundSuntrike(strike));
    }

    @Override
    public void spawnOrbFX(World world, double x, double y, double z, double targetX, double targetZ) {
        EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
        effectRenderer.addEffect(new EntityOrbFX(world, x, y, z, targetX, targetZ));
    }

    @Override
    public void spawnOrbFX(World world, double x, double y, double z, double targetX, double targetY, double targetZ, double speed) {
        EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
        effectRenderer.addEffect(new EntityOrbFX(world, x, y, z, targetX, targetY, targetZ, speed));
    }
}
