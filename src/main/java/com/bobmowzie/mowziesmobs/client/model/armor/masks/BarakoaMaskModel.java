package com.bobmowzie.mowziesmobs.client.model.armor.masks;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BarakoaMaskModel extends AnimatedGeoModel<ItemBarakoaMask> {

    @Override
    public ResourceLocation getModelLocation(ItemBarakoaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_" + object.getType().name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemBarakoaMask object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/mask_" + object.getType().name + ".geo.json");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemBarakoaMask animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/barakoa_mask.animation.json");
    }
}