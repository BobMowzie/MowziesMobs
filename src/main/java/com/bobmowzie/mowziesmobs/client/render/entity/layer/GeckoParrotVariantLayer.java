package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.ParrotVariantLayer;

public class GeckoParrotVariantLayer extends ParrotVariantLayer<AbstractClientPlayerEntity> implements IGeckoRenderLayer {
    private final GeckoRenderPlayer renderPlayerAnimated;

    public GeckoParrotVariantLayer(GeckoRenderPlayer rendererIn) {
        super(rendererIn);
        renderPlayerAnimated = rendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        try {
            MowzieGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone("Body");
            MatrixStack newMatrixStack = new MatrixStack();
            newMatrixStack.getLast().getNormal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.getLast().getMatrix().mul(bone.getWorldSpaceXform());
            this.renderParrot(newMatrixStack, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
            this.renderParrot(newMatrixStack, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
        }
        catch (RuntimeException ignored) {}
    }
}
