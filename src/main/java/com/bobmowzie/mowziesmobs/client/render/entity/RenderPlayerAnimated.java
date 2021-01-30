package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPlayerAnimated;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class RenderPlayerAnimated extends PlayerRenderer {
    public RenderPlayerAnimated(EntityRendererManager renderManager, boolean useSmallArms) {
        super(renderManager, useSmallArms);
        this.layerRenderers.clear();
        this.addLayer(new BipedArmorLayer<>(this, new ModelBipedAnimated(0.5F), new ModelBipedAnimated(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new Deadmau5HeadLayer(this));
        this.addLayer(new CapeLayer(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new ParrotVariantLayer<>(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));

        this.entityModel = new ModelPlayerAnimated(0.0f, useSmallArms);
    }

    @Override
    public void doRender(AbstractClientPlayerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!entity.isUser() || this.renderManager.info.getRenderViewEntity() == entity) {
            double d0 = y;
            if (entity.shouldRenderSneaking()) {
                d0 = y - 0.125D;
            }

            this.setModelVisibilities(entity);
            GlStateManager.setProfile(GlStateManager.Profile.PLAYER_SKIN);
            doRenderLivingRenderer(entity, x, d0, z, entityYaw, partialTicks);
            GlStateManager.unsetProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
    }

    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {
        PlayerModel<AbstractClientPlayerEntity> playermodel = this.getEntityModel();
        if (clientPlayer.isSpectator()) {
            playermodel.setVisible(false);
            playermodel.bipedHead.showModel = true;
            playermodel.bipedHeadwear.showModel = true;
        } else {
            ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            playermodel.setVisible(true);
            playermodel.bipedHeadwear.showModel = clientPlayer.isWearing(PlayerModelPart.HAT);
            playermodel.bipedBodyWear.showModel = clientPlayer.isWearing(PlayerModelPart.JACKET);
            playermodel.bipedLeftLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.bipedRightLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.bipedLeftArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_SLEEVE);
            playermodel.bipedRightArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.isSneak = clientPlayer.shouldRenderSneaking();
//            BipedModel.ArmPose bipedmodel$armpose = this.func_217766_a(clientPlayer, itemstack, itemstack1, Hand.MAIN_HAND);
//            BipedModel.ArmPose bipedmodel$armpose1 = this.func_217766_a(clientPlayer, itemstack, itemstack1, Hand.OFF_HAND);
//            if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
//                playermodel.rightArmPose = bipedmodel$armpose;
//                playermodel.leftArmPose = bipedmodel$armpose1;
//            } else {
//                playermodel.rightArmPose = bipedmodel$armpose1;
//                playermodel.leftArmPose = bipedmodel$armpose;
//            }
        }

    }

    public void doRenderLivingRenderer(AbstractClientPlayerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(entity, this, partialTicks, x, y, z))) return;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.entityModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        boolean shouldSit = entity.isPassenger() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.entityModel.isSitting = shouldSit;
        this.entityModel.isChild = entity.isChild();

        try {
            float f = MathHelper.func_219805_h(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
            float f1 = MathHelper.func_219805_h(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead);
            float f2 = f1 - f;
            if (shouldSit && entity.getRidingEntity() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity.getRidingEntity();
                f = MathHelper.func_219805_h(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }

                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }

                f2 = f1 - f;
            }

            float f7 = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
            this.renderLivingAt(entity, x, y, z);
            float f8 = this.handleRotationFloat(entity, partialTicks);
            this.applyRotations(entity, f8, f, partialTicks);
            float f4 = this.prepareScale(entity, partialTicks);
            float f5 = 0.0F;
            float f6 = 0.0F;
            if (!entity.isPassenger() && entity.isAlive()) {
                f5 = MathHelper.lerp(partialTicks, entity.prevLimbSwingAmount, entity.limbSwingAmount);
                f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
                if (entity.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }
            }

            GlStateManager.enableAlphaTest();
            this.entityModel.setLivingAnimations(entity, f6, f5, partialTicks);
            this.entityModel.setRotationAngles(entity, f6, f5, f8, f2, f7, f4);
            if (this.renderOutlines) {
                boolean flag = this.setScoreTeamColor(entity);
                GlStateManager.enableColorMaterial();
                GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
                if (!this.renderMarker) {
                    this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                }

                if (!entity.isSpectator()) {
                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }

                GlStateManager.tearDownSolidRenderingTextureCombine();
                GlStateManager.disableColorMaterial();
                if (flag) {
                    this.unsetScoreTeamColor();
                }
            } else {
                boolean flag1 = this.setDoRenderBrightness(entity, partialTicks);
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                if (flag1) {
                    this.unsetBrightness();
                }

                GlStateManager.depthMask(true);
                if (!entity.isSpectator()) {
                    this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                }
            }

            GlStateManager.disableRescaleNormal();
        } catch (Exception exception) {

        }

        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.enableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        doRenderEntityRenderer(entity, x, y, z, entityYaw, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(entity, this, partialTicks, x, y, z));
    }

    public void doRenderEntityRenderer(AbstractClientPlayerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            this.renderName(entity, x, y, z);
        }
    }
}
