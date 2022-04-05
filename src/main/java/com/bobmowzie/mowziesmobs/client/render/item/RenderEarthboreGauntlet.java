package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelEarthboreGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthboreGauntlet;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEarthboreGauntlet extends GeoItemRenderer<ItemEarthboreGauntlet> {

    public RenderEarthboreGauntlet() {
        super(new ModelEarthboreGauntlet());
    }

    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!modelProvider.getAnimationProcessor().getModelRendererList().isEmpty()) {
            if (transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ||
                transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                modelProvider.getBone("root").setHidden(true);
                modelProvider.getBone("rootFlipped").setHidden(false);
            }
            else {
                modelProvider.getBone("root").setHidden(false);
                modelProvider.getBone("rootFlipped").setHidden(true);
            }
        }
        super.func_239207_a_(itemStack, transformType, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
