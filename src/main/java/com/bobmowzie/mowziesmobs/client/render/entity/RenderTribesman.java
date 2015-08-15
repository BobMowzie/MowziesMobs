package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribeElite;
import com.bobmowzie.mowziesmobs.common.entity.EntityTribesman;
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
    private static final ResourceLocation ELITE_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman1.png");

    private static final ResourceLocation TEXTURE2 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman2.png");
    private static final ResourceLocation TEXTURE3 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman3.png");
    private static final ResourceLocation TEXTURE4 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman4.png");
    private static final ResourceLocation TEXTURE5 = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribesman5.png");

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
        if (entity instanceof EntityTribeElite)
        {
            return ELITE_TEXTURE;
        }
        EntityTribesman tribesman = (EntityTribesman)entity;
        if (tribesman.getMask() == 2) return TEXTURE2;
        if (tribesman.getMask() == 3) return TEXTURE3;
        if (tribesman.getMask() == 4) return TEXTURE4;
        if (tribesman.getMask() == 5) return TEXTURE5;
        return TEXTURE2;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity)
    {
        return 0;
    }
}
