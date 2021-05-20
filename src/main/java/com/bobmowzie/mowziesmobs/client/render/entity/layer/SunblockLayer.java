package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class SunblockLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T,M> {
    private static final ResourceLocation SUNBLOCK_ARMOR = new ResourceLocation(MowziesMobs.MODID, "textures/entity/sunblock_glow.png");

    public SunblockLayer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entitylivingbaseIn, LivingCapability.LivingProvider.LIVING_CAPABILITY);
        if (livingCapability != null && livingCapability.getHasSunblock()) {
            float f = (float) entitylivingbaseIn.ticksExisted + partialTicks;
            EntityModel<T> entitymodel = this.getEntityModel();
            entitymodel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.getEntityModel().copyModelAttributesTo(entitymodel);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(this.func_225633_a_(), this.func_225634_a_(f), f * 0.01F));
            entitymodel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            entitymodel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 0.1F, 1.0F);
        }
    }

    protected float func_225634_a_(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected ResourceLocation func_225633_a_() {
        return SUNBLOCK_ARMOR;
    }
}
