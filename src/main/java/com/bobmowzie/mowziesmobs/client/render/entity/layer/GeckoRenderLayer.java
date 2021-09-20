package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderPlayerAnimated;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class GeckoRenderLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    protected final RenderPlayerAnimated renderer;

    public GeckoRenderLayer(RenderPlayerAnimated entityRendererIn) {
        super(entityRendererIn);
        renderer = entityRendererIn;
    }

    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public ModelGeckoPlayer getGeckoModel() {
        return renderer.getAnimatedPlayerModel();
    }
}
