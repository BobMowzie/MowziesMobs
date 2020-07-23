package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderIceBall extends Render<EntityIceBall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/ice_ball.png");
    public ModelIceBall model;

    public RenderIceBall(RenderManager mgr) {
        super(mgr);
        model = new ModelIceBall();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityIceBall entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(EntityIceBall entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y - 1, z);
        GlStateManager.rotate(entityYaw, 0, -1, 0);
        bindTexture(TEXTURE);
        model.render(entity, 0.0725F, partialTicks);
        GlStateManager.popMatrix();
    }
}
