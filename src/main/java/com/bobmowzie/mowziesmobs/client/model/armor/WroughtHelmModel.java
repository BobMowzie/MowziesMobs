package com.bobmowzie.mowziesmobs.client.model.armor;
// Made with Blockbench 4.2.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WroughtHelmModel<T extends LivingEntity> extends HumanoidModel<T> {

	public WroughtHelmModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createArmorLayer() {
		CubeDeformation deformation = CubeDeformation.NONE;
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.getChild("head");

		PartDefinition tuskRight1 = head.addOrReplaceChild("tuskRight1", CubeListBuilder.create().texOffs(40, 24).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -1.5F, -2.5F, 0.4363F, 0.7854F, 0.0F));

		PartDefinition tuskRight2 = tuskRight1.addOrReplaceChild("tuskRight2", CubeListBuilder.create().texOffs(34, 4).addBox(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.5F, -5.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition hornRight1 = head.addOrReplaceChild("hornRight1", CubeListBuilder.create().texOffs(8, 3).mirror().addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, -8.0F, -3.0F, -0.3491F, -0.7854F, 0.0F));

		PartDefinition hornLeft = hornRight1.addOrReplaceChild("hornLeft", CubeListBuilder.create().texOffs(43, 12).mirror().addBox(0.0F, -2.0F, -6.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.5F, -6.0F, -1.2217F, 0.0F, 0.0F));

		PartDefinition tuskLeft1 = head.addOrReplaceChild("tuskLeft1", CubeListBuilder.create().texOffs(40, 24).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.5F, -2.5F, 0.4363F, -0.7854F, 0.0F));

		PartDefinition tuskLeft2 = tuskLeft1.addOrReplaceChild("tuskLeft2", CubeListBuilder.create().texOffs(34, 4).addBox(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.5F, -5.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition hornLeft1 = head.addOrReplaceChild("hornLeft1", CubeListBuilder.create().texOffs(8, 3).mirror().addBox(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -8.0F, -3.0F, -0.3491F, 0.7854F, 0.0F));

		PartDefinition hornRight = hornLeft1.addOrReplaceChild("hornRight", CubeListBuilder.create().texOffs(30, 12).mirror().addBox(0.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.5F, -6.0F, -1.2217F, 0.0F, 0.0F));

		PartDefinition shape1 = head.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}