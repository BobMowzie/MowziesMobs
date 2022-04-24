package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Quaternion;

public class RenderFallingBlock extends EntityRenderer<EntityFallingBlock> {
    public RenderFallingBlock(EntityRendererManager mgr) {
        super(mgr);
    }

    @Override
    public void render(EntityFallingBlock entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        matrixStackIn.push();
        matrixStackIn.translate(0, 0.5f, 0);
        if (entityIn.getMode() == EntityFallingBlock.EnumFallingBlockMode.MOBILE) {
            matrixStackIn.rotate(new Quaternion(0, Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()), 0, true));
            matrixStackIn.rotate(new Quaternion(Mth.lerp(partialTicks, entityIn.xRot0, entityIn.getXRot()), 0, 0, true));
        }
        else {
            matrixStackIn.translate(0, Mth.lerp(partialTicks, entityIn.prevAnimY, entityIn.animY), 0);
            matrixStackIn.translate(0, -1, 0);
        }
        matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
        dispatcher.renderBlock(entityIn.getBlock(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFallingBlock entity) {
        return null;
    }
}
