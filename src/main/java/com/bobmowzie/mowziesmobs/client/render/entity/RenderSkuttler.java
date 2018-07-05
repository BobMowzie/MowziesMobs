package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFrostmaw;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSkuttler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.skuttler.EntitySkuttler;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 5/8/2017.
 */
public class RenderSkuttler extends RenderLiving<EntitySkuttler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/skuttler.png");

    public RenderSkuttler(RenderManager mgr) {
        super(mgr, new ModelSkuttler(), 0.6f);
    }

    @Override
    protected float getDeathMaxRotation(EntitySkuttler entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySkuttler entity) {
        return RenderSkuttler.TEXTURE;
    }

    @Override
    public void doRender(EntitySkuttler entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
