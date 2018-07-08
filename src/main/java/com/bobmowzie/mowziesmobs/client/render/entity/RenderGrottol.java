package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFrostmaw;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGrottol;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 5/8/2017.
 */
public class RenderGrottol extends RenderLiving<EntityGrottol> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/grottol.png");

    public RenderGrottol(RenderManager mgr) {
        super(mgr, new ModelGrottol(), 0.6f);
    }

    @Override
    protected float getDeathMaxRotation(EntityGrottol entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGrottol entity) {
        return RenderGrottol.TEXTURE;
    }

    @Override
    public void doRender(EntityGrottol entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
