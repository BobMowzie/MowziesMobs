package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelEarthboreGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthboreGauntlet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEarthboreGauntlet extends GeoItemRenderer<ItemEarthboreGauntlet> {

    public RenderEarthboreGauntlet() {
        super(new ModelEarthboreGauntlet());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!modelProvider.getAnimationProcessor().getModelRendererList().isEmpty()) {
            if (transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND ||
                transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                modelProvider.getBone("root").setHidden(true);
                modelProvider.getBone("rootFlipped").setHidden(false);
            }
            else {
                modelProvider.getBone("root").setHidden(false);
                modelProvider.getBone("rootFlipped").setHidden(true);
            }
        }
        super.renderByItem(itemStack, transformType, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
