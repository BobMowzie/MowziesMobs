package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GeckoSunblockLayer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private static final ResourceLocation SUNBLOCK_ARMOR = new ResourceLocation(MowziesMobs.MODID, "textures/entity/sunblock_glow.png");

    public GeckoSunblockLayer(IGeoRenderer<T> entityRendererIn, EntityRendererProvider.Context context) {
        super(entityRendererIn);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entitylivingbaseIn, CapabilityHandler.LIVING_CAPABILITY);
        if (livingCapability != null && livingCapability.getHasSunblock()) {
            float f = (float) entitylivingbaseIn.tickCount + partialTicks;
            RenderType renderType = RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f), f * 0.01F);

            getRenderer().render(getEntityModel().getModel(getEntityModel().getModelLocation(entitylivingbaseIn)),
                    entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn, bufferIn.getBuffer(renderType),
                    packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 0.1F, 1.0F);
        }
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected ResourceLocation getTextureLocation() {
        return SUNBLOCK_ARMOR;
    }
}
