package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;

public class RenderFallingBlock extends EntityRenderer<EntityFallingBlock> {
    public RenderFallingBlock(EntityRenderDispatcher mgr) {
        super(mgr);
    }

    @Override
    public void render(EntityFallingBlock entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 0.5f, 0);
        if (entityIn.getMode() == EntityFallingBlock.EnumFallingBlockMode.MOBILE) {
            matrixStackIn.mulPose(new Quaternion(0, Mth.lerp(partialTicks, entityIn.yRotO, entityIn.yRot), 0, true));
            matrixStackIn.mulPose(new Quaternion(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.xRot), 0, 0, true));
        }
        else {
            matrixStackIn.translate(0, Mth.lerp(partialTicks, entityIn.prevAnimY, entityIn.animY), 0);
            matrixStackIn.translate(0, -1, 0);
        }
        matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
        dispatcher.renderSingleBlock(entityIn.getBlock(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFallingBlock entity) {
        return null;
    }
}
