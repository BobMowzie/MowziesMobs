package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Optional;

public class UmvuthanaArmorLayer extends GeoRenderLayer<EntityUmvuthana> {
    private final HumanoidModel defaultBipedModel;
    private MowzieGeckoEntity entity;

    public UmvuthanaArmorLayer(GeoRenderer<EntityUmvuthana> entityRendererIn, EntityRendererProvider.Context context) {
        super(entityRendererIn);
        defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, EntityUmvuthana entityLivingBaseIn, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferIn, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay) {
        poseStack.pushPose();
        this.entity = entityLivingBaseIn;
        GeoModel<EntityUmvuthana> model = this.renderer.getGeoModel();
        String boneName = "maskTwitcher";
        String handBoneName = "maskHand";
        Optional<GeoBone> bone = model.getBone(boneName);
        if (bone.isPresent() && !bone.get().isHidden()) {
            Matrix4f matrix4f = bone.get().getModelSpaceMatrix();
            poseStack.mulPoseMatrix(matrix4f);
            renderArmor(entityLivingBaseIn, bufferIn, poseStack, packedLightIn);
        }
        Optional<GeoBone> handBone = model.getBone(handBoneName);
        if (handBone.isPresent() && !handBone.get().isHidden()) {
            Matrix4f matrix4f = handBone.get().getModelSpaceMatrix();
            poseStack.mulPoseMatrix(matrix4f);
            renderArmor(entityLivingBaseIn, bufferIn, poseStack, packedLightIn);
        }
        poseStack.popPose();
    }

    private void renderArmor(LivingEntity entityLivingBaseIn, MultiBufferSource bufferIn, PoseStack poseStack, int packedLightIn) {
        ItemStack itemStack = entityLivingBaseIn.getItemBySlot(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem) itemStack.getItem();
            if (armoritem.getType() == ArmorItem.Type.HELMET) {
                boolean glintIn = itemStack.hasFoil();
                HumanoidModel a = defaultBipedModel;
                a = getArmorModelHook(entityLivingBaseIn, itemStack, EquipmentSlot.HEAD, a);
                String armorTexture = armoritem.getArmorTexture(itemStack, entityLivingBaseIn, EquipmentSlot.HEAD, null);
                if (armorTexture != null) {
                    VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(new ResourceLocation(armorTexture)), false, glintIn);
                    poseStack.mulPose((new Quaternionf()).rotationXYZ(0.0F, 0.0F, (float) Math.PI));
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
