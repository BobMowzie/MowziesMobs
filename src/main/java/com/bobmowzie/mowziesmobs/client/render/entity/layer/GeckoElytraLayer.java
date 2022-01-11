package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.armor.MowzieElytraModel;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.LivingEntity;

public class GeckoElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    public GeckoElytraLayer(IEntityRenderer<T, M> rendererIn, ModelRenderer bipedBody) {
        super(rendererIn);
        modelElytra = new MowzieElytraModel(bipedBody);
    }
}
