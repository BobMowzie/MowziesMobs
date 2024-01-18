package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderSunstrike;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderUmvuthi;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SolarFlareAbility;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class SolarFlareLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public SolarFlareLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_117346_) {
        super(p_117346_);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        SolarFlareAbility ability = (SolarFlareAbility) AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.SOLAR_FLARE_ABILITY);
        if (ability != null && ability.isUsing() && ability.getTicksInUse() > RenderUmvuthi.BURST_START_FRAME && ability.getTicksInUse() < RenderUmvuthi.BURST_START_FRAME + RenderUmvuthi.BURST_FRAME_COUNT - 1) {
            matrixStackIn.popPose();
            Quaternion quat = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            matrixStackIn.mulPose(quat);
            matrixStackIn.translate(0, 1, 0);
            matrixStackIn.scale(0.8f, 0.8f, 0.8f);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
            PoseStack.Pose matrixstack$entry = matrixStackIn.last();
            Matrix4f matrix4f = matrixstack$entry.pose();
            Matrix3f matrix3f = matrixstack$entry.normal();
            RenderUmvuthi.drawBurst(matrix4f, matrix3f, ivertexbuilder, ability.getTicksInUse() - RenderUmvuthi.BURST_START_FRAME + partialTicks, packedLightIn);
            matrixStackIn.pushPose();
        }
    }
}
