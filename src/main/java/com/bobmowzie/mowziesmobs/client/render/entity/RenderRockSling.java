package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelRockSling;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderRockSling extends GeoEntityRenderer<EntityRockSling> {

    public RenderRockSling(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelRockSling());
    }
}
