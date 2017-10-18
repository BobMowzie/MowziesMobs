package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAxeAttack extends Render<EntityAxeAttack> {
	private static ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

	ModelAxeAttack model;

	public RenderAxeAttack(RenderManager mgr) {
		super(mgr);
		model = new ModelAxeAttack();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAxeAttack entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityAxeAttack entity, double x, double y, double z, float yaw, float delta) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(yaw, 0, -1, 0);
		bindTexture(TEXTURE);
		model.render(entity, 0.0625F, delta);
		GlStateManager.popMatrix();
	}
}
