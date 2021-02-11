package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSuperNova;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderSuperNova extends EntityRenderer<EntitySuperNova> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova.png");
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_1.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_2.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_3.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_4.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_5.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_6.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_7.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_8.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_9.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_10.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_11.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_12.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_13.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_14.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_15.png"),
            new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_16.png")
    };
    public ModelSuperNova model;

    public RenderSuperNova(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelSuperNova();
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySuperNova entity) {
        int index = entity.ticksExisted % TEXTURES.length;
        return TEXTURES[index];
    }

    @Override
    public void render(EntitySuperNova entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float ageFrac = (entityIn.ticksExisted + partialTicks) / (float)(EntitySuperNova.DURATION);
        RenderSystem.disableCull();
        RenderSystem.disableLighting();
        matrixStackIn.push();
        int i = (int) entityIn.getBrightness();
        int k = i >> 16 & 255;
        i = 240 | k << 16;
        int j = i >> 16 & 65535;
        k = i & 65535;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k); // TODO

        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(entityIn)));
        model.render(matrixStackIn, ivertexbuilder, packedLightIn, 0, 1, 1, 1, 1);

        matrixStackIn.pop();
        RenderSystem.enableLighting();
        RenderSystem.enableCull();
    }
}
