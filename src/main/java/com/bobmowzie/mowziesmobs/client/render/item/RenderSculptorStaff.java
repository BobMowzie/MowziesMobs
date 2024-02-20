package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelSculptorStaff;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderSculptorStaff extends GeoItemRenderer<ItemSculptorStaff> {

    public RenderSculptorStaff() {
        super(new ModelSculptorStaff());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        super.renderByItem(itemStack, transformType, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
