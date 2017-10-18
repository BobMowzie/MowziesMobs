package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * Created by Josh on 4/15/2017.
 */
public class RenderRing extends Render<EntityRing> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/ring.png");

	public RenderRing(RenderManager mgr) {
		super(mgr);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRing entity) {
		return TEXTURE;
	}

	@Override
	public void doRender(EntityRing entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		setupGL();
		bindTexture(getEntityTexture(entity));
		if (entity.facesCamera) {
			GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
			GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
		} else {
			if (entity.facing != null) {
				float pitch = (float) Math.toDegrees(Math.asin(-entity.facing.y));
				float yaw = (float) Math.toDegrees(MathHelper.atan2(entity.facing.x, entity.facing.z));
				GlStateManager.rotate(yaw, 0, 1, 0);
				GlStateManager.rotate(pitch, 1, 0, 0);
			}
		}
		renderRing(entity, partialTicks);
		GlStateManager.rotate(180, 0, 1, 0);
		renderRing(entity, partialTicks);
		revertGL();
		GlStateManager.popMatrix();
	}

	private void renderRing(EntityRing entity, float partialTicks) {
		double minU = 0;
		double maxU = 1;
		double minV = 0;
		double maxV = 1;
		Tessellator t = Tessellator.getInstance();
		BufferBuilder buf = t.getBuffer();
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		float size = entity.size * entity.interpolate(partialTicks);
		float opacity = entity.opacity * (1 - size / entity.size);
		float r = entity.r;
		float g = entity.g;
		float b = entity.b;
		buf.pos(-size, -size, 0).tex(minU, minV).lightmap(0, 240).color(r, g, b, opacity).endVertex();
		buf.pos(-size, size, 0).tex(minU, maxV).lightmap(0, 240).color(r, g, b, opacity).endVertex();
		buf.pos(size, size, 0).tex(maxU, maxV).lightmap(0, 240).color(r, g, b, opacity).endVertex();
		buf.pos(size, -size, 0).tex(maxU, minV).lightmap(0, 240).color(r, g, b, opacity).endVertex();
		t.draw();
	}

	private void setupGL() {
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0);
	}

	private void revertGL() {
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
	}
}
