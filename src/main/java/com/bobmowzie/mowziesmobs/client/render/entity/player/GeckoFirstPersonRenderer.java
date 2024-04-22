package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.HashMap;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class GeckoFirstPersonRenderer extends ItemInHandRenderer implements GeoRenderer<GeckoPlayer> {
    public MultiBufferSource rtb;

    public static GeckoPlayer.GeckoPlayerFirstPerson GECKO_PLAYER_FIRST_PERSON;

    private static HashMap<Class<? extends GeckoPlayer>, GeckoFirstPersonRenderer> modelsToLoad = new HashMap<>();
    private ModelGeckoPlayerFirstPerson geoModel;

    boolean mirror;

    public Vec3 particleEmitterRoot;

    public GeckoFirstPersonRenderer(Minecraft mcIn, ModelGeckoPlayerFirstPerson geoModel) {
        super(mcIn, mcIn.getEntityRenderDispatcher(), mcIn.getItemRenderer());
        this.geoModel = geoModel;
    }

    /* TODO: Not sure what this did. Still need it for new geckolib?
    static {
        AnimationController.addModelFetcher((GeoEntity object) -> {
            if (object instanceof GeckoPlayer.GeckoPlayerFirstPerson) {
                GeckoFirstPersonRenderer render = modelsToLoad.get(object.getClass());
                return (GeoEntityModel<Object>) render.getGeoModelProvider();
            } else {
                return null;
            }
        });
    }*/

    public GeckoFirstPersonRenderer getModelProvider(Class<? extends GeckoPlayer> animatable) {
        return modelsToLoad.get(animatable);
    }

    public HashMap<Class<? extends GeckoPlayer>, GeckoFirstPersonRenderer> getModelsToLoad() {
        return modelsToLoad;
    }

    public void renderItemInFirstPerson(AbstractClientPlayer player, float pitch, float partialTicks, InteractionHand handIn, float swingProgress, ItemStack stack, float equippedProgress, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, GeckoPlayer geckoPlayer) {
        this.rtb = bufferIn;

        boolean flag = handIn == InteractionHand.MAIN_HAND;
        HumanoidArm handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
        mirror = player.getMainArm() == HumanoidArm.LEFT;

        if (flag) {
            this.geoModel.setTextureFromPlayer(player);
            AnimationState<GeckoPlayer> animationState = new AnimationState<>(geckoPlayer, 0, 0, partialTicks, false);
            long instanceId = getInstanceId(geckoPlayer);

            this.geoModel.addAdditionalStateData(geckoPlayer, instanceId, animationState::setData);
            this.geoModel.handleAnimations(geckoPlayer, instanceId, animationState);

            RenderType rendertype = RenderType.itemEntityTranslucentCull(getTextureLocation(geckoPlayer));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            matrixStackIn.translate(0, -2, -1);
            actuallyRender(matrixStackIn, geckoPlayer, getGeoModel().getBakedModel(getGeoModel().getModelResource(geckoPlayer)), rendertype, bufferIn, ivertexbuilder, false, partialTicks, combinedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        PlayerAbility.HandDisplay handDisplay = PlayerAbility.HandDisplay.DEFAULT;
        float offHandEquipProgress = 0.0f;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
            Ability ability = abilityCapability.getActiveAbility();
            if (ability instanceof PlayerAbility) {
                PlayerAbility playerAbility = (PlayerAbility) ability;
                ItemStack stackOverride = flag ? playerAbility.heldItemMainHandOverride() : playerAbility.heldItemOffHandOverride();
                if (stackOverride != null) stack = stackOverride;

                handDisplay = flag ? playerAbility.getFirstPersonMainHandDisplay() : playerAbility.getFirstPersonOffHandDisplay();
            }

            if (ability.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP)
                offHandEquipProgress = Mth.clamp(1f - (ability.getTicksInSection() + partialTicks) / 5f, 0f, 1f);
            else if (ability.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.RECOVERY && ability.getCurrentSection() instanceof AbilitySection.AbilitySectionDuration)
                offHandEquipProgress = Mth.clamp((ability.getTicksInSection() + partialTicks - ((AbilitySection.AbilitySectionDuration)ability.getCurrentSection()).duration + 5) / 5f, 0f, 1f);
        }

        if (geoModel.isInitialized()) {
            if (handDisplay != PlayerAbility.HandDisplay.DONT_RENDER) {
                int sideMult = handside == HumanoidArm.RIGHT ? -1 : 1;
                if (mirror) handside = handside.getOpposite();
                String sideName = handside == HumanoidArm.RIGHT ? "Right" : "Left";
                String boneName = sideName + "Arm";
                MowzieGeoBone bone = this.geoModel.getMowzieBone(boneName);

                PoseStack newMatrixStack = new PoseStack();

                float fixedPitchController = 1f - this.geoModel.getControllerValueInverted("FixedPitchController" + sideName);
                newMatrixStack.mulPose(new Quaternionf(Axis.XP.rotationDegrees(pitch * fixedPitchController)));

                newMatrixStack.last().normal().mul(bone.getWorldSpaceNormal());
                newMatrixStack.last().pose().mul(bone.getWorldSpaceMatrix());
                newMatrixStack.translate(sideMult * 0.547, 0.7655, 0.625);

                if (mirror) handside = handside.getOpposite();

                if (stack.isEmpty() && !flag && handDisplay == PlayerAbility.HandDisplay.FORCE_RENDER && !player.isInvisible()) {
                    newMatrixStack.translate(0, -1 * offHandEquipProgress, 0);
                    super.renderPlayerArm(newMatrixStack, bufferIn, combinedLightIn, 0.0f, 0.0f, handside);
                } else {
                    super.renderArmWithItem(player, partialTicks, pitch, handIn, 0.0f, stack, 0.0f, newMatrixStack, bufferIn, combinedLightIn);
                }
            }

            PoseStack toWorldSpace = new PoseStack();
            toWorldSpace.translate(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
            toWorldSpace.mulPose(MathUtils.quatFromRotationXYZ(0,-player.getYRot() + 180, 0, true));
            toWorldSpace.mulPose(MathUtils.quatFromRotationXYZ(-player.getXRot(),0, 0, true));
            MowzieGeoBone particleEmitterRootBone = geoModel.getMowzieBone("ParticleEmitterRoot");
            Vector4f emitterRootPos = new Vector4f(0, 0, 0, 1);
            emitterRootPos.mul(particleEmitterRootBone.getWorldSpaceMatrix());
            emitterRootPos.mul(toWorldSpace.last().pose());
            particleEmitterRoot = new Vec3(emitterRootPos.x(), emitterRootPos.y(), emitterRootPos.z());
        }
    }

    public void setSmallArms() {
        this.geoModel.setUseSmallArms(true);
    }

    public ModelGeckoPlayerFirstPerson getAnimatedPlayerModel() {
        return this.geoModel;
    }

    @Override
    public GeoModel<GeckoPlayer> getGeoModel() {
        return geoModel;
    }

    @Override
    public GeckoPlayer getAnimatable() {
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoPlayer geckoPlayer) {
        return ((AbstractClientPlayer)geckoPlayer.getPlayer()).getSkinTextureLocation();
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return false;
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {

    }

    @Override
    public void updateAnimatedTextureFrame(GeckoPlayer animatable) {

    }

    @Override
    public void renderRecursively(PoseStack matrixStack, GeckoPlayer animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferIn, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        if (mirror) {
            MowzieRenderUtils.translateMirror(matrixStack, bone);
            MowzieRenderUtils.moveToPivotMirror(matrixStack, bone);
            MowzieRenderUtils.rotateMirror(matrixStack, bone);
            RenderUtils.scaleMatrixForBone(matrixStack, bone);
        }
        else {
            RenderUtils.translateMatrixToBone(matrixStack, bone);
            RenderUtils.translateToPivotPoint(matrixStack, bone);
            RenderUtils.rotateMatrixAroundBone(matrixStack, bone);
            RenderUtils.scaleMatrixForBone(matrixStack, bone);
        }
        // Record xform matrices for relevant bones
        if (bone instanceof MowzieGeoBone) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone)bone;
            if (mowzieBone.getName().equals("LeftArm") || mowzieBone.getName().equals("RightArm") || mowzieBone.getName().equals("ParticleEmitterRoot")) {
                matrixStack.pushPose();
                PoseStack.Pose entry = matrixStack.last();
                mowzieBone.setWorldSpaceNormal(new Matrix3f(entry.normal()));
                mowzieBone.setWorldSpaceMatrix(new Matrix4f(entry.pose()));
                matrixStack.popPose();
            }
        }
        if (mirror) {
            MowzieRenderUtils.translateAwayFromPivotPointMirror(matrixStack, bone);
        }
        else {
            RenderUtils.translateAwayFromPivotPoint(matrixStack, bone);
        }
        renderCubesOfBone(matrixStack, bone, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        if (!isReRender)
            applyRenderLayersForBone(matrixStack, animatable, bone, renderType, bufferIn, buffer, partialTick, packedLightIn, packedOverlayIn);

        renderChildBones(matrixStack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStack.popPose();
    }
}
