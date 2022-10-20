package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
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
public class GeckoFirstPersonRenderer extends ItemInHandRenderer implements IGeoRenderer<GeckoPlayer> {
    public MultiBufferSource rtb;

    public static GeckoPlayer.GeckoPlayerFirstPerson GECKO_PLAYER_FIRST_PERSON;

    private static HashMap<Class<? extends GeckoPlayer>, GeckoFirstPersonRenderer> modelsToLoad = new HashMap<>();
    private ModelGeckoPlayerFirstPerson modelProvider;

    boolean mirror;

    public GeckoFirstPersonRenderer(Minecraft mcIn, ModelGeckoPlayerFirstPerson modelProvider) {
        super(mcIn);
        this.modelProvider = modelProvider;
    }

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof GeckoPlayer.GeckoPlayerFirstPerson) {
                GeckoFirstPersonRenderer render = modelsToLoad.get(object.getClass());
                return (IAnimatableModel<Object>) render.getGeoModelProvider();
            } else {
                return null;
            }
        });
    }

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
            this.modelProvider.setTextureFromPlayer(player);
            this.modelProvider.setLivingAnimations(geckoPlayer, player.getUUID().hashCode());

            RenderType rendertype = RenderType.itemEntityTranslucentCull(getTextureLocation(geckoPlayer));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            matrixStackIn.translate(0, -2, -1);
            render(
                    getGeoModelProvider().getModel(getGeoModelProvider().getModelLocation(geckoPlayer)),
                    geckoPlayer, partialTicks, rendertype, matrixStackIn, bufferIn, ivertexbuilder, combinedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F
            );
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

        if (handDisplay != PlayerAbility.HandDisplay.DONT_RENDER && modelProvider.isInitialized()) {
            int sideMult = handside == HumanoidArm.RIGHT ? -1 : 1;
            if (mirror) handside = handside.getOpposite();
            String sideName = handside == HumanoidArm.RIGHT ? "Right" : "Left";
            String boneName = sideName + "Arm";
            MowzieGeoBone bone = this.modelProvider.getMowzieBone(boneName);

            PoseStack newMatrixStack = new PoseStack();

            float fixedPitchController = 1f - this.modelProvider.getControllerValue("FixedPitchController" + sideName);
            newMatrixStack.mulPose(new Quaternion(Vector3f.XP, pitch * fixedPitchController, true));

            newMatrixStack.last().normal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.last().pose().multiply(bone.getWorldSpaceXform());
            newMatrixStack.translate(sideMult * 0.547, 0.7655, 0.625);

            if (mirror) handside = handside.getOpposite();

            if (stack.isEmpty() && !flag && handDisplay == PlayerAbility.HandDisplay.FORCE_RENDER && !player.isInvisible()) {
                newMatrixStack.translate(0, -1 * offHandEquipProgress, 0);
                super.renderPlayerArm(newMatrixStack, bufferIn, combinedLightIn, 0.0f, 0.0f, handside);
            }
            else {
                super.renderArmWithItem(player, partialTicks, pitch, handIn, 0.0f, stack, 0.0f, newMatrixStack, bufferIn, combinedLightIn);
            }
        }
    }

    public void setSmallArms() {
        this.modelProvider.setUseSmallArms(true);
    }

    @Override
    public GeoModelProvider<GeckoPlayer> getGeoModelProvider() {
        return this.modelProvider;
    }

    public ModelGeckoPlayerFirstPerson getAnimatedPlayerModel() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoPlayer geckoPlayer) {
        return ((AbstractClientPlayer)geckoPlayer.getPlayer()).getSkinTextureLocation();
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        if (mirror) {
            MowzieRenderUtils.translateMirror(bone, matrixStack);
            MowzieRenderUtils.moveToPivotMirror(bone, matrixStack);
            MowzieRenderUtils.rotateMirror(bone, matrixStack);
            RenderUtils.scale(bone, matrixStack);
        }
        else {
            RenderUtils.translate(bone, matrixStack);
            RenderUtils.moveToPivot(bone, matrixStack);
            RenderUtils.rotate(bone, matrixStack);
            RenderUtils.scale(bone, matrixStack);
        }
        // Record xform matrices for relevant bones
        if (bone instanceof MowzieGeoBone) {
            MowzieGeoBone mowzieBone = (MowzieGeoBone)bone;
            if (mowzieBone.name.equals("LeftArm") || mowzieBone.name.equals("RightArm")) {
                matrixStack.pushPose();
                PoseStack.Pose entry = matrixStack.last();
                mowzieBone.setWorldSpaceNormal(entry.normal().copy());
                mowzieBone.setWorldSpaceXform(entry.pose().copy());
                matrixStack.popPose();
            }
        }
        if (mirror) {
            MowzieRenderUtils.moveBackFromPivotMirror(bone, matrixStack);
        }
        else {
            RenderUtils.moveBackFromPivot(bone, matrixStack);
        }
        if (!bone.isHidden) {
            Iterator var10 = bone.childCubes.iterator();

            while(var10.hasNext()) {
                GeoCube cube = (GeoCube)var10.next();
                matrixStack.pushPose();
                this.renderCube(cube, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                matrixStack.popPose();
            }

            var10 = bone.childBones.iterator();

            while(var10.hasNext()) {
                GeoBone childBone = (GeoBone)var10.next();
                this.renderRecursively(childBone, matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }
        }

        matrixStack.popPose();
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
