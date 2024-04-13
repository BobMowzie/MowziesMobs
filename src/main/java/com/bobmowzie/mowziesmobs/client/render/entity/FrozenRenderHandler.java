package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

/**
 * Created by BobMowzie on 6/28/2017.
 */
public enum FrozenRenderHandler {
    INSTANCE;

    private static final ResourceLocation FROZEN_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/frozen.png");

    public static class LayerFrozen<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T,M> {
        private final LivingEntityRenderer<T, M> renderer;

        public LayerFrozen(LivingEntityRenderer<T, M> renderer) {
            super(renderer);
            this.renderer = renderer;
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(living, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null && frozenCapability.getFrozen()) {
                EntityModel model = this.renderer.getModel();

                float transparency = 1;
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(FROZEN_TEXTURE));
                model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, transparency);
            }
        }
    }

    public static class GeckoLayerFrozen<T extends LivingEntity & GeoEntity> extends GeoLayerRenderer<T> {

        public GeckoLayerFrozen(IGeoRenderer<T> entityRendererIn, EntityRendererProvider.Context context) {
            super(entityRendererIn);
        }

        @Override
        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(living, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null && frozenCapability.getFrozen()) {
                RenderType renderType = RenderType.entityTranslucent(FROZEN_TEXTURE);

                getRenderer().render(getEntityModel().getModel(getEntityModel().getModelResource(living)),
                        living, partialTicks, renderType, matrixStackIn, bufferIn, bufferIn.getBuffer(renderType),
                        packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        event.getPoseStack().pushPose();

        Player player = Minecraft.getInstance().player;

        if(player != null) {
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(player, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null && frozenCapability.getFrozen()) {
                boolean isMainHand = event.getHand() == InteractionHand.MAIN_HAND;
                if (isMainHand && !player.isInvisible() && event.getItemStack().isEmpty()) {
                    HumanoidArm enumhandside = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
                    renderArmFirstPersonFrozen(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getEquipProgress(), event.getSwingProgress(), enumhandside);
                    event.setCanceled(true);
                }
            }
        }

        event.getPoseStack().popPose();
    }

    /**
     * From ItemRenderer#renderArmFirstPerson
     * @param swingProgress
     * @param equipProgress
     * @param handSide
     */
    private void renderArmFirstPersonFrozen(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, float equippedProgress, float swingProgress, HumanoidArm side) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();
        Minecraft.getInstance().getTextureManager().bindForSetup(FROZEN_TEXTURE);
        boolean flag = side != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(swingProgress * (float)Math.PI);
        matrixStackIn.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equippedProgress * -0.6F, f4 + -0.71999997F);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swingProgress * swingProgress * (float)Math.PI);
        float f6 = Mth.sin(f1 * (float)Math.PI);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayer abstractclientplayerentity = mc.player;
        mc.getTextureManager().bindForSetup(abstractclientplayerentity.getSkinTextureLocation());
        matrixStackIn.translate(f * -1.0F, 3.6F, 3.5D);
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(200.0F));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
        matrixStackIn.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerRenderer playerrenderer = (PlayerRenderer)renderManager.getRenderer(abstractclientplayerentity);
        if (flag) {
            playerrenderer.renderRightHand(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            matrixStackIn.scale(1.02f, 1.02f, 1.02f);
            this.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity, playerrenderer.getModel());
        } else {
            playerrenderer.renderLeftHand(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            matrixStackIn.scale(1.02f, 1.02f, 1.02f);
            this.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity, playerrenderer.getModel());
        }
    }

    public void renderRightArm(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, AbstractClientPlayer playerIn, PlayerModel<AbstractClientPlayer> model) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, (model).rightArm, (model).rightSleeve, model);
    }

    public void renderLeftArm(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, AbstractClientPlayer playerIn, PlayerModel<AbstractClientPlayer> model) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, (model).leftArm, (model).leftSleeve, model);
    }

    private void renderItem(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, AbstractClientPlayer playerIn, ModelPart rendererArmIn, ModelPart rendererArmwearIn, PlayerModel<AbstractClientPlayer> model) {
        this.setModelVisibilities(playerIn, model);
        model.attackTime = 0.0F;
        model.crouching = false;
        model.swimAmount = 0.0F;
        model.setupAnim(playerIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArmwearIn.xRot = 0.0F;
        rendererArmwearIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(FROZEN_TEXTURE)), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.8f);
    }

    private void setModelVisibilities(AbstractClientPlayer clientPlayer, PlayerModel<AbstractClientPlayer> playermodel) {
        if (clientPlayer.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.hat.visible = clientPlayer.isModelPartShown(PlayerModelPart.HAT);
            playermodel.jacket.visible = clientPlayer.isModelPartShown(PlayerModelPart.JACKET);
            playermodel.leftPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.rightPants.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.leftSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            playermodel.rightSleeve.visible = clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.crouching = clientPlayer.isCrouching();
        }
    }
}