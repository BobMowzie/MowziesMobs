package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by BobMowzie on 6/28/2017.
 */
public enum FrozenRenderHandler {
    INSTANCE;

    private static final ResourceLocation FROZEN_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/frozen.png");

    public static class LayerFrozen<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T,M> {
        private final LivingRenderer<T, M> renderer;

        public LayerFrozen(LivingRenderer<T, M> renderer) {
            super(renderer);
            this.renderer = renderer;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (living.isPotionActive(PotionHandler.FROZEN)) {
                EntityModel model = this.renderer.getEntityModel();
//                Map<ModelRenderer, Boolean> visibilities = new HashMap<>();
//                for(ModelRenderer box : model.boxList) {
//                    if(this.modelExclusions.test(box)) {
//                        visibilities.put(box, box.showModel);
//                        box.showModel = false;
//                    }
//                } TODO

                float transparency = 1;
                IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(FROZEN_TEXTURE));
                model.render(matrixStackIn, ivertexbuilder, packedLightIn, 0, 1, 1, 1, transparency);

//                for(Map.Entry<ModelRenderer, Boolean> entry : visibilities.entrySet()) {
//                    entry.getKey().showModel = entry.getValue();
//                } TODO


            }
        }

//        @Override
//        public boolean shouldCombineTextures() {
//            return false;
//        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        event.getMatrixStack().push();

        PlayerEntity player = Minecraft.getInstance().player;

        if(player != null && player.isPotionActive(PotionHandler.FROZEN)) {
            if(player.isPotionActive(PotionHandler.FROZEN)) {
                boolean isMainHand = event.getHand() == Hand.MAIN_HAND;
                if(isMainHand && !player.isInvisible() && event.getItemStack().isEmpty()) {
                    HandSide enumhandside = isMainHand ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
                    renderArmFirstPersonFrozen(event.getMatrixStack(), event.getBuffers(), event.getLight(), event.getEquipProgress(), event.getSwingProgress(), enumhandside);
                    event.setCanceled(true);
                }
            }
        }

        event.getMatrixStack().pop();
    }

    /**
     * From ItemRenderer#renderArmFirstPerson
     * @param swingProgress
     * @param equipProgress
     * @param handSide
     */
    private void renderArmFirstPersonFrozen(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, float equippedProgress, float swingProgress, HandSide side) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager renderManager = mc.getRenderManager();
        Minecraft.getInstance().getTextureManager().bindTexture(FROZEN_TEXTURE);
        boolean flag = side != HandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float)Math.PI);
        matrixStackIn.translate((double)(f * (f2 + 0.64000005F)), (double)(f3 + -0.6F + equippedProgress * -0.6F), (double)(f4 + -0.71999997F));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayerEntity abstractclientplayerentity = mc.player;
        mc.getTextureManager().bindTexture(abstractclientplayerentity.getLocationSkin());
        matrixStackIn.translate((double)(f * -1.0F), (double)3.6F, 3.5D);
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * 120.0F));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(200.0F));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f * -135.0F));
        matrixStackIn.translate((double)(f * 5.6F), 0.0D, 0.0D);
        PlayerRenderer playerrenderer = (PlayerRenderer)renderManager.<AbstractClientPlayerEntity>getRenderer(abstractclientplayerentity);
        if (flag) {
            playerrenderer.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            matrixStackIn.scale(1.02f, 1.02f, 1.02f);
            this.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity, playerrenderer.getEntityModel());
        } else {
            playerrenderer.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            matrixStackIn.scale(1.02f, 1.02f, 1.02f);
            this.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity, playerrenderer.getEntityModel());
        }
    }

    public void renderRightArm(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, PlayerModel<AbstractClientPlayerEntity> model) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, (model).bipedRightArm, (model).bipedRightArmwear, model);
    }

    public void renderLeftArm(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, PlayerModel<AbstractClientPlayerEntity> model) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, (model).bipedLeftArm, (model).bipedLeftArmwear, model);
    }

    private void renderItem(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, ModelRenderer rendererArmIn, ModelRenderer rendererArmwearIn, PlayerModel<AbstractClientPlayerEntity> model) {
        this.setModelVisibilities(playerIn, model);
        model.swingProgress = 0.0F;
        model.isSneak = false;
        model.swimAnimation = 0.0F;
        model.setRotationAngles(playerIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArmwearIn.rotateAngleX = 0.0F;
        rendererArmwearIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(FROZEN_TEXTURE)), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.5f);
    }

    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer, PlayerModel<AbstractClientPlayerEntity> playermodel) {
        if (clientPlayer.isSpectator()) {
            playermodel.setVisible(false);
            playermodel.bipedHead.showModel = true;
            playermodel.bipedHeadwear.showModel = true;
        } else {
            playermodel.setVisible(true);
            playermodel.bipedHeadwear.showModel = clientPlayer.isWearing(PlayerModelPart.HAT);
            playermodel.bipedBodyWear.showModel = clientPlayer.isWearing(PlayerModelPart.JACKET);
            playermodel.bipedLeftLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.bipedRightLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.bipedLeftArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_SLEEVE);
            playermodel.bipedRightArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.isSneak = clientPlayer.isCrouching();
        }
    }
}