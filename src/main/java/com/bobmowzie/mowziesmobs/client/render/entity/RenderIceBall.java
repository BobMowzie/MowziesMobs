package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderIceBall extends EntityRenderer<EntityIceBall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/ice_ball.png");
    public ModelIceBall model;

    public RenderIceBall(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelIceBall();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityIceBall entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityIceBall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(new Vector3f(0, -1, 0), entityYaw, true));
        matrixStackIn.translate(0, -0.5f, 0);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(TEXTURE));
        model.setRotationAngles(entityIn, 0, 0, entityIn.ticksExisted + partialTicks, 0, 0);
        model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStackIn.pop();
    }
}
