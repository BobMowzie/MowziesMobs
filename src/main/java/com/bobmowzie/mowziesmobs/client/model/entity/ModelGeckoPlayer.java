package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;

public class ModelGeckoPlayer extends ModelGeckoBiped {

	public MowzieGeoBone bipedLeftArmwear() {
		return getMowzieBone("LeftArmLayer");
	}

	public MowzieGeoBone bipedRightArmwear() {
		return getMowzieBone("RightArmLayer");
	}

	public MowzieGeoBone bipedLeftLegwear() {
		return getMowzieBone("LeftLegLayer");
	}

	public MowzieGeoBone bipedRightLegwear() {
		return getMowzieBone("RightLegLayer");
	}

	public MowzieGeoBone bipedBodywear() {
		return getMowzieBone("BodyLayer");
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedLeftArmwear().setHidden(!visible);
		this.bipedRightArmwear().setHidden(!visible);
		this.bipedLeftLegwear().setHidden(!visible);
		this.bipedRightLegwear().setHidden(!visible);
		this.bipedBodywear().setHidden(!visible);
	}
}