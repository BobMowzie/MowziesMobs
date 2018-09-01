package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelDynamicsTester;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 8/30/2018.
 */
public class RenderDynamicsTester extends RenderLiving<EntityDynamicsTester> {
    private static final ResourceLocation TEXTURE_STONE = new ResourceLocation("textures/blocks/stone.png");

    public RenderDynamicsTester(RenderManager mgr) {
        super(mgr, new ModelDynamicsTester(), 0.5f);
    }

    @Override
    protected float getDeathMaxRotation(EntityDynamicsTester entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityDynamicsTester entity) {
        return TEXTURE_STONE;
    }
}
