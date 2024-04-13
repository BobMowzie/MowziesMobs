package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.GeoModel;

public class UmvuthanaMaskModel extends GeoModel<ItemUmvuthanaMask> {

    @Override
    public ResourceLocation getModelResource(ItemUmvuthanaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_" + object.getType().name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemUmvuthanaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/umvuthana_mask_" + object.getType().name + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemUmvuthanaMask animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/umvuthana_mask.animation.json");
    }
}