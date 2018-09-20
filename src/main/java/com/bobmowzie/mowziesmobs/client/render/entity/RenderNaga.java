package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelNaga;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Josh on 9/9/2018.
 */
@SideOnly(Side.CLIENT)
public class RenderNaga extends RenderLiving<EntityNaga> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/naga.png");

    public RenderNaga(RenderManager mgr) {
        super(mgr, new ModelNaga(), 0);
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