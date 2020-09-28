package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ExtendedModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.Entity;

public class ModelSuperNova extends AdvancedModelBase {
	private final ExtendedModelRenderer body1;
	private final ExtendedModelRenderer body2;
	private final ExtendedModelRenderer body3;
	private final ExtendedModelRenderer body4;

	public ModelSuperNova() {
		textureWidth = 64;
		textureHeight = 64;

		body1 = new ExtendedModelRenderer(this, 0, 0);
		body1.setRotationPoint(0.0F, 16.0F, 0.0F);
		body1.cubeList.add(new ModelBox(body1, 0, 0, -32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false));
		body2 = new ExtendedModelRenderer(this, 0, 0);
		body2.setRotationPoint(0.0F, 16.0F, 0.0F);
		body2.cubeList.add(new ModelBox(body2, 0, 0, -32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false));
		setRotationAngle(body2, (float)Math.PI / 2, (float)Math.PI / 2, (float)Math.PI / 2);
		body3 = new ExtendedModelRenderer(this, 0, 0);
		body3.setRotationPoint(0.0F, 16.0F, 0.0F);
		body3.cubeList.add(new ModelBox(body3, 0, 0, -32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false));
		setRotationAngle(body3, (float)Math.PI, (float)Math.PI, (float)Math.PI);
		body4 = new ExtendedModelRenderer(this, 0, 0);
		body4.setRotationPoint(0.0F, 16.0F, 0.0F);
		body4.cubeList.add(new ModelBox(body4, 0, 0, -32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false));
		setRotationAngle(body4, 1.5f * (float)Math.PI, 1.5f * (float)Math.PI, 1.5f * (float)Math.PI);
	}

	public void render(Entity entity, float f5, float delta) {
		float ageFrac = (entity.ticksExisted + delta) / (float)(EntitySuperNova.DURATION);
		float scale = (float) (Math.pow(ageFrac, 0.5) * 3 + 0.05 * Math.cos((entity.ticksExisted + delta) * 3));
		float opacity = (float) (1.0 - Math.pow(ageFrac, 4f)) * 0.4f;
		body1.setOpacity(opacity);
		body2.setOpacity(opacity);
		body3.setOpacity(opacity);
		body4.setOpacity(opacity);

		body4.setScale(scale * 0.4f, scale * 0.4f, scale * 0.4f);
		body4.render(f5);

		body3.setScale(scale * 0.6f, scale * 0.6f, scale * 0.6f);
		body3.render(f5);

		body2.setScale(scale * 0.8f, scale * 0.8f, scale * 0.8f);
		body2.render(f5);

		body1.setScale(scale, scale, scale);
		body1.render(f5);
	}

	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}