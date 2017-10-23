package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFoliaath extends RenderLiving<EntityFoliaath> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/foliaath.png");

	public RenderFoliaath(RenderManager mgr) {
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
}
