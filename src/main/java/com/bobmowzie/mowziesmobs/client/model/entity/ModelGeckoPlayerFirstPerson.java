package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

public class ModelGeckoPlayerFirstPerson extends MowzieAnimatedGeoModel<GeckoPlayer> {
	
	private ResourceLocation animationFileLocation;
	private ResourceLocation modelLocation;
	private ResourceLocation textureLocation;

	public boolean isSitting = false;
	public boolean isChild = true;
	public float swingProgress;
	public boolean isSneak;
	public float swimAnimation;

	public BipedModel.ArmPose leftArmPose = BipedModel.ArmPose.EMPTY;
	public BipedModel.ArmPose rightArmPose = BipedModel.ArmPose.EMPTY;

	protected boolean useSmallArms;
	
	@Override
	public ResourceLocation getAnimationFileLocation(GeckoPlayer animatable) {
		return animationFileLocation;
	}

	@Override
	public ResourceLocation getModelLocation(GeckoPlayer animatable) {
		return modelLocation;
	}

	@Override
	public ResourceLocation getTextureLocation(GeckoPlayer animatable) {
		return textureLocation;
	}

	/** Check if the modelId has some ResourceLocation **/
	@Override
	public boolean resourceForModelId(AbstractClientPlayerEntity player) {
		this.animationFileLocation = new ResourceLocation(MowziesMobs.MODID, "animations/animated_player_first_person.animation.json");
		this.modelLocation = new ResourceLocation(MowziesMobs.MODID, "geo/animated_player_first_person.geo.json");
		this.textureLocation = player.getLocationSkin();
		return true;
	}
}