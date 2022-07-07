package com.bobmowzie.mowziesmobs.client.model.armor;
// Made with Blockbench 4.2.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class SolVisageModel<T extends LivingEntity> extends HumanoidModel<T> {

	public SolVisageModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createArmorLayer() {
		CubeDeformation deformation = CubeDeformation.NONE;
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.getChild("head");

		PartDefinition maskBase = head.addOrReplaceChild("maskBase", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, deformation), PartPose.offset(0.0F, -6.777372F, -4F));

		PartDefinition maskFace = maskBase.addOrReplaceChild("maskFace", CubeListBuilder.create().texOffs(0, 97).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 15.0F, 7.0F, deformation), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition maskMouth = maskFace.addOrReplaceChild("maskMouth", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, deformation), PartPose.offset(0.0F, 5.0F, 0.0F));

		PartDefinition upperLip = maskMouth.addOrReplaceChild("upperLip", CubeListBuilder.create().texOffs(50, 124).addBox(-6.0F, -2.0F, 0.0F, 12.0F, 2.0F, 2.0F, deformation), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition teethTop = upperLip.addOrReplaceChild("teethTop", CubeListBuilder.create().texOffs(0, 120).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 1.0F, deformation), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition jaw = maskMouth.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(48, 109).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 4.0F, 7.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lowerLip = jaw.addOrReplaceChild("lowerLip", CubeListBuilder.create().texOffs(50, 124).addBox(-6.0F, 0.0F, -2.0F, 12.0F, 2.0F, 2.0F, deformation), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 3.1416F));

		PartDefinition teethBottom = lowerLip.addOrReplaceChild("teethBottom", CubeListBuilder.create().texOffs(0, 120).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, deformation), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 3.1416F, 0.0F, -3.1416F));

		PartDefinition leftLip = jaw.addOrReplaceChild("leftLip", CubeListBuilder.create().texOffs(26, 120).addBox(0.0F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, deformation), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition rightLip = jaw.addOrReplaceChild("rightLip", CubeListBuilder.create().texOffs(26, 120).mirror().addBox(-2.0F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, deformation).mirror(false), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition forehead = maskFace.addOrReplaceChild("forehead", CubeListBuilder.create().texOffs(0, 122).addBox(-6.0F, 0.0F, -2.0F, 12.0F, 4.0F, 2.0F, deformation), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition nose = maskFace.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(34, 117).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 7.0F, 4.0F, deformation), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition headdress1 = maskBase.addOrReplaceChild("headdress1", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition headdress1back = maskBase.addOrReplaceChild("headdress1back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, 3.1416F));

		PartDefinition headdress2 = maskBase.addOrReplaceChild("headdress2", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.6981F));

		PartDefinition headdress2back = maskBase.addOrReplaceChild("headdress2back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, -2.4435F));

		PartDefinition headdress3 = maskBase.addOrReplaceChild("headdress3", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 1.3963F));

		PartDefinition headdress3back = maskBase.addOrReplaceChild("headdress3back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, -1.7453F));

		PartDefinition headdress4 = maskBase.addOrReplaceChild("headdress4", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, -0.6981F));

		PartDefinition headdress4back = maskBase.addOrReplaceChild("headdress4back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, 2.4435F));

		PartDefinition headdress5 = maskBase.addOrReplaceChild("headdress5", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, -1.3963F));

		PartDefinition headdress5back = maskBase.addOrReplaceChild("headdress5back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, 1.7453F));

		PartDefinition headdress6 = maskBase.addOrReplaceChild("headdress6", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 2.0944F));

		PartDefinition headdress6back = maskBase.addOrReplaceChild("headdress6back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, -1.0472F));

		PartDefinition headdress7 = maskBase.addOrReplaceChild("headdress7", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, -2.0944F));

		PartDefinition headdress7back = maskBase.addOrReplaceChild("headdress7back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, 1.0472F));

		PartDefinition rightEar = maskBase.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(38, 109).addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 2.0F, deformation), PartPose.offsetAndRotation(6.0F, -2.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		PartDefinition rightEarring = rightEar.addOrReplaceChild("rightEarring", CubeListBuilder.create().texOffs(0, 84).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, deformation), PartPose.offset(2.5F, 6.0F, 1.0F));

		PartDefinition rightEarringBack = rightEar.addOrReplaceChild("rightEarringBack", CubeListBuilder.create().texOffs(0, 84).addBox(-2.0F, 0.0F, 0.01F, 4.0F, 4.0F, 0.0F, deformation), PartPose.offsetAndRotation(2.5F, 6.0F, 1.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition leftEar = maskBase.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(38, 109).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 6.0F, 2.0F, deformation), PartPose.offsetAndRotation(-6.0F, -2.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		PartDefinition leftEarring = leftEar.addOrReplaceChild("leftEarring", CubeListBuilder.create().texOffs(0, 84).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, deformation), PartPose.offset(-2.5F, 6.0F, 1.0F));

		PartDefinition leftEarringBack = leftEar.addOrReplaceChild("leftEarringBack", CubeListBuilder.create().texOffs(0, 84).addBox(-2.0F, 0.0F, 0.01F, 4.0F, 4.0F, 0.0F, deformation), PartPose.offsetAndRotation(-2.5F, 6.0F, 1.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {
		poseStack.pushPose();
		poseStack.scale(0.8f, 0.8f, 0.8f);
		super.renderToBuffer(poseStack, vertexConsumer, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_);
		poseStack.popPose();
	}
}