package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SolVisageModel extends GeoModel<ItemSolVisage> {

	@Override
	public ResourceLocation getModelResource(ItemSolVisage object) {
		return new ResourceLocation(MowziesMobs.MODID, "geo/sol_visage.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ItemSolVisage object) {
		return new ResourceLocation(MowziesMobs.MODID, "textures/entity/umvuthi.png");
	}

	@Override
	public ResourceLocation getAnimationResource(ItemSolVisage animatable) {
		return new ResourceLocation(MowziesMobs.MODID, "animations/sol_visage.animation.json");
	}

	@Override
	public RenderType getRenderType(ItemSolVisage animatable, ResourceLocation texture) {
		return RenderType.entityCutoutNoCull(texture);
	}
}