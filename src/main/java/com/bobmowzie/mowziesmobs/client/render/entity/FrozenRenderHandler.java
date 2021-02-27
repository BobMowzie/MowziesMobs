package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Josh on 6/28/2017.
 */
public enum FrozenRenderHandler {
    INSTANCE;

    private static final ResourceLocation FROZEN_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/frozen.png");

    public static class LayerFrozen extends LayerRenderer<LivingEntity, EntityModel<LivingEntity>> {
        private final LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer;
        private final Predicate<RendererModel> modelExclusions;

        public LayerFrozen(LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, Predicate<RendererModel> modelExclusions) {
            super(renderer);
            this.renderer = renderer;
            this.modelExclusions = modelExclusions;
        }

        public LayerFrozen(LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
            this(renderer, box -> {
//                if(renderer instanceof RenderPlayer) {
//                    RenderPlayer renderPlayer = (RenderPlayer) renderer;
//                    ModelPlayer playerModel = renderPlayer.getMainModel();
//                    return box == playerModel.bipedHeadwear || box == playerModel.bipedRightLegwear ||
//                            box == playerModel.bipedLeftLegwear || box == playerModel.bipedBodyWear ||
//                            box == playerModel.bipedRightArmwear || box == playerModel.bipedLeftArmwear;
//                }
                return false;
            });
        }

        @Override
        public void render(LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (living.isPotionActive(PotionHandler.FROZEN)) {
                EntityModel model = this.renderer.getEntityModel();
                Map<RendererModel, Boolean> visibilities = new HashMap<>();
                for(RendererModel box : model.boxList) {
                    if(this.modelExclusions.test(box)) {
                        visibilities.put(box, box.showModel);
                        box.showModel = false;
                    }
                }

                //Render decay overlay
                float transparency = 1;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.renderer.bindTexture(FROZEN_TEXTURE);
                GlStateManager.color4f(1, 1, 1, transparency);
                model.render(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.color4f(1, 1, 1, 1);

                for(Map.Entry<RendererModel, Boolean> entry : visibilities.entrySet()) {
                    entry.getKey().showModel = entry.getValue();
                }


            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderSpecificHandEvent event) {
        GlStateManager.pushMatrix();

        PlayerEntity player = Minecraft.getInstance().player;

        if(player != null && player.isPotionActive(PotionHandler.FROZEN)) {
            if(player.isPotionActive(PotionHandler.FROZEN)) {
                boolean isMainHand = event.getHand() == Hand.MAIN_HAND;
                if(isMainHand && !player.isInvisible() && event.getItemStack().isEmpty()) {
                    HandSide enumhandside = isMainHand ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
                    renderArmFirstPersonFrozen(event.getEquipProgress(), event.getSwingProgress(), enumhandside);
                    event.setCanceled(true);
                }
            }
        }

        GlStateManager.popMatrix();
    }

    /**
     * From ItemRenderer#renderArmFirstPerson
     * @param swingProgress
     * @param equipProgress
     * @param handSide
     */
    private void renderArmFirstPersonFrozen(float swingProgress, float equipProgress, HandSide handSide) {
        Minecraft mc = Minecraft.getInstance();
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

        GlStateManager.enableCull();
    }
}