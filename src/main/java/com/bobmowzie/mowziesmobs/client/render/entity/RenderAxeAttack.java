package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderAxeAttack extends EntityRenderer<EntityAxeAttack> {
    public static ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    ModelAxeAttack model;

    public RenderAxeAttack(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelAxeAttack();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityAxeAttack entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityAxeAttack entity, float entityYaw, float delta, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == entity.getCaster() && Minecraft.getInstance().gameSettings.thirdPersonView == 0) {
            RenderSystem.pushMatrix();
            RenderSystem.rotatef(entityYaw, 0, -1, 0);
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            model.render(matrixStackIn, (IVertexBuilder) bufferIn, packedLightIn, 0, 1, 1, 1, 1);
            RenderSystem.popMatrix();
        }
    }
}
