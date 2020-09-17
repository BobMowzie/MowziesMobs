package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderFoliaath extends MobRenderer<EntityFoliaath> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/foliaath.png");

    public RenderFoliaath(EntityRendererManager mgr) {
        super(mgr, new ModelFoliaath(), 0);
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
    public void doRender(EntityFoliaath entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        //int biomeColor = entity.world.getBiome(entity.getPosition()).getGrassColorAtPos(entity.getPosition());
    }
}
