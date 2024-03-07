package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPlayerAnimated;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.HashMap;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class GeckoRenderPlayer extends PlayerRenderer implements IGeoRenderer<GeckoPlayer> {

    public MultiBufferSource rtb;
    private static HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> modelsToLoad = new HashMap<>();
    private ModelGeckoPlayerThirdPerson modelProvider;

    private Matrix4f worldRenderMat;

    public Vec3 betweenHandsPos;
    public Vec3 particleEmitterRoot;

    private boolean isInvisible = false;

    public GeckoRenderPlayer(EntityRendererProvider.Context context, boolean slim, ModelGeckoPlayerThirdPerson modelProvider) {
        super(context, slim);

        ModelPlayerAnimated<AbstractClientPlayer> modelPlayerAnimated = new ModelPlayerAnimated<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim);
        ModelPlayerAnimated.setUseMatrixMode(modelPlayerAnimated, true);
        this.model = modelPlayerAnimated;

        this.layers.clear();
        this.addLayer(new GeckoArmorLayer<>(this, new ModelBipedAnimated<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new ModelBipedAnimated<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR))));
        this.addLayer(new GeckoPlayerItemInHandLayer(this));
        this.addLayer(new ArrowLayer<>(context, this));
        this.addLayer(new Deadmau5EarsLayer(this));
        this.addLayer(new GeckoCapeLayer(this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
        this.addLayer(new GeckoElytraLayer(this, context.getModelSet()));
        this.addLayer(new GeckoParrotOnShoulderLayer(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this));
        this.addLayer(new FrozenRenderHandler.LayerFrozen<>(this));
        this.addLayer(new SolarFlareLayer(this));


        this.modelProvider = modelProvider;
        this.modelProvider.setUseSmallArms(slim);

        worldRenderMat = new Matrix4f();
        worldRenderMat.setIdentity();
    }

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof GeckoPlayer.GeckoPlayerThirdPerson) {
                GeckoRenderPlayer render = modelsToLoad.get(object.getClass());
                return (IAnimatableModel<Object>) render.getGeoModelProvider();
            } else {
                return null;
            }
        });
    }

    public GeckoRenderPlayer getModelProvider(Class<? extends GeckoPlayer> animatable) {
        return modelsToLoad.get(animatable);
    }

    public ModelGeckoPlayerThirdPerson getGeckoModel() {
        return modelProvider;
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
        ModelGeckoPlayerThirdPerson playermodel = (ModelGeckoPlayerThirdPerson) getGeoModelProvider();
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
                    modelProvider.rightArmPose = bipedmodel$armpose;
                    modelProvider.leftArmPose = bipedmodel$armpose1;
                } else {
                    modelProvider.rightArmPose = bipedmodel$armpose1;
                    modelProvider.leftArmPose = bipedmodel$armpose;
                }
            }
        }
    }

    public void renderLiving(AbstractClientPlayer entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
//        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))) return;
        matrixStackIn.pushPose();
        this.model.attackTime = this.getAttackAnim(entityIn, partialTicks);

        boolean shouldSit = entityIn.isPassenger() && (entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = entityIn.isBaby();
        float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entityIn.yHeadRotO, entityIn.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entityIn.getVehicle();
            f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
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

        float f6 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
        if (entityIn.getPose() == Pose.SLEEPING) {
            Direction direction = entityIn.getBedOrientation();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStackIn.translate((double)((float)(-direction.getStepX()) * f4), 0.0D, (double)((float)(-direction.getStepZ()) * f4));
            }
        }

        float f7 = this.getBob(entityIn, partialTicks);
        this.scale(entityIn, matrixStackIn, partialTicks);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = Mth.lerp(partialTicks, entityIn.animationSpeedOld, entityIn.animationSpeed);
            f5 = entityIn.animationPosition - entityIn.animationSpeed * (1.0F - partialTicks);
            if (entityIn.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.modelProvider.setLivingAnimations(geckoPlayer, entityIn.getUUID().hashCode());
        if (this.modelProvider.isInitialized()) {
            this.applyRotationsPlayerRenderer(entityIn, matrixStackIn, f7, f, partialTicks, f1);
            float bodyRotateAmount = this.modelProvider.getControllerValueInverted("BodyRotateController");
            this.modelProvider.setRotationAngles(entityIn, f5, f8, f7, Mth.rotLerp(bodyRotateAmount, 0, f2), f6, partialTicks);

            MowzieGeoBone leftHeldItem = modelProvider.getMowzieBone("LeftHeldItem");
            MowzieGeoBone rightHeldItem = modelProvider.getMowzieBone("RightHeldItem");

            Matrix4f worldMatInverted = matrixStackIn.last().pose().copy();
            worldMatInverted.invert();
            Matrix3f worldNormInverted = matrixStackIn.last().normal().copy();
            worldNormInverted.invert();
            PoseStack toWorldSpace = new PoseStack();
            toWorldSpace.mulPose(new Quaternion(0, -entityYaw + 180, 0, true));
            toWorldSpace.translate(0, -1.5f, 0);
            toWorldSpace.last().normal().mul(worldNormInverted);
            toWorldSpace.last().pose().multiply(worldMatInverted);

            Vector4f leftHeldItemPos = new Vector4f(0, 0, 0, 1);
            leftHeldItemPos.transform(leftHeldItem.getWorldSpaceXform());
            leftHeldItemPos.transform(toWorldSpace.last().pose());
            Vec3 leftHeldItemPos3 = new Vec3(leftHeldItemPos.x(), leftHeldItemPos.y(), leftHeldItemPos.z());

            Vector4f rightHeldItemPos = new Vector4f(0, 0, 0, 1);
            rightHeldItemPos.transform(rightHeldItem.getWorldSpaceXform());
            rightHeldItemPos.transform(toWorldSpace.last().pose());
            Vec3 rightHeldItemPos3 = new Vec3(rightHeldItemPos.x(), rightHeldItemPos.y(), rightHeldItemPos.z());

            betweenHandsPos = rightHeldItemPos3.add(leftHeldItemPos3.subtract(rightHeldItemPos3).scale(0.5));

            MowzieGeoBone particleEmitterRootBone = modelProvider.getMowzieBone("ParticleEmitterRoot");
            Vector4f emitterRootPos = new Vector4f(0, 0, 0, 1);
            emitterRootPos.transform(particleEmitterRootBone.getWorldSpaceXform());
            emitterRootPos.transform(toWorldSpace.last().pose());
            particleEmitterRoot = new Vec3(emitterRootPos.x(), emitterRootPos.y(), emitterRootPos.z());
        }
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
            int i = getOverlayCoords(entityIn, this.getWhiteOverlayProgress(entityIn, partialTicks));
            matrixStackIn.pushPose();
            worldRenderMat.load(matrixStackIn.last().pose());
            modelProvider.setTextureFromPlayer(entityIn);
            render(
                    getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(geckoPlayer)),
                    geckoPlayer, partialTicks, rendertype, matrixStackIn, bufferIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F
            );
            matrixStackIn.popPose();
            ModelBipedAnimated.copyFromGeckoModel(this.model, this.modelProvider);
            this.model.setupAnim(entityIn, f5, f8, f7, f2, f6);
        }

        if (!entityIn.isSpectator()) {
            for(RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> layerrenderer : this.layers) {
                layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        matrixStackIn.popPose();
        renderEntity(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
//        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
    }

    public void renderEntity(AbstractClientPlayer entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(entityIn))) {
            this.renderNameTag(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
    }

    protected void applyRotationsPlayerRenderer(AbstractClientPlayer entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        float f = entityLiving.getSwimAmount(partialTicks);
        if (entityLiving.isFallFlying()) {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f1 = (float)entityLiving.getFallFlyingTicks() + partialTicks;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isAutoSpinAttack()) {
                matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f2 * (-90.0F - entityLiving.getXRot())));
            }

            Vec3 vector3d = entityLiving.getViewVector(partialTicks);
            Vec3 vector3d1 = entityLiving.getDeltaMovement();
            double d0 = vector3d1.horizontalDistanceSqr();
            double d1 = vector3d.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                matrixStackIn.mulPose(Vector3f.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            float swimController = this.modelProvider.getControllerValueInverted("SwimController");
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f3 = entityLiving.isInWater() ? -90.0F - entityLiving.getXRot() : -90.0F;
            float f4 = Mth.lerp(f, 0.0F, f3) * swimController;
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f4));
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
            float bodyRotateAmount = this.modelProvider.getControllerValueInverted("BodyRotateController");
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - Mth.rotLerp(bodyRotateAmount, headYaw, rotationYaw)));
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(f * this.getFlipDegrees(entityLiving)));
        } else if (entityLiving.isAutoSpinAttack()) {
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.getXRot()));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(((float)entityLiving.tickCount + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedOrientation();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(this.getFlipDegrees(entityLiving)));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F));
        } else if (entityLiving.hasCustomName() || entityLiving instanceof Player) {
            String s = ChatFormatting.stripFormatting(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof Player) || ((Player)entityLiving).isModelPartShown(PlayerModelPart.CAPE))) {
                matrixStackIn.translate(0.0D, (double)(entityLiving.getBbHeight() + 0.1F), 0.0D);
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            }
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
    public GeoModelProvider<GeckoPlayer> getGeoModelProvider() {
        return this.modelProvider;
    }

    public ModelGeckoPlayerThirdPerson getAnimatedPlayerModel() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoPlayer geckoPlayer) {
        return getTextureLocation((AbstractClientPlayer) geckoPlayer.getPlayer());
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        RenderUtils.translate(bone, matrixStack);
        RenderUtils.moveToPivot(bone, matrixStack);
        RenderUtils.rotate(bone, matrixStack);
        RenderUtils.scale(bone, matrixStack);
        // Record xform matrices for relevant bones
        if (bone instanceof MowzieGeoBone) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone)bone;
            if (
                    mowzieBone.name.equals("LeftHeldItem") || mowzieBone.name.equals("RightHeldItem") ||
                    mowzieBone.name.equals("Head") ||
                    mowzieBone.name.equals("Body") ||
                    mowzieBone.name.equals("LeftArm") ||
                    mowzieBone.name.equals("RightArm") ||
                    mowzieBone.name.equals("RightLeg") ||
                    mowzieBone.name.equals("LeftLeg") ||
                    mowzieBone.name.equals("ParticleEmitterRoot")
            ) {
                matrixStack.pushPose();
                if (!mowzieBone.name.equals("LeftHeldItem") && !mowzieBone.name.equals("RightHeldItem")) {
                    matrixStack.scale(-1.0F, -1.0F, 1.0F);
                }
                if (mowzieBone.name.equals("Body")) {
                    matrixStack.translate(0, -0.75, 0);
                }
                if (mowzieBone.name.equals("LeftArm")) {
                    matrixStack.translate(-0.075, 0, 0);
                }
                if (mowzieBone.name.equals("RightArm")) {
                    matrixStack.translate(0.075, 0, 0);
                }
                PoseStack.Pose entry = matrixStack.last();
                mowzieBone.setWorldSpaceNormal(entry.normal().copy());
                mowzieBone.setWorldSpaceXform(entry.pose().copy());
                matrixStack.popPose();
            }
        }
        RenderUtils.moveBackFromPivot(bone, matrixStack);
        if (!bone.isHidden) {
            Iterator var10 = bone.childCubes.iterator();

            while(var10.hasNext()) {
                GeoCube cube = (GeoCube)var10.next();
                matrixStack.pushPose();
                if (!isInvisible) {
                    this.renderCube(cube, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }
                matrixStack.popPose();
            }

            var10 = bone.childBones.iterator();

            while(var10.hasNext()) {
                GeoBone childBone = (GeoBone)var10.next();
                this.renderRecursively(childBone, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }

        matrixStack.popPose();

        for(RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> layerrenderer : this.layers) {
            if (layerrenderer instanceof IGeckoRenderLayer) ((IGeckoRenderLayer)layerrenderer).renderRecursively(bone, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    @Override
    public void setCurrentRTB(MultiBufferSource rtb) {
        this.rtb = rtb;
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }
}
