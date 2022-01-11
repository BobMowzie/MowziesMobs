package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGrottol;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderGrottol extends MobRenderer<EntityGrottol, ModelGrottol<EntityGrottol>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/grottol.png");

    public RenderGrottol(EntityRendererManager mgr) {
        super(mgr, new ModelGrottol<>(), 0.6f);
    }

    @Override
    protected float getDeathMaxRotation(EntityGrottol entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGrottol entity) {
        return RenderGrottol.TEXTURE;
    }

    /*@Override
    public void doRender(EntityGrottol entity, double x, double y, double z, float yaw, float delta) {
        if (entity.hasMinecartBlockDisplay()) {
            if (!renderOutlines) {
                renderName(entity, x, y, z);
            }
        } else {
            super.doRender(entity, x, y, z, yaw, delta);
        }
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float delta) {
        if (!(entity instanceof EntityGrottol) || !((EntityGrottol) entity).hasMinecartBlockDisplay()) {
            super.doRenderShadowAndFire(entity, x, y, z, yaw, delta);
        }
    }*/
}
