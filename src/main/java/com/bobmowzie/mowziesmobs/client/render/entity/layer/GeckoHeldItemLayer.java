package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.model.ItemCameraTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.HandSide;
import net.minecraft.world.phys.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoHeldItemLayer extends LayerRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> implements IGeckoRenderLayer {
    private GeckoRenderPlayer renderPlayerAnimated;

    public GeckoHeldItemLayer(GeckoRenderPlayer entityRendererIn) {
        super(entityRendererIn);
        renderPlayerAnimated = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!renderPlayerAnimated.getAnimatedPlayerModel().isInitialized()) return;
        boolean flag = entitylivingbaseIn.getPrimaryHand() == HandSide.RIGHT;
        ItemStack mainHandStack = entitylivingbaseIn.getHeldItemMainhand();
        ItemStack offHandStack = entitylivingbaseIn.getHeldItemOffhand();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(entitylivingbaseIn);
        if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
            Ability ability = abilityCapability.getActiveAbility();
            mainHandStack = ability.heldItemMainHandOverride() != null ? ability.heldItemMainHandOverride() : mainHandStack;
            offHandStack = ability.heldItemOffHandOverride() != null ? ability.heldItemOffHandOverride() : offHandStack;
        }
        ItemStack itemstack = flag ? offHandStack : mainHandStack;
        ItemStack itemstack1 = flag ? mainHandStack : offHandStack;
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            matrixStackIn.push();
            if (this.getEntityModel().isChild) {
                float f = 0.5F;
                matrixStackIn.translate(0.0D, 0.75D, 0.0D);
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }

            this.func_229135_a_(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
            this.func_229135_a_(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.pop();
        }
    }

    private void func_229135_a_(LivingEntity entity, ItemStack itemStack, ItemCameraTransforms.TransformType transformType, HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn) {
        if (!itemStack.isEmpty()) {
            String boneName = side == HandSide.RIGHT ? "RightHeldItem" : "LeftHeldItem";
            MowzieGeoBone bone = renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone(boneName);
            MatrixStack newMatrixStack = new MatrixStack();
            newMatrixStack.getLast().getNormal().mul(bone.getWorldSpaceNormal());
            newMatrixStack.getLast().getMatrix().mul(bone.getWorldSpaceXform());
            newMatrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0F));
            boolean flag = side == HandSide.LEFT;
            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, itemStack, transformType, flag, newMatrixStack, buffer, packedLightIn);
        }
    }
}
