package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelMMLantern;
import com.bobmowzie.mowziesmobs.server.entity.mmlantern.EntityMMLantern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 5/8/2017.
 */
public class RenderMMLantern extends RenderLiving<EntityMMLantern> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/mmlantern.png");

    public RenderMMLantern(RenderManager mgr) {
        super(mgr, new ModelMMLantern(), 0.6f);
    }

    @Override
    protected float getDeathMaxRotation(EntityMMLantern entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMMLantern entity) {
        return RenderMMLantern.TEXTURE;
    }

    @Override
    public void doRender(EntityMMLantern entity, double x, double y, double z, float yaw, float delta) {

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
