package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class ModelGeckoPlayerFirstPerson extends MowzieAnimatedGeoModel<GeckoPlayer> {
	
	private ResourceLocation animationFileLocation;
	private ResourceLocation modelLocation;
	private ResourceLocation textureLocation;

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

	public void setUseSmallArms(boolean useSmallArms) {
		this.useSmallArms = useSmallArms;
	}

	public boolean isUsingSmallArms() {
		return useSmallArms;
	}

	@Override
	public void setLivingAnimations(GeckoPlayer entity, Integer uniqueID) {
		super.setLivingAnimations(entity, uniqueID);
		if (isInitialized()) {
			MowzieGeoBone rightArmLayerClassic = getMowzieBone("RightArmLayerClassic");
			MowzieGeoBone leftArmLayerClassic = getMowzieBone("LeftArmLayerClassic");
			MowzieGeoBone rightArmLayerSlim = getMowzieBone("RightArmLayerSlim");
			MowzieGeoBone leftArmLayerSlim = getMowzieBone("LeftArmLayerSlim");
			MowzieGeoBone rightArmClassic = getMowzieBone("RightArmClassic");
			MowzieGeoBone leftArmClassic = getMowzieBone("LeftArmClassic");
			MowzieGeoBone rightArmSlim = getMowzieBone("RightArmSlim");
			MowzieGeoBone leftArmSlim = getMowzieBone("LeftArmSlim");
			if (useSmallArms) {
				rightArmLayerClassic.setHidden(true);
				rightArmClassic.setHidden(true);
				leftArmLayerClassic.setHidden(true);
				leftArmClassic.setHidden(true);
				rightArmLayerSlim.setHidden(false);
				rightArmSlim.setHidden(false);
				leftArmLayerSlim.setHidden(false);
				leftArmSlim.setHidden(false);
			}
			else {
				rightArmLayerSlim.setHidden(true);
				rightArmSlim.setHidden(true);
				leftArmLayerSlim.setHidden(true);
				leftArmSlim.setHidden(true);
				rightArmLayerClassic.setHidden(false);
				rightArmClassic.setHidden(false);
				leftArmLayerClassic.setHidden(false);
				leftArmClassic.setHidden(false);
			}


			getMowzieBone("LeftHeldItem").setHidden(true);
			getMowzieBone("RightHeldItem").setHidden(true);
			getMowzieBone("RightArmClassic").setHidden(true);
			getMowzieBone("LeftArmClassic").setHidden(true);
			getMowzieBone("RightArmLayerClassic").setHidden(true);
			getMowzieBone("LeftArmLayerClassic").setHidden(true);
		}
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