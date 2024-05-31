package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelUmvuthana;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthanaArmorLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthanaSunLayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

@OnlyIn(Dist.CLIENT)
public class RenderUmvuthana extends MowzieGeoEntityRenderer<EntityUmvuthana> {
    public RenderUmvuthana(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelUmvuthana());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addRenderLayer(new GeckoSunblockLayer<>(this, renderManager));
        this.addRenderLayer(new UmvuthanaArmorLayer(this, renderManager));
        this.addRenderLayer(new UmvuthanaSunLayer(this, renderManager));
        this.shadowRadius = 0.6f;
    }

    @Override
    public void render(EntityUmvuthana entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        MowzieGeoBone head = getMowzieGeoModel().getMowzieBone("head");
        Vector3d worldPos = head.getWorldPosition();
        if (entity.headPos != null && entity.headPos.length > 0)
            entity.headPos[0] = new Vec3(worldPos.x, worldPos.y, worldPos.z);

        if (!Minecraft.getInstance().isPaused()) {
            MowzieGeoBone mask = getMowzieGeoModel().getMowzieBone("maskTwitcher");
            entity.updateRattleSound(mask.getRotZ());
        }
    }

    @Override
    public boolean shouldRender(EntityUmvuthana entity, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        boolean result = super.shouldRender(entity, p_114492_, p_114493_, p_114494_, p_114495_);
        if (!result) entity.headPos[0] = entity.position().add(0, entity.getEyeHeight(), 0);
        return result;
    }
}
