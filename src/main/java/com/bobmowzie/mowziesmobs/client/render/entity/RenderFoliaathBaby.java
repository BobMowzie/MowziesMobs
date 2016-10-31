package com.bobmowzie.mowziesmobs.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaathBaby;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;

@SideOnly(Side.CLIENT)
public class RenderFoliaathBaby extends RenderLiving<EntityBabyFoliaath> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/foliaath_baby.png");

    public RenderFoliaathBaby(RenderManager mgr) {
        super(mgr, new ModelFoliaathBaby(), 0);
    }

    @Override
    protected float getDeathMaxRotation(EntityBabyFoliaath entity) {
        return 0;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBabyFoliaath entity) {
        return RenderFoliaathBaby.TEXTURE;
    }
}