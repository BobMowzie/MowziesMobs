package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthboreGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSculptorStaff extends AnimatedGeoModel<ItemSculptorStaff> {

    @Override
    public ResourceLocation getModelLocation(ItemSculptorStaff object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemSculptorStaff object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/sculptor_staff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemSculptorStaff animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/sculptor_staff.animation.json");
    }
}