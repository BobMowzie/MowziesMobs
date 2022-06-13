package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.armor.MowzieElytraModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;

public class GeckoElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    public GeckoElytraLayer(RenderLayerParent<T, M> rendererIn, EntityModelSet modelSet) {
        super(rendererIn, modelSet);
        elytraModel = new MowzieElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }
}
