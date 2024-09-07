package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelSculptorStaff;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderSculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.Optional;

public class RenderSculptorStaff extends GeoItemRenderer<ItemSculptorStaff> {
    public float disappearController = 0;

    public RenderSculptorStaff() {
        super(new ModelSculptorStaff());
    }

    @Override
    public Color getRenderColor(ItemSculptorStaff animatable, float partialTick, int packedLight) {
        return Color.ofRGBA(1, 1, 1, 1.0f - disappearController);
    }

    @Override
    public RenderType getRenderType(ItemSculptorStaff animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (disappearController > 0) {
            return RenderType.entityTranslucent(texture);
        }
        else {
            return super.getRenderType(animatable, texture, bufferSource, partialTick);
        }
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);

        Optional<GeoBone> disappearControllerBone = model.getBone("disappearController");
        disappearControllerBone.ifPresent(geoBone -> disappearController = geoBone.getPosX());
    }
}
