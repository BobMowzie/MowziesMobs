package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class LanternGelLayer<T extends EntityLantern> extends LayerRenderer<T, ModelLantern<T>> {
    private final EntityModel<T> model = new ModelLantern<>(true);

    public LanternGelLayer(IEntityRenderer<T, ModelLantern<T>> renderer) {
        super(renderer);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            this.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.model.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(MMRenderType.getLantern(this.getEntityTexture(entitylivingbaseIn)));
            this.model.render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
