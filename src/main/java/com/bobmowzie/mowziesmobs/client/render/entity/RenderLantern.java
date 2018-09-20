package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 5/8/2017.
 */
public class RenderLantern extends RenderLiving<EntityLantern> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/mmlantern.png");

    public RenderLantern(RenderManager mgr) {
        super(mgr, new ModelLantern(), 0.6f);
    }

    @Override
    protected float getDeathMaxRotation(EntityLantern entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityLantern entity) {
        return RenderLantern.TEXTURE;
    }

    @Override
    public void doRender(EntityLantern entity, double x, double y, double z, float yaw, float delta) {

        int i = 65680;
        int j = i % 65536;
        int k = i / 65536;

        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

        super.doRender(entity, x, y, z, yaw, delta);

        i = entity.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;

        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        GlStateManager.enableLighting();
    }
}
