package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaathBaby;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderFoliaathBaby extends MobRenderer<EntityBabyFoliaath, ModelFoliaathBaby<EntityBabyFoliaath>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/foliaath_baby.png");

    public RenderFoliaathBaby(EntityRendererManager mgr) {
        super(mgr, new ModelFoliaathBaby<>(), 0);
    }

    @Override
    protected float getDeathMaxRotation(EntityBabyFoliaath entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBabyFoliaath entity) {
        return RenderFoliaathBaby.TEXTURE;
    }
}