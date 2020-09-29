package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelNaga;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by Josh on 9/9/2018.
 */
@OnlyIn(Dist.CLIENT)
public class RenderNaga extends MobRenderer<EntityNaga, ModelNaga<EntityNaga>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/naga.png");

    public RenderNaga(EntityRendererManager mgr) {
        super(mgr, new ModelNaga<>(), 0);
    }

    @Override
    protected float getDeathMaxRotation(EntityNaga entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityNaga entity) {
        return TEXTURE;
    }
}