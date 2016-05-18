package com.bobmowzie.mowziesmobs.client.render;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class FoliaathRenderer extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureFoliaath.png");

    public FoliaathRenderer(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return FoliaathRenderer.TEXTURE;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity) {
        return 0;
    }
}
