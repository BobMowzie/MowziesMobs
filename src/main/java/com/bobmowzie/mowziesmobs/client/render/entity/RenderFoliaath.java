package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderFoliaath extends MobRenderer<EntityFoliaath, ModelFoliaath<EntityFoliaath>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/foliaath.png");

    public RenderFoliaath(EntityRendererManager mgr) {
        super(mgr, new ModelFoliaath<>(), 0);
    }

    @Override
    protected float getDeathMaxRotation(EntityFoliaath entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFoliaath entity) {
        return RenderFoliaath.TEXTURE;
    }

    @Override
    public void render(EntityFoliaath entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
