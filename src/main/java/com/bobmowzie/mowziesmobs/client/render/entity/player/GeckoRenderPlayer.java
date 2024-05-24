package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPlayerAnimated;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.joml.*;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.lang.Math;
import java.util.HashMap;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class GeckoRenderPlayer extends PlayerRenderer implements GeoRenderer<GeckoPlayer> {

    public MultiBufferSource rtb;
    private static HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> modelsToLoad = new HashMap<>();
    private ModelGeckoPlayerThirdPerson geoModel;

    private Matrix4f worldRenderMat;

    public Vec3 betweenHandsPos;
    public Vec3 particleEmitterRoot;

    private boolean isInvisible = false;

    private GeckoPlayer animatable;

    public GeckoRenderPlayer(EntityRendererProvider.Context context, boolean slim, ModelGeckoPlayerThirdPerson geoModel) {
        super(context, slim);

        ModelPlayerAnimated<AbstractClientPlayer> modelPlayerAnimated = new ModelPlayerAnimated<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim);
        ModelPlayerAnimated.setUseMatrixMode(modelPlayerAnimated, true);
        this.model = modelPlayerAnimated;

        this.layers.clear();
        this.addLayer(new GeckoArmorLayer<>(this, new ModelBipedAnimated<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new ModelBipedAnimated<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new GeckoPlayerItemInHandLayer(this));
        this.addLayer(new ArrowLayer<>(context, this));
        this.addLayer(new Deadmau5EarsLayer(this));
        this.addLayer(new GeckoCapeLayer(this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new GeckoElytraLayer(this, context.getModelSet()));
        this.addLayer(new GeckoParrotOnShoulderLayer(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
        this.addLayer(new FrozenRenderHandler.LayerFrozen<>(this));
        this.addLayer(new SolarFlareLayer(this));
        this.addLayer(new SunblockLayer<>(this));


        this.geoModel = geoModel;
        this.geoModel.setUseSmallArms(slim);

        worldRenderMat = new Matrix4f();
        worldRenderMat.identity();
    }

    public ModelGeckoPlayerThirdPerson getGeckoModel() {
        return geoModel;
    }

    public HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> getModelsToLoad() {
        return modelsToLoad;
    }

    public void render(AbstractClientPlayer entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
        this.rtb = bufferIn;
        this.setModelVisibilities(entityIn);
        renderLiving(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn, geckoPlayer);
    }

    private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
        ModelGeckoPlayerThirdPerson playermodel = getGeckoModel();
        if (playermodel.isInitialized()) {
            if (clientPlayer.isSpectator()) {
                playermodel.setVisible(false);
                playermodel.bipedHead().setHidden(false);
                playermodel.bipedHeadwear().setHidden(false);
            } else {
                playermodel.setVisible(true);
                playermodel.bipedHeadwear().setHidden(!clientPlayer.isModelPartShown(PlayerModelPart.HAT));
                playermodel.bipedBodywear().setHidden(!clientPlayer.isModelPartShown(PlayerModelPart.JACKET));
                playermodel.bipedLeftLegwear().setHidden(!clientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG));
                playermodel.bipedRightLegwear().setHidden(!clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG));
                playermodel.bipedLeftArmwear().setHidden(!clientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
                playermodel.bipedRightArmwear().setHidden(!clientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
                playermodel.isSneak = clientPlayer.isCrouching();
                HumanoidModel.ArmPose bipedmodel$armpose = getArmPose(clientPlayer, InteractionHand.MAIN_HAND);
                HumanoidModel.ArmPose bipedmodel$armpose1 = getArmPose(clientPlayer, InteractionHand.OFF_HAND);
                if (bipedmodel$armpose.isTwoHanded()) {
                    bipedmodel$armpose1 = clientPlayer.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
                }

                if (clientPlayer.getMainArm() == HumanoidArm.RIGHT) {
                    geoModel.rightArmPose = bipedmodel$armpose;
                    geoModel.leftArmPose = bipedmodel$armpose1;
                } else {
                    geoModel.rightArmPose = bipedmodel$armpose1;
                    geoModel.leftArmPose = bipedmodel$armpose;
                }
            }
        }
    }

    public void renderLiving(AbstractClientPlayer entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
        this.animatable = geckoPlayer;

        matrixStackIn.pushPose();

        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(entityIn);
        boolean flag1 = !flag && !entityIn.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entityIn);
        this.isInvisible = !flag && !flag1 && !flag2;
        RenderType rendertype = this.getRenderType(entityIn, flag, flag1, flag2);
        if (this.isInvisible) {
            rendertype = this.model.renderType(getTextureLocation(geckoPlayer));
        }
        if (rendertype != null) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            defaultRender(matrixStackIn, animatable, bufferIn, rendertype, ivertexbuilder, 0, partialTicks, packedLightIn);
        }

        matrixStackIn.popPose();
        renderEntity(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public int getPackedOverlay(GeckoPlayer animatable, float u, float partialTick) {
        AbstractClientPlayer player = (AbstractClientPlayer) animatable.getPlayer();
        return getOverlayCoords(player, this.getWhiteOverlayProgress(player, partialTick));
    }

    @Override
    public void actuallyRender(PoseStack poseStack, GeckoPlayer animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        AbstractClientPlayer entity = (AbstractClientPlayer) animatable.getPlayer();
        this.model.attackTime = this.getAttackAnim(entity, partialTick);

        boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = entity.isBaby();
        float f_lerpBodyRot = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        float f1_lerpHeadRot = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
        float f2_netHeadYaw = f1_lerpHeadRot - f_lerpBodyRot;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity.getVehicle();
            f_lerpBodyRot = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2_netHeadYaw = f1_lerpHeadRot - f_lerpBodyRot;
            float f3 = Mth.wrapDegrees(f2_netHeadYaw);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f_lerpBodyRot = f1_lerpHeadRot - f3;
            if (f3 * f3 > 2500.0F) {
                f_lerpBodyRot += f3 * 0.2F;
            }

            f2_netHeadYaw = f1_lerpHeadRot - f_lerpBodyRot;
        }

        float f6 = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        if (isEntityUpsideDown(entity)) {
            f6 *= -1.0F;
            f2_netHeadYaw *= -1.0F;
        }

        if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                float f4 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float)(-direction.getStepX()) * f4, 0.0F, (float)(-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(entity, partialTick);
//        this.setupRotations(entity, poseStack, f7, f_lerpBodyRot, partialTick);   This is where the vanilla function gets called. We move it lower down after animations are handled
        this.scale(entity, poseStack, partialTick);
        float f8_limbSwingAmount = 0.0F;
        float f5_limbSwing = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8_limbSwingAmount = entity.walkAnimation.speed(partialTick);
            f5_limbSwing = entity.walkAnimation.position(partialTick);
            if (entity.isBaby()) {
                f5_limbSwing *= 3.0F;
            }

            if (f8_limbSwingAmount > 1.0F) {
                f8_limbSwingAmount = 1.0F;
            }
        }

        if (!isReRender) {
            float headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
            float motionThreshold = getMotionAnimThreshold(animatable);
            Vec3 velocity = entity.getDeltaMovement();
            float avgVelocity = (float)(Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f;
            AnimationState<GeckoPlayer> animationState = new AnimationState<GeckoPlayer>(animatable, f5_limbSwing, f8_limbSwingAmount, partialTick, avgVelocity >= motionThreshold && f8_limbSwingAmount != 0);
            long instanceId = getInstanceId(animatable);

            animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
            animationState.setData(DataTickets.ENTITY, entity);
            animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, entity.isBaby(), -f2_netHeadYaw, -headPitch));
            this.getGeckoModel().addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.getGeckoModel().handleAnimations(animatable, instanceId, animationState);
        }

        if (this.geoModel.isInitialized()) {
            this.setupRotations(entity, poseStack, f7, f_lerpBodyRot, partialTick, f1_lerpHeadRot);
            float bodyRotateAmount = this.geoModel.getControllerValueInverted("BodyRotateController");
            this.geoModel.setRotationAngles(entity, f5_limbSwing, f8_limbSwingAmount, f7, Mth.rotLerp(bodyRotateAmount, 0, f2_netHeadYaw), f6, partialTick);

            MowzieGeoBone leftHeldItem = geoModel.getMowzieBone("LeftHeldItem");
            MowzieGeoBone rightHeldItem = geoModel.getMowzieBone("RightHeldItem");

            Matrix4f worldMatInverted = new Matrix4f(poseStack.last().pose());
            worldMatInverted.invert();
            Matrix3f worldNormInverted = new Matrix3f(poseStack.last().normal());
            worldNormInverted.invert();
            PoseStack toWorldSpace = new PoseStack();
            toWorldSpace.mulPose(MathUtils.quatFromRotationXYZ(0, -f_lerpBodyRot + 180, 0, true));
            toWorldSpace.translate(0, -1.5f, 0);
            toWorldSpace.last().normal().mul(worldNormInverted);
            toWorldSpace.last().pose().mul(worldMatInverted);

            Vector4f leftHeldItemPos = new Vector4f(0, 0, 0, 1);
            leftHeldItemPos.mul(leftHeldItem.getWorldSpaceMatrix());
            leftHeldItemPos.mul(toWorldSpace.last().pose());
            Vec3 leftHeldItemPos3 = new Vec3(leftHeldItemPos.x(), leftHeldItemPos.y(), leftHeldItemPos.z());

            Vector4f rightHeldItemPos = new Vector4f(0, 0, 0, 1);
            rightHeldItemPos.mul(rightHeldItem.getWorldSpaceMatrix());
            rightHeldItemPos.mul(toWorldSpace.last().pose());
            Vec3 rightHeldItemPos3 = new Vec3(rightHeldItemPos.x(), rightHeldItemPos.y(), rightHeldItemPos.z());

            betweenHandsPos = rightHeldItemPos3.add(leftHeldItemPos3.subtract(rightHeldItemPos3).scale(0.5));

            MowzieGeoBone particleEmitterRootBone = geoModel.getMowzieBone("ParticleEmitterRoot");
            Vector4f emitterRootPos = new Vector4f(0, 0, 0, 1);
            emitterRootPos.mul(particleEmitterRootBone.getWorldSpaceMatrix());
            emitterRootPos.mul(toWorldSpace.last().pose());
            particleEmitterRoot = new Vec3(emitterRootPos.x(), emitterRootPos.y(), emitterRootPos.z());
        }

        poseStack.translate(0, 0.01f, 0);

//        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (!entity.isInvisibleTo(Minecraft.getInstance().player))
            GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        ModelPlayerAnimated.copyFromGeckoModel(this.model, geoModel);

        if (!entity.isSpectator()) {
            for(RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> layerrenderer : this.layers) {
                layerrenderer.render(poseStack, bufferSource, packedLight, entity, f5_limbSwing, f8_limbSwingAmount, partialTick, f7, f2_netHeadYaw, f6);
            }
        }
    }

    public void renderEntity(AbstractClientPlayer entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        net.minecraftforge.client.event.RenderNameTagEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameTagEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(entityIn))) {
            this.renderNameTag(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
    }

    protected void setupRotations(AbstractClientPlayer entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        float f = entityLiving.getSwimAmount(partialTicks);
        if (entityLiving.isFallFlying()) {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f1 = (float)entityLiving.getFallFlyingTicks() + partialTicks;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isAutoSpinAttack()) {
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - entityLiving.getXRot())));
            }

            Vec3 vector3d = entityLiving.getViewVector(partialTicks);
            Vec3 vector3d1 = entityLiving.getDeltaMovement();
            double d0 = vector3d1.horizontalDistanceSqr();
            double d1 = vector3d.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                matrixStackIn.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            float swimController = this.geoModel.getControllerValueInverted("SwimController");
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f3 = entityLiving.isInWater() || entityLiving.isInFluidType((fluidType, height) -> entityLiving.canSwimInFluidType(fluidType)) ? -90.0F - entityLiving.getXRot() : -90.0F;
            float f4 = Mth.lerp(f, 0.0F, f3) * swimController;
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(f4));
            if (entityLiving.isVisuallySwimming()) {
                matrixStackIn.translate(0.0D, -1.0D, (double)0.3F);
            }
        } else {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
        }
    }

    protected void applyRotationsLivingRenderer(AbstractClientPlayer entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        if (this.isShaking(entityLiving)) {
            rotationYaw += (float)(Math.cos((double)entityLiving.tickCount * 3.25D) * Math.PI * (double)0.4F);
        }

        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            float bodyRotateAmount = this.geoModel.getControllerValueInverted("BodyRotateController");
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - Mth.rotLerp(bodyRotateAmount, headYaw, rotationYaw)));
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entityLiving)));
        } else if (entityLiving.isAutoSpinAttack()) {
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F - entityLiving.getXRot()));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(((float)entityLiving.tickCount + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedOrientation();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(this.getFlipDegrees(entityLiving)));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (isEntityUpsideDown(entityLiving)) {
            matrixStackIn.translate(0.0F, entityLiving.getBbHeight() + 0.1F, 0.0F);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    private static float getFacingAngle(Direction facingIn) {
        switch(facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    @Override
    public GeoModel<GeckoPlayer> getGeoModel() {
        return this.geoModel;
    }

    @Override
    public GeckoPlayer getAnimatable() {
        return animatable;
    }

    public ModelGeckoPlayerThirdPerson getAnimatedPlayerModel() {
        return this.geoModel;
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoPlayer geckoPlayer) {
        return getTextureLocation((AbstractClientPlayer) geckoPlayer.getPlayer());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, GeckoPlayer animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        // Record xform matrices for relevant bones
        if (bone instanceof MowzieGeoBone) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone)bone;
            if (
                    mowzieBone.getName().equals("LeftHeldItem") || mowzieBone.getName().equals("RightHeldItem") ||
                    mowzieBone.getName().equals("Head") ||
                    mowzieBone.getName().equals("Body") ||
                    mowzieBone.getName().equals("BodyLayer") ||
                    mowzieBone.getName().equals("LeftArm") ||
                    mowzieBone.getName().equals("RightArm") ||
                    mowzieBone.getName().equals("RightLeg") ||
                    mowzieBone.getName().equals("LeftLeg") ||
                    mowzieBone.getName().equals("ParticleEmitterRoot")
            ) {
                poseStack.pushPose();
                if (!mowzieBone.getName().equals("LeftHeldItem") && !mowzieBone.getName().equals("RightHeldItem")) {
                    poseStack.scale(-1.0F, -1.0F, 1.0F);
                }
                if (mowzieBone.getName().equals("Body")) {
                    poseStack.translate(0, -0.75, 0);
                }
                if (mowzieBone.getName().equals("LeftArm")) {
                    poseStack.translate(-0.075, 0, 0);
                }
                if (mowzieBone.getName().equals("RightArm")) {
                    poseStack.translate(0.075, 0, 0);
                }
                PoseStack.Pose entry = poseStack.last();
                mowzieBone.setWorldSpaceNormal(new Matrix3f(entry.normal()));
                mowzieBone.setWorldSpaceMatrix(new Matrix4f(entry.pose()));
                poseStack.popPose();
            }
        }
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (!isReRender)
            applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();

        for(RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> layerrenderer : this.layers) {
            if (layerrenderer instanceof IGeckoRenderLayer) ((IGeckoRenderLayer)layerrenderer).renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    /**
     * Update the current frame of a {@link AnimatableTexture potentially animated} texture used by this GeoRenderer.<br>
     * This should only be called immediately prior to rendering, and only
     * @see AnimatableTexture#setAndUpdate
     */
    @Override
    public void updateAnimatedTextureFrame(GeckoPlayer animatable) {
        AnimatableTexture.setAndUpdate(getTextureLocation(animatable));
    }

    /**
     * Create and fire the relevant {@code CompileLayers} event hook for this renderer
     */
    @Override
    public void fireCompileRenderLayersEvent() {
    }

    /**
     * Create and fire the relevant {@code Pre-Render} event hook for this renderer.<br>
     * @return Whether the renderer should proceed based on the cancellation state of the event
     */
    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return true;
    }

    /**
     * Create and fire the relevant {@code Post-Render} event hook for this renderer
     */
    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
    }
}
