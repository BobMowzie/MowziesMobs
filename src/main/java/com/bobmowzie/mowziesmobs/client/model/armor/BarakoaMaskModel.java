package com.bobmowzie.mowziesmobs.client.model.armor;
// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class BarakoaMaskModel<T extends LivingEntity> extends HumanoidModel<T> {

	public BarakoaMaskModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.getChild("head");

		PartDefinition maskBase = head.addOrReplaceChild("maskBase", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -7.0F));

		PartDefinition maskLeft = maskBase.addOrReplaceChild("maskLeft", CubeListBuilder.create().texOffs(58, 34).mirror().addBox(0.0F, -1.0F, -0.01F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(48, 18).mirror().addBox(0.0F, -8.0F, 0.0F, 7.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, -0.4363F, 0.0F));

		PartDefinition maskRight = maskBase.addOrReplaceChild("maskRight", CubeListBuilder.create().texOffs(48, 18).addBox(-7.0F, -8.0F, 0.0F, 7.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(58, 34).addBox(-7.0F, -1.0F, -0.01F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0F, 0.4363F, 0.0F));

		PartDefinition mane = maskBase.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -12.0F, 0.0F, 24.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 4.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}
}