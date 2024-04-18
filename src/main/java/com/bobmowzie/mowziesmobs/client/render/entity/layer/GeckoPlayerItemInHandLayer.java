package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoPlayerItemInHandLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> implements IGeckoRenderLayer {
    private GeckoRenderPlayer renderPlayerAnimated;

    public GeckoPlayerItemInHandLayer(GeckoRenderPlayer entityRendererIn) {
        super(entityRendererIn);
        renderPlayerAnimated = entityRendererIn;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!renderPlayerAnimated.getAnimatedPlayerModel().isInitialized()) return;
        boolean flag = entitylivingbaseIn.getMainArm() == HumanoidArm.RIGHT;
        ItemStack mainHandStack = entitylivingbaseIn.getMainHandItem();
        ItemStack offHandStack = entitylivingbaseIn.getOffhandItem();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(entitylivingbaseIn);
        if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
            Ability ability = abilityCapability.getActiveAbility();
            if (ability instanceof PlayerAbility) {
                PlayerAbility playerAbility = (PlayerAbility) ability;
                mainHandStack = playerAbility.heldItemMainHandOverride() != null ? playerAbility.heldItemMainHandOverride() : mainHandStack;
                offHandStack = playerAbility.heldItemOffHandOverride() != null ? playerAbility.heldItemOffHandOverride() : offHandStack;
            }
        }
        ItemStack itemstack = flag ? offHandStack : mainHandStack;
        ItemStack itemstack1 = flag ? mainHandStack : offHandStack;
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            matrixStackIn.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                matrixStackIn.translate(0.0D, 0.75D, 0.0D);
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderArmWithItem(entitylivingbaseIn, itemstack1, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, matrixStackIn, bufferIn, packedLightIn);
            this.renderArmWithItem(entitylivingbaseIn, itemstack, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
    }

    private void renderArmWithItem(LivingEntity entity, ItemStack itemStack, ItemTransforms.TransformType transformType, HumanoidArm side, PoseStack matrixStack, MultiBufferSource buffer, int packedLightIn) {
        if (!itemStack.isEmpty()) {
            String boneName = side == HumanoidArm.RIGHT ? "RightHeldItem" : "LeftHeldItem";
            MowzieGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone(boneName);
            PoseStack newMatrixStack = new PoseStack();
            newMatrixStack.last().normal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.last().pose().multiply(bone.getWorldSpaceMatrix());
            newMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean flag = side == HumanoidArm.LEFT;
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(entity, itemStack, transformType, flag, newMatrixStack, buffer, packedLightIn);
        }
    }
}
