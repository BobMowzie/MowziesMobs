package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeElite;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderTribesman extends RenderLiving
{
    public RenderTribesman(ModelBase model, float shadowSize)
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
        if (entity instanceof EntityTribeElite) return new ResourceLocation(MowziesMobs.getModId() + "textures/entity/textureTribesman2.png");
        return new ResourceLocation(MowziesMobs.getModId() + "textures/entity/textureTribesman1.png");
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 0;
    }
}
