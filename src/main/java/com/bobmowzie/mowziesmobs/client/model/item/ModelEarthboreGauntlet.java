package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthboreGauntlet;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.GeoModel;

public class ModelEarthboreGauntlet extends GeoModel<ItemEarthboreGauntlet> {

    @Override
    public ResourceLocation getModelResource(ItemEarthboreGauntlet object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/earthbore_gauntlet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemEarthboreGauntlet object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/earthbore_gauntlet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemEarthboreGauntlet animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/earthbore_gauntlet.animation.json");
    }
}