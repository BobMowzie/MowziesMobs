package com.bobmowzie.mowziesmobs.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;

@SideOnly(Side.CLIENT)
public class FoliaathBabyRenderer extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureBabyFoliaath.png");

    public FoliaathBabyRenderer(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    public void preRenderCallback(EntityLivingBase entity, float side) {
        super.preRenderCallback(entity, side);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return FoliaathBabyRenderer.TEXTURE;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity) {
        return 0;
    }
}