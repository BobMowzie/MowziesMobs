package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ExtendedModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPoisonBall extends AdvancedModelBase {
	private final ExtendedModelRenderer inner;
	private final ExtendedModelRenderer outer;

	public ModelPoisonBall() {
		textureWidth = 32;
		textureHeight = 32;

		inner = new ExtendedModelRenderer(this);
		inner.setRotationPoint(0.0F, 3.5F, 0.0F);
		inner.cubeList.add(new ModelBox(inner, 0, 16, -3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F, false));

		outer = new ExtendedModelRenderer(this);
		outer.setRotationPoint(0.0F, 3.5F, 0.0F);
		outer.cubeList.add(new ModelBox(outer, 0, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false));

		inner.setOpacity(1f);
		outer.setOpacity(0.6f);
	}

	public void render(Entity entity, float f5, float delta) {
		EntityPoisonBall poisonBall = (EntityPoisonBall)entity;
		float dx = (float) (poisonBall.prevMotionX + (poisonBall.motionX - poisonBall.prevMotionX) * delta);
		float dy = (float) (entity.motionY + (entity.motionY - poisonBall.prevMotionY) * delta);
		float dz = (float) (entity.motionZ + (entity.motionZ - poisonBall.prevMotionZ) * delta);
		double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
		if (d != 0) {
			double a = dy / d;
			a = Math.max(-1, Math.min(1, a));
			float pitch = -(float) Math.asin(a);
			inner.rotateAngleX = pitch + (float)Math.PI / 2f;
			outer.rotateAngleX = pitch + (float)Math.PI / 2f;
		}

		inner.render(f5);
		outer.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}