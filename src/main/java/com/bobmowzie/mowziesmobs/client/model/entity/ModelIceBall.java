package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ExtendedModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.Entity;

public class ModelIceBall<T extends EntityIceBall> extends AdvancedModelBase<T> {
	private final ExtendedModelRenderer body1;
	private final ExtendedModelRenderer body2;
	private final ExtendedModelRenderer core;

	public ModelIceBall() {
		textureWidth = 128;
		textureHeight = 64;

		body1 = new ExtendedModelRenderer(this, 32, 0);
		body1.setRotationPoint(0.0F, 16.0F, 0.0F);
		body1.addBox(new ModelBox(body1, 8, 0, -12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F, false));

		body2 = new ExtendedModelRenderer(this, 32, 0);
		body2.setRotationPoint(0.0F, 16.0F, 0.0F);
		setRotationAngle(body2, 0.7854F, 0.0F, 0.7854F);
		body2.addBox(new ModelBox(body2, 8, 0, -12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F, false));

		core = new ExtendedModelRenderer(this, 8, 0);
		core.setRotationPoint(0.0F, 16.0F, 0.0F);
		core.addBox(new ModelBox(core, 0, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false));

		body1.setOpacity(0.65f);
		body2.setOpacity(0.65f);
	}

	public void render(Entity entity, float f5, float delta) {
		body1.rotateAngleX = 0;
		body1.rotateAngleY = 0;
		body1.rotateAngleZ = -(entity.ticksExisted + delta) * 0.4f;
		body2.rotateAngleX = 0.7854F;
		body2.rotateAngleY = 0.7854F + (entity.ticksExisted + delta) * 0.4f;
		body2.rotateAngleZ = 0.7854F + (entity.ticksExisted + delta) * 0.4f;
		body2.isHidden = false;

		core.rotateAngleZ = (entity.ticksExisted + delta) * 0.4f;

		core.render(f5);
		body1.render(f5);
		body2.render(f5);
	}

	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}