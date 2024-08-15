package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemGeomancerArmor;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelGeomancerArmor extends GeoModel<ItemGeomancerArmor> {
    @Override
    public ResourceLocation getModelResource(ItemGeomancerArmor object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/geomancer_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItemGeomancerArmor object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/item/geomancer_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItemGeomancerArmor animatable) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/empty.animation.json");
    }

    @Override
    public RenderType getRenderType(ItemGeomancerArmor animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
