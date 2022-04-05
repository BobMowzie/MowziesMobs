package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthboreGauntlet;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelEarthboreGauntlet extends AnimatedGeoModel<ItemEarthboreGauntlet> {

    @Override
    public ResourceLocation getModelLocation(ItemEarthboreGauntlet object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/earthbore_gauntlet.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemEarthboreGauntlet object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/earthbore_gauntlet.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemEarthboreGauntlet animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/earthbore_gauntlet.animation.json");
    }
}