package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPlayerAnimated;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.minecraft.util.text.TextFormatting;
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

    private static HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> modelsToLoad = new HashMap<>();
    private ModelGeckoPlayerThirdPerson modelProvider;

    private Matrix4f worldRenderMat;

    public Vector3d betweenHandsPos;

    public GeckoRenderPlayer(EntityRendererManager renderManager, ModelGeckoPlayerThirdPerson modelProvider) {
        super(renderManager, false);

        this.entityModel = new ModelPlayerAnimated<>(0.0f, false);

        this.layerRenderers.clear();
        this.addLayer(new BipedArmorLayer<>(this, new ModelBipedAnimated<>(0.5F), new ModelBipedAnimated<>(1.0F)));
        this.addLayer(new GeckoHeldItemLayer(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new Deadmau5HeadLayer(this));
        this.addLayer(new GeckoCapeLayer(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new GeckoElytraLayer<>(this, this.entityModel.bipedBody));
        this.addLayer(new GeckoParrotVariantLayer(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));
        this.addLayer(new BeeStingerLayer<>(this));
        this.addLayer(new FrozenRenderHandler.LayerFrozen<>(this));

        this.modelProvider = modelProvider;

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

    public HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> getModelsToLoad() {
        return modelsToLoad;
    }

    public void setSmallArms() {
        this.entityModel = new ModelPlayerAnimated<>(0.0f, true);
        this.modelProvider.setUseSmallArms(true);
    }

    public void render(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
        this.setModelVisibilities(entityIn);
        renderLiving(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn, geckoPlayer);
    }

    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {
        ModelGeckoPlayerThirdPerson playermodel = (ModelGeckoPlayerThirdPerson) getGeoModelProvider();
        if (playermodel.isInitialized()) {
            if (clientPlayer.isSpectator()) {
                playermodel.setVisible(false);
                playermodel.bipedHead().setHidden(false);
                playermodel.bipedHeadwear().setHidden(false);
            } else {
                playermodel.setVisible(true);
                playermodel.bipedHeadwear().setHidden(!clientPlayer.isWearing(PlayerModelPart.HAT));
                playermodel.bipedBodywear().setHidden(!clientPlayer.isWearing(PlayerModelPart.JACKET));
                playermodel.bipedLeftLegwear().setHidden(!clientPlayer.isWearing(PlayerModelPart.LEFT_PANTS_LEG));
                playermodel.bipedRightLegwear().setHidden(!clientPlayer.isWearing(PlayerModelPart.RIGHT_PANTS_LEG));
                playermodel.bipedLeftArmwear().setHidden(!clientPlayer.isWearing(PlayerModelPart.LEFT_SLEEVE));
                playermodel.bipedRightArmwear().setHidden(!clientPlayer.isWearing(PlayerModelPart.RIGHT_SLEEVE));
                playermodel.isSneak = clientPlayer.isCrouching();
                BipedModel.ArmPose bipedmodel$armpose = func_241741_a_(clientPlayer, Hand.MAIN_HAND);
                BipedModel.ArmPose bipedmodel$armpose1 = func_241741_a_(clientPlayer, Hand.OFF_HAND);
                if (bipedmodel$armpose.func_241657_a_()) {
                    bipedmodel$armpose1 = clientPlayer.getHeldItemOffhand().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM;
                }

                if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
                    modelProvider.rightArmPose = bipedmodel$armpose;
                    modelProvider.leftArmPose = bipedmodel$armpose1;
                } else {
                    modelProvider.rightArmPose = bipedmodel$armpose1;
                    modelProvider.leftArmPose = bipedmodel$armpose;
                }
            }
        }
    }

    public void renderLiving(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))) return;
        matrixStackIn.push();
        this.entityModel.swingProgress = this.getSwingProgress(entityIn, partialTicks);

        boolean shouldSit = entityIn.isPassenger() && (entityIn.getRidingEntity() != null && entityIn.getRidingEntity().shouldRiderSit());
        this.entityModel.isSitting = shouldSit;
        this.entityModel.isChild = entityIn.isChild();
        float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
        float f1 = MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYawHead, entityIn.rotationYawHead);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getRidingEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entityIn.getRidingEntity();
            f = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
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

        float f6 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
        if (entityIn.getPose() == Pose.SLEEPING) {
            Direction direction = entityIn.getBedDirection();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStackIn.translate((double)((float)(-direction.getXOffset()) * f4), 0.0D, (double)((float)(-direction.getZOffset()) * f4));
            }
        }

        float f7 = this.handleRotationFloat(entityIn, partialTicks);
        this.preRenderCallback(entityIn, matrixStackIn, partialTicks);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = MathHelper.lerp(partialTicks, entityIn.prevLimbSwingAmount, entityIn.limbSwingAmount);
            f5 = entityIn.limbSwing - entityIn.limbSwingAmount * (1.0F - partialTicks);
            if (entityIn.isChild()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.modelProvider.setLivingAnimations(geckoPlayer, entityIn.getUniqueID().hashCode());
        if (this.modelProvider.isInitialized()) {
            this.applyRotationsPlayerRenderer(entityIn, matrixStackIn, f7, f, partialTicks, f1);
            float bodyRotateAmount = this.modelProvider.getControllerValue("BodyRotateController");
            this.modelProvider.setRotationAngles(entityIn, f5, f8, f7, MathHelper.interpolateAngle(bodyRotateAmount, 0, f2), f6, partialTicks);

            MowzieGeoBone leftHeldItem = modelProvider.getMowzieBone("LeftHeldItem");
            MowzieGeoBone rightHeldItem = modelProvider.getMowzieBone("RightHeldItem");

            Matrix4f worldMatInverted = matrixStackIn.getLast().getMatrix().copy();
            worldMatInverted.invert();
            Matrix3f worldNormInverted = matrixStackIn.getLast().getNormal().copy();
            worldNormInverted.invert();
            MatrixStack toWorldSpace = new MatrixStack();
            toWorldSpace.rotate(new Quaternion(0, -entityYaw + 180, 0, true));
            toWorldSpace.translate(0, -1.5f, 0);
            toWorldSpace.getLast().getNormal().mul(worldNormInverted);
            toWorldSpace.getLast().getMatrix().mul(worldMatInverted);

            Vector4f leftHeldItemPos = new Vector4f(0, 0, 0, 1);
            leftHeldItemPos.transform(leftHeldItem.getWorldSpaceXform());
            leftHeldItemPos.transform(toWorldSpace.getLast().getMatrix());
            Vector3d leftHeldItemPos3 = new Vector3d(leftHeldItemPos.getX(), leftHeldItemPos.getY(), leftHeldItemPos.getZ());

            Vector4f rightHeldItemPos = new Vector4f(0, 0, 0, 1);
            rightHeldItemPos.transform(rightHeldItem.getWorldSpaceXform());
            rightHeldItemPos.transform(toWorldSpace.getLast().getMatrix());
            Vector3d rightHeldItemPos3 = new Vector3d(rightHeldItemPos.getX(), rightHeldItemPos.getY(), rightHeldItemPos.getZ());

            betweenHandsPos = rightHeldItemPos3.add(leftHeldItemPos3.subtract(rightHeldItemPos3).scale(0.5));
        }
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isVisible(entityIn);
        boolean flag1 = !flag && !entityIn.isInvisibleToPlayer(minecraft.player);
        boolean flag2 = minecraft.isEntityGlowing(entityIn);
        RenderType rendertype = this.func_230496_a_(entityIn, flag, flag1, flag2);
        if (rendertype != null) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getPackedOverlay(entityIn, this.getOverlayProgress(entityIn, partialTicks));
            matrixStackIn.push();
            worldRenderMat.set(matrixStackIn.getLast().getMatrix());
            render(
                    getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(geckoPlayer)),
                    geckoPlayer, partialTicks, rendertype, matrixStackIn, bufferIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F
            );
            matrixStackIn.pop();
            this.entityModel.setRotationAngles(entityIn, f5, f8, f7, f2, f6);
            ModelBipedAnimated.copyFromGeckoModel(this.entityModel, this.modelProvider);
        }

        if (!entityIn.isSpectator()) {
            for(LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> layerrenderer : this.layerRenderers) {
                layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        matrixStackIn.pop();
        renderEntity(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
    }

    public void renderEntity(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.canRenderName(entityIn))) {
            this.renderName(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
    }

    protected void applyRotationsPlayerRenderer(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        float f = entityLiving.getSwimAnimation(partialTicks);
        if (entityLiving.isElytraFlying()) {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f1 = (float)entityLiving.getTicksElytraFlying() + partialTicks;
            float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isSpinAttacking()) {
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f2 * (-90.0F - entityLiving.rotationPitch)));
            }

            Vector3d vector3d = entityLiving.getLook(partialTicks);
            Vector3d vector3d1 = entityLiving.getMotion();
            double d0 = Entity.horizontalMag(vector3d1);
            double d1 = Entity.horizontalMag(vector3d);
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                matrixStackIn.rotate(Vector3f.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            float swimController = this.modelProvider.getControllerValue("SwimController");
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f3 = entityLiving.isInWater() ? -90.0F - entityLiving.rotationPitch : -90.0F;
            float f4 = MathHelper.lerp(f, 0.0F, f3) * swimController;
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f4));
            if (entityLiving.isActualySwimming()) {
                matrixStackIn.translate(0.0D, -1.0D, (double)0.3F);
            }
        } else {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
        }
    }

    protected void applyRotationsLivingRenderer(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        if (this.func_230495_a_(entityLiving)) {
            rotationYaw += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * (double)0.4F);
        }

        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            float bodyRotateAmount = this.modelProvider.getControllerValue("BodyRotateController");
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - MathHelper.interpolateAngle(bodyRotateAmount, headYaw, rotationYaw)));
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
        } else if (entityLiving.isSpinAttacking()) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.rotationPitch));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(((float)entityLiving.ticksExisted + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedDirection();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(this.getDeathMaxRotation(entityLiving)));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270.0F));
        } else if (entityLiving.hasCustomName() || entityLiving instanceof PlayerEntity) {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof PlayerEntity) || ((PlayerEntity)entityLiving).isWearing(PlayerModelPart.CAPE))) {
                matrixStackIn.translate(0.0D, (double)(entityLiving.getHeight() + 0.1F), 0.0D);
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
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
        return getEntityTexture((AbstractClientPlayerEntity) geckoPlayer.getPlayer());
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack matrixStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStack.push();
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
                    mowzieBone.name.equals("LeftLeg")
            ) {
                matrixStack.push();
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
                MatrixStack.Entry entry = matrixStack.getLast();
                mowzieBone.setWorldSpaceNormal(entry.getNormal().copy());
                mowzieBone.setWorldSpaceXform(entry.getMatrix().copy());
                matrixStack.pop();
            }
        }
        RenderUtils.moveBackFromPivot(bone, matrixStack);
        if (!bone.isHidden) {
            Iterator var10 = bone.childCubes.iterator();

            while(var10.hasNext()) {
                GeoCube cube = (GeoCube)var10.next();
                matrixStack.push();
                this.renderCube(cube, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                matrixStack.pop();
            }

            var10 = bone.childBones.iterator();

            while(var10.hasNext()) {
                GeoBone childBone = (GeoBone)var10.next();
                this.renderRecursively(childBone, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }

        matrixStack.pop();

        for(LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> layerrenderer : this.layerRenderers) {
            if (layerrenderer instanceof IGeckoRenderLayer) ((IGeckoRenderLayer)layerrenderer).renderRecursively(bone, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}
