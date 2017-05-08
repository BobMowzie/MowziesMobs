package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.EntityFrostmaw;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 5/8/2017.
 */
public class RenderFrostmaw extends RenderLiving<EntityFrostmaw> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/frostmaw.png");

    public RenderFrostmaw(RenderManager mgr) {
        super(mgr, new ModelFrostmaw(), 3.5f);
    }

    @Override
    protected float getDeathMaxRotation(EntityFrostmaw entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFrostmaw entity) {
        return RenderFrostmaw.TEXTURE;
    }
}
