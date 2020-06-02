package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Josh on 5/8/2017.
 */
public class RenderLantern extends RenderLiving<EntityLantern> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/mmlantern.png");

    public RenderLantern(RenderManager mgr) {
        super(mgr, new ModelLantern(), 0.6f);
    }

    @Override
    protected float getDeathMaxRotation(EntityLantern entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityLantern entity) {
        return RenderLantern.TEXTURE;
    }
}
