package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public interface IGeckoRenderLayer {
    default void renderRecursively(GeoBone bone, MatrixStack matrixStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }
}
