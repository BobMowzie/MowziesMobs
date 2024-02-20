package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderSunstrike;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy.SolarFlareAbility;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class SolarFlareLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private GeckoRenderPlayer renderPlayerAnimated;

    public SolarFlareLayer(GeckoRenderPlayer entityRendererIn) {
        super(entityRendererIn);
        renderPlayerAnimated = entityRendererIn;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        SolarFlareAbility ability = (SolarFlareAbility) AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.SOLAR_FLARE_ABILITY);
        if (ability != null && ability.isUsing() && ability.getTicksInUse() > RenderUmvuthi.BURST_START_FRAME && ability.getTicksInUse() < RenderUmvuthi.BURST_START_FRAME + RenderUmvuthi.BURST_FRAME_COUNT - 1) {
            matrixStackIn.pushPose();
            MowzieGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone("Body");
            Vector4f vecTranslation = new Vector4f(0, 0, 0, 1);
            vecTranslation.transform(bone.getWorldSpaceXform());
            PoseStack newMatrixStack = new PoseStack();
            newMatrixStack.translate(vecTranslation.x(), vecTranslation.y(), vecTranslation.z());
            newMatrixStack.scale(0.8f, 0.8f, 0.8f);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
            PoseStack.Pose matrixstack$entry2 = newMatrixStack.last();
            Matrix4f matrix4f2 = matrixstack$entry2.pose();
            Matrix3f matrix3f = matrixstack$entry2.normal();
            RenderUmvuthi.drawBurst(matrix4f2, matrix3f, ivertexbuilder, ability.getTicksInUse() - RenderUmvuthi.BURST_START_FRAME + partialTicks, packedLightIn);
            matrixStackIn.popPose();
        }
    }
}
