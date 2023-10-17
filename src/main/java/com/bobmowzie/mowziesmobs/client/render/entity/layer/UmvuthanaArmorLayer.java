package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.Optional;

public class UmvuthanaArmorLayer extends GeoLayerRenderer<EntityUmvuthana> {
    private final HumanoidModel defaultBipedModel;

    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();
    private MowzieGeckoEntity entity;

    public UmvuthanaArmorLayer(IGeoRenderer<EntityUmvuthana> entityRendererIn, EntityRendererProvider.Context context) {
        super(entityRendererIn);
        defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityUmvuthana entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.entity = entityLivingBaseIn;
        GeoModel model = this.entityRenderer.getGeoModelProvider().getModel(this.entityRenderer.getGeoModelProvider().getModelLocation(entity));
        String boneName = "maskTwitcher";
        String handBoneName = "maskHand";
        Optional<GeoBone> bone = model.getBone(boneName);
        if (bone.isPresent() && !bone.get().isHidden()) {
            Matrix4f matrix4f = bone.get().getModelSpaceXform();
            poseStack.mulPoseMatrix(matrix4f);
            renderArmor(entityLivingBaseIn, bufferIn, poseStack, packedLightIn);
        }
        Optional<GeoBone> handBone = model.getBone(handBoneName);
        if (handBone.isPresent() && !handBone.get().isHidden()) {
            Matrix4f matrix4f = handBone.get().getModelSpaceXform();
            poseStack.mulPoseMatrix(matrix4f);
            renderArmor(entityLivingBaseIn, bufferIn, poseStack, packedLightIn);
        }
    }

    private void renderArmor(LivingEntity entityLivingBaseIn, MultiBufferSource bufferIn, PoseStack poseStack, int packedLightIn) {
        ItemStack itemStack = entityLivingBaseIn.getItemBySlot(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem) itemStack.getItem();
            if (armoritem.getSlot() == EquipmentSlot.HEAD) {
                boolean glintIn = itemStack.hasFoil();
                HumanoidModel a = defaultBipedModel;
                a = getArmorModelHook(entityLivingBaseIn, itemStack, EquipmentSlot.HEAD, a);
                String armorTexture = armoritem.getArmorTexture(itemStack, entityLivingBaseIn, EquipmentSlot.HEAD, null);
                if (armorTexture != null) {
                    VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(new ResourceLocation(armorTexture)), false, glintIn);
                    poseStack.mulPose(Quaternion.fromXYZ(0.0F, 0.0F, (float) Math.PI));
                    poseStack.scale(1.111f, 1.111f, 1.111f);
                    poseStack.translate(0, 0.25, 0.15);
                    a.renderToBuffer(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    protected HumanoidModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model) {
        Model basicModel = net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
        return basicModel instanceof HumanoidModel ? (HumanoidModel<?>) basicModel : model;
    }
}
