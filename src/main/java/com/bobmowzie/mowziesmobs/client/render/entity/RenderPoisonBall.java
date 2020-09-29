package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPoisonBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderPoisonBall extends EntityRenderer<EntityPoisonBall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/poison_ball.png");
    public ModelPoisonBall model;

    public RenderPoisonBall(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelPoisonBall();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPoisonBall entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(EntityPoisonBall entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.rotatef(entityYaw, 0, -1, 0);
        bindTexture(TEXTURE);
        model.render(entity, 0.0725F, partialTicks);
        GlStateManager.popMatrix();
    }
}
