package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.LanternGelLayer;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderLantern extends MobRenderer<EntityLantern, ModelLantern<EntityLantern>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/mmlantern.png");

    public RenderLantern(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelLantern<>(), 0.6f);
        this.addLayer(new LanternGelLayer<>(this));
    }

    @Override
    protected float getFlipDegrees(EntityLantern entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLantern entity) {
        return RenderLantern.TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(EntityLantern p_114496_, BlockPos p_114497_) {
        return 15;
    }
}
