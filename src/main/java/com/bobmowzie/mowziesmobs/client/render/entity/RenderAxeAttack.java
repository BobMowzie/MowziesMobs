package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
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
    public void render(EntityAxeAttack axe, float entityYaw, float delta, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null && player == axe.getCaster()) {
                matrixStackIn.push();
                Vector3d prevAxePos = new Vector3d(axe.lastTickPosX, axe.lastTickPosY, axe.lastTickPosZ);
                Vector3d prevPlayerPos = new Vector3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
                Vector3d axePos = prevAxePos.add(axe.getPositionVec().subtract(prevAxePos).scale(delta));
                Vector3d playerPos = prevPlayerPos.add(player.getPositionVec().subtract(prevPlayerPos).scale(delta));
                Vector3d deltaPos = axePos.subtract(playerPos).scale(-1);
                matrixStackIn.translate(deltaPos.getX(), deltaPos.getY(), deltaPos.getZ());
                matrixStackIn.rotate(new Quaternion(new Vector3f(0, -1, 0), player.rotationYaw, true));
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(TEXTURE));
                model.setRotationAngles(axe, 0, 0, axe.ticksExisted + delta, 0, 0);
                model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                matrixStackIn.pop();
            }
        }
    }
}
