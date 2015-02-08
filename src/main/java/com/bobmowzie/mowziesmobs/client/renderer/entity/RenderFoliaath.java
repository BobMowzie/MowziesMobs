package com.bobmowzie.mowziesmobs.client.renderer.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.MowzieModelBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFoliaath extends RenderLiving
{
	public RenderFoliaath(ModelBase model, float shadowSize)
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
        return new ResourceLocation(MowziesMobs.getModID() + "textures/entity/TextureFoliaath.png");
    }
}
