package com.bobmowzie.mowziesmobs.client.model.armor.masks;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class UmvuthanaMaskModel extends AnimatedGeoModel<ItemUmvuthanaMask> {

    @Override
    public ResourceLocation getModelLocation(ItemUmvuthanaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_" + object.getType().name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemUmvuthanaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_" + object.getType().name + ".geo.json");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemUmvuthanaMask animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/umvuthana_mask.animation.json");
    }
}