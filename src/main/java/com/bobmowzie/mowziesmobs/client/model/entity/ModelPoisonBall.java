package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ExtendedModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class ModelPoisonBall extends AdvancedModelBase {
	private final ExtendedModelRenderer inner;
	private final ExtendedModelRenderer outer;

	public ModelPoisonBall() {
		textureWidth = 32;
		textureHeight = 32;

		inner = new ExtendedModelRenderer(this);
		inner.setRotationPoint(0.0F, 3.5F, 0.0F);
		inner.addBox(new ModelBox(inner, 0, 16, -3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F, false));

		outer = new ExtendedModelRenderer(this);
		outer.setRotationPoint(0.0F, 3.5F, 0.0F);
		outer.addBox(new ModelBox(outer, 0, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false));

		inner.setOpacity(1f);
		outer.setOpacity(0.6f);
	}

	public void render(Entity entity, float f5, float delta) {
		EntityPoisonBall poisonBall = (EntityPoisonBall)entity;
		Vec3d prevV = new Vec3d(poisonBall.prevMotionX, poisonBall.prevMotionY, poisonBall.prevMotionZ);
		Vec3d dv = prevV.add(poisonBall.getMotion().subtract(prevV).scale(delta));
		double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
		if (d != 0) {
			double a = dv.y / d;
			a = Math.max(-1, Math.min(1, a));
			float pitch = -(float) Math.asin(a);
			inner.rotateAngleX = pitch + (float)Math.PI / 2f;
			outer.rotateAngleX = pitch + (float)Math.PI / 2f;
		}

		inner.render(f5);
		outer.render(f5);
	}

	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}