package com.bobmowzie.mowziesmobs.client.model.armor;
// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;

public class BarakoaMaskModel<T extends LivingEntity> extends HumanoidModel<T> {

	public BarakoaMaskModel(ModelPart root) {
		super(root);
		head.cubes = Collections.EMPTY_LIST;
		hat.cubes = Collections.EMPTY_LIST;
	}

	public static LayerDefinition createArmorLayer() {
		CubeDeformation deformation = CubeDeformation.NONE;
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.getChild("head");

		PartDefinition maskBase = head.addOrReplaceChild("maskBase", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, deformation), PartPose.offset(0.0F, -3.310181F, -8.267222F));

		PartDefinition maskLeft = maskBase.addOrReplaceChild("maskLeft", CubeListBuilder.create().texOffs(48, 18).mirror().addBox(0.0F, -8.0F, 0.0F, 7.0F, 14.0F, 2.0F, deformation).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.4363F, 0.0F));

		PartDefinition maskRight = maskBase.addOrReplaceChild("maskRight", CubeListBuilder.create().texOffs(48, 18).addBox(-7.0F, -8.0F, 0.0F, 7.0F, 14.0F, 2.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.4363F, 0.0F));

		PartDefinition mane = maskBase.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -12.0F, 0.0F, 24.0F, 24.0F, 0.0F, deformation), PartPose.offset(0.0F, -2.0F, 4.0F));

		PartDefinition maneBack = maskBase.addOrReplaceChild("maneBack", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -12.0F, 0.0F, 24.0F, 24.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, -2.0F, 3.99F, 0, (float) Math.PI, 0));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {
		poseStack.pushPose();
		poseStack.scale(0.8f, 0.8f, 0.8f);
		poseStack.translate(0, -0.1f, 0);
		super.renderToBuffer(poseStack, vertexConsumer, p_102036_, p_102037_, p_102038_, p_102039_, p_102040_, p_102041_);
		poseStack.popPose();
	}
}