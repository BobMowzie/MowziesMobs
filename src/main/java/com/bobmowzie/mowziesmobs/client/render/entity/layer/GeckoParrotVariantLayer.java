package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;

public class GeckoParrotVariantLayer extends ParrotOnShoulderLayer<AbstractClientPlayer> implements IGeckoRenderLayer {
    private final GeckoRenderPlayer renderPlayerAnimated;

    public GeckoParrotVariantLayer(GeckoRenderPlayer rendererIn) {
        super(rendererIn);
        renderPlayerAnimated = rendererIn;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        try {
            MowzieGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone("Body");
            PoseStack newMatrixStack = new PoseStack();
            newMatrixStack.last().normal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.last().pose().multiply(bone.getWorldSpaceXform());
            this.render(newMatrixStack, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
            this.render(newMatrixStack, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
        }
        catch (RuntimeException ignored) {}
    }
}
