package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.ilexiconn.llibrary.client.model.tools.BasicModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelSuperNova<T extends EntitySuperNova> extends AdvancedModelBase<T> {
	private final AdvancedModelRenderer body1;
	private final AdvancedModelRenderer body2;
	private final AdvancedModelRenderer body3;
	private final AdvancedModelRenderer body4;

	public ModelSuperNova() {
		textureWidth = 64;
		textureHeight = 64;

		body1 = new AdvancedModelRenderer(this, 0, 0);
		body1.setRotationPoint(0.0F, 16.0F, 0.0F);
		body1.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
		body2 = new AdvancedModelRenderer(this, 0, 0);
		body2.setRotationPoint(0.0F, 16.0F, 0.0F);
		body2.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
		setRotationAngle(body2, (float)Math.PI / 2, (float)Math.PI / 2, (float)Math.PI / 2);
		body3 = new AdvancedModelRenderer(this, 0, 0);
		body3.setRotationPoint(0.0F, 16.0F, 0.0F);
		body3.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
		setRotationAngle(body3, (float)Math.PI, (float)Math.PI, (float)Math.PI);
		body4 = new AdvancedModelRenderer(this, 0, 0);
		body4.setRotationPoint(0.0F, 16.0F, 0.0F);
		body4.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
		setRotationAngle(body4, 1.5f * (float)Math.PI, 1.5f * (float)Math.PI, 1.5f * (float)Math.PI);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		body4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		body3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		body2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float ageFrac = ageInTicks / (float)(EntitySuperNova.DURATION);
		float scale = (float) (Math.pow(ageFrac, 0.5) * 3 + 0.05 * Math.cos(ageInTicks * 3));
		float opacity = (float) Math.max((1.0 - Math.pow(ageFrac, 4f)) * 0.4f, 0.00001);
		body1.setOpacity(opacity);
		body2.setOpacity(opacity);
		body3.setOpacity(opacity);
		body4.setOpacity(opacity);

		body4.setScale(scale * 0.4f, scale * 0.4f, scale * 0.4f);
		body3.setScale(scale * 0.6f, scale * 0.6f, scale * 0.6f);
		body2.setScale(scale * 0.8f, scale * 0.8f, scale * 0.8f);
		body1.setScale(scale, scale, scale);
	}

	public void setRotationAngle(BasicModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}