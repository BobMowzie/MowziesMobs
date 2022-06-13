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

		PartDefinition group = head.addOrReplaceChild("group", CubeListBuilder.create().texOffs(0, 122).addBox(-6.0F, -7.0F, -10.0F, 12.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 97).addBox(-6.0F, -7.0F, -8.0F, 12.0F, 15.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(50, 124).addBox(-6.0F, 2.0F, -10.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 120).mirror().addBox(4.0F, 4.0F, -10.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(26, 120).addBox(-6.0F, 4.0F, -10.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 120).addBox(-4.0F, 4.0F, -9.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 8.0F));

		PartDefinition cube_r1 = group.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(4, 84).addBox(-2.25F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, 3.0F, -4.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r2 = group.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 84).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.75F, 3.0F, -4.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r3 = group.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 109).addBox(5.0F, 2.0F, -8.0F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.25F, -5.0F, 4.5F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r4 = group.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(38, 109).addBox(5.0F, 2.0F, -8.0F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, -5.0F, -0.5F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r5 = group.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(34, 117).addBox(4.0F, 1.0F, -8.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -3.0F, -1.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition bone = group.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(50, 124).addBox(-4.0F, 6.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 14.0F, -2.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(-2.0F, -2.0F, 1.0F));

		PartDefinition cube_r6 = bone2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 120).addBox(-8.0F, 10.0F, 7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -3.1416F));

		PartDefinition bone3 = head.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, 45.75F, 6.0F));

		PartDefinition headdress3back = bone3.addOrReplaceChild("headdress3back", CubeListBuilder.create().texOffs(27, 76).mirror().addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, 1.3963F));

		PartDefinition headdress6back = bone3.addOrReplaceChild("headdress6back", CubeListBuilder.create().texOffs(27, 76).mirror().addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, 2.0944F));

		PartDefinition headdress6 = bone3.addOrReplaceChild("headdress6", CubeListBuilder.create().texOffs(27, 76).mirror().addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, -1.0472F));

		PartDefinition headdress4back = bone3.addOrReplaceChild("headdress4back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, -0.6981F));

		PartDefinition headdress4 = bone3.addOrReplaceChild("headdress4", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, 2.4435F));

		PartDefinition headdress7 = bone3.addOrReplaceChild("headdress7", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, 1.0472F));

		PartDefinition headdress7back = bone3.addOrReplaceChild("headdress7back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, -2.0944F));

		PartDefinition headdress2 = bone3.addOrReplaceChild("headdress2", CubeListBuilder.create().texOffs(27, 76).mirror().addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, -2.4435F));

		PartDefinition headdress3 = bone3.addOrReplaceChild("headdress3", CubeListBuilder.create().texOffs(27, 76).mirror().addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, -1.7453F));

		PartDefinition headdress2back = bone3.addOrReplaceChild("headdress2back", CubeListBuilder.create().texOffs(27, 76).mirror().addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, 0.6981F));

		PartDefinition headdress5back = bone3.addOrReplaceChild("headdress5back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, -1.3963F));

		PartDefinition headdress1back = bone3.addOrReplaceChild("headdress1back", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.01F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition headdress1 = bone3.addOrReplaceChild("headdress1", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, 3.1416F));

		PartDefinition headdress5 = bone3.addOrReplaceChild("headdress5", CubeListBuilder.create().texOffs(27, 76).addBox(-3.0F, -18.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.7774F, -4.0F, -2.618F, 0.0F, 1.7453F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}
}