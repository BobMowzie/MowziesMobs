package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Quaternion;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vector3f;
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
//        Player player = Minecraft.getInstance().player;
//        if (player != null && player == axe.getCaster() && Minecraft.getInstance().gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
//            matrixStackIn.push();
//            Vec3 prevAxePos = new Vec3(axe.lastTickPosX, axe.lastTickPosY, axe.lastTickPosZ);
//            Vec3 prevPlayerPos = new Vec3(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
//            Vec3 axePos = prevAxePos.add(axe.position().subtract(prevAxePos).scale(delta));
//            Vec3 playerPos = prevPlayerPos.add(player.position().subtract(prevPlayerPos).scale(delta));
//            Vec3 deltaPos = axePos.subtract(playerPos).scale(-1);
//            matrixStackIn.translate(deltaPos.x(), deltaPos.y(), deltaPos.z());
//            matrixStackIn.rotate(new Quaternion(new Vector3f(0, -1, 0), player.getYRot(), true));
//            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(TEXTURE));
//            model.setRotationAngles(axe, 0, 0, axe.tickCount + delta, 0, 0);
//            model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//            matrixStackIn.pop();
//        }
    }
}
