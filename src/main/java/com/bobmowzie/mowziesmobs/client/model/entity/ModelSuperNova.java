package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ExtendedModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSuperNova extends AdvancedModelBase {
	private final ExtendedModelRenderer body1;

	public ModelSuperNova() {
		textureWidth = 64;
		textureHeight = 64;

		body1 = new ExtendedModelRenderer(this, 0, 0);
		body1.setRotationPoint(0.0F, 16.0F, 0.0F);
		body1.cubeList.add(new ModelBox(body1, 0, 0, -32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false));
	}

	public void render(Entity entity, float f5, float delta) {
		body1.rotateAngleX = 0;
		body1.rotateAngleY = 0;
		float ageFrac = (entity.ticksExisted + delta) / (float)(EntitySuperNova.DURATION);
		float scale = (float) (Math.pow(ageFrac, 0.5) * 2 + 0.05 * Math.cos((entity.ticksExisted + delta) * 3));
		body1.setHasLighting(true);
		body1.setOpacity((float) (1.0 - Math.pow(ageFrac, 4f)) * 0.4f);

		body1.setScale(scale * 0.4f, scale * 0.4f, scale * 0.4f);
		body1.render(f5);

		body1.setScale(scale * 0.6f, scale * 0.6f, scale * 0.6f);
		body1.render(f5);

		body1.setScale(scale * 0.8f, scale * 0.8f, scale * 0.8f);
		body1.render(f5);

		body1.setScale(scale, scale, scale);
		body1.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}