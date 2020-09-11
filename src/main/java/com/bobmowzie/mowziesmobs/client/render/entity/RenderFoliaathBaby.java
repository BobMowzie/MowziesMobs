package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaathBaby;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFoliaathBaby extends MobRenderer<EntityBabyFoliaath> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/foliaath_baby.png");

    public RenderFoliaathBaby(EntityRendererManager mgr) {
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