package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.armor.MowzieElytraModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class GeckoElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    public GeckoElytraLayer(RenderLayerParent<T, M> rendererIn, ModelPart bipedBody) {
        super(rendererIn);
        elytraModel = new MowzieElytraModel(bipedBody);
    }
}
