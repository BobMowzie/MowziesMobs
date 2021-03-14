package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
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
                    renderArmFirstPersonFrozen(event.getMatrixStack(), event.getBuffers(), event.getLight(), enumhandside);
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
    private void renderArmFirstPersonFrozen(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, HandSide side) {
        Minecraft.getInstance().getTextureManager().bindTexture(FROZEN_TEXTURE);
        AbstractClientPlayerEntity player = Minecraft.getInstance().player;
        PlayerRenderer playerrenderer = (PlayerRenderer)Minecraft.getInstance().getRenderManager().<AbstractClientPlayerEntity>getRenderer(player);
        matrixStackIn.push();
        float f = side == HandSide.RIGHT ? 1.0F : -1.0F;
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(92.0F));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(45.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * -41.0F));
        matrixStackIn.translate((double)(f * 0.3F), (double)-1.1F, (double)0.45F);
        if (side == HandSide.RIGHT) {
            playerrenderer.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, player);
        } else {
            playerrenderer.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, player);
        }

        matrixStackIn.pop();
        /*Minecraft mc = Minecraft.getInstance();
        EntityRendererManager renderManager = mc.getRenderManager();
        boolean flag = handSide != HandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(equipProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(equipProgress * (float)Math.PI);
        GlStateManager.translatef(f * (f2 + 0.64000005F), f3 + -0.6F + swingProgress * -0.6F, f4 + -0.71999997F);
        GlStateManager.rotatef(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(equipProgress * equipProgress * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        GlStateManager.rotatef(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayerEntity abstractclientplayer = mc.player;
        mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translatef(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotatef(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(f * 5.6F, 0.0F, 0.0F);
        PlayerRenderer renderplayer = (PlayerRenderer) (EntityRenderer<?>) renderManager.getRenderer(abstractclientplayer);
        GlStateManager.disableCull();

        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);

            mc.getTextureManager().bindTexture(FROZEN_TEXTURE);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float transparency = 1;
            GlStateManager.color4f(1, 1, 1, transparency);

            //From RenderPlayer#renderRightArm
            PlayerModel modelplayer = renderplayer.getEntityModel();
            GlStateManager.enableBlend();
            modelplayer.swingProgress = 0.0F;
            modelplayer.isSneak = false;
            modelplayer.setRotationAngles(abstractclientplayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            modelplayer.bipedRightArm.rotateAngleX = 0.0F;
            modelplayer.bipedRightArm.render(0.0625F);
            modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
            modelplayer.bipedRightArmwear.render(0.0625F);
            GlStateManager.disableBlend();
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);

            mc.getTextureManager().bindTexture(FROZEN_TEXTURE);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float transparency = 1;
            GlStateManager.color4f(1, 1, 1, transparency);

            //From RenderPlayer#renderLeftArm
            PlayerModel modelplayer = renderplayer.getEntityModel();
            GlStateManager.enableBlend();
            modelplayer.isSneak = false;
            modelplayer.swingProgress = 0.0F;
            modelplayer.setRotationAngles(abstractclientplayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
            modelplayer.bipedLeftArm.render(0.0625F);
            modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
            modelplayer.bipedLeftArmwear.render(0.0625F);
            GlStateManager.disableBlend();
        }

        GlStateManager.color4f(1, 1, 1, 1);

        GlStateManager.enableCull();*/ // TODO
    }
}