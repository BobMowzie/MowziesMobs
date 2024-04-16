package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class UmvuthanaMaskModel extends GeoModel<ItemUmvuthanaMask> {

    @Override
    public ResourceLocation getModelResource(ItemUmvuthanaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_" + object.getMaskType().name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemUmvuthanaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/umvuthana_mask_" + object.getMaskType().name + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemUmvuthanaMask animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/umvuthana_mask.animation.json");
    }

    @Override
    public RenderType getRenderType(ItemUmvuthanaMask animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }
}