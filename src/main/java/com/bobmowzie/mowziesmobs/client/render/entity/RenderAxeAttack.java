package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class RenderAxeAttack extends EntityRenderer<EntityAxeAttack> {
    public static ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    ModelAxeAttack model;

    public RenderAxeAttack(EntityRendererProvider.Context mgr) {
        super(mgr);
        model = new ModelAxeAttack();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAxeAttack entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityAxeAttack axe, float entityYaw, float delta, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) {
            Player player = Minecraft.getInstance().player;
            if (player != null && player == axe.getCaster()) {
                matrixStackIn.pushPose();
                Vec3 prevAxePos = new Vec3(axe.xOld, axe.yOld, axe.zOld);
                Vec3 prevPlayerPos = new Vec3(player.xOld, player.yOld, player.zOld);
                Vec3 axePos = prevAxePos.add(axe.position().subtract(prevAxePos).scale(delta));
                Vec3 playerPos = prevPlayerPos.add(player.position().subtract(prevPlayerPos).scale(delta));
                Vec3 deltaPos = axePos.subtract(playerPos).scale(-1);
                matrixStackIn.translate(deltaPos.x(), deltaPos.y(), deltaPos.z());
                matrixStackIn.mulPose(new Quaternionf(new AxisAngle4f(player.getYRot() * (float) Math.PI/180f, new Vector3f(0, -1, 0))));
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(TEXTURE));
                model.setupAnim(axe, 0, 0, axe.tickCount + delta, 0, 0);
                model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                matrixStackIn.popPose();
            }
        }
    }
}
