package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderBabyFoliaath extends RenderLiving
{
    public RenderBabyFoliaath(ModelBase model, float shadowSize)
    {
        super(model, shadowSize);
    }

    @Override
    public void preRenderCallback(EntityLivingBase entity, float side)
    {
        super.preRenderCallback(entity, side);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity)
    {
        return new ResourceLocation(MowziesMobs.getModId() + "textures/entity/textureBabyFoliaath.png");
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 0;
    }
}