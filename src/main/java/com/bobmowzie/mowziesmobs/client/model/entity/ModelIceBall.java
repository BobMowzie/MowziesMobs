package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

public class ModelIceBall<T extends EntityIceBall> extends AdvancedModelBase<T> {
	private final AdvancedModelRenderer body1;
	private final AdvancedModelRenderer body2;
	private final AdvancedModelRenderer core;

	public ModelIceBall() {
		texWidth = 128;
		texHeight = 64;

		body1 = new AdvancedModelRenderer(this, 32, 0);
		body1.setPos(0.0F, 16.0F, 0.0F);
		body1.addBox(-12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F, false);

		body2 = new AdvancedModelRenderer(this, 32, 0);
		body2.setPos(0.0F, 16.0F, 0.0F);
		setRotationAngle(body2, 0.7854F, 0.0F, 0.7854F);
		body2.addBox(-12.0F, -12.0F, -12.0F, 24, 24, 24, 0.0F, false);

		core = new AdvancedModelRenderer(this, 8, 0);
		core.setPos(0.0F, 16.0F, 0.0F);
		core.addBox( -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false);

		body1.setOpacity(0.65f);
		body2.setOpacity(0.65f);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		body1.xRot = 0;
		body1.yRot = 0;
		body1.zRot = -ageInTicks * 0.4f;
		body2.xRot = 0.7854F;
		body2.yRot = 0.7854F + ageInTicks * 0.4f;
		body2.zRot = 0.7854F + ageInTicks * 0.4f;
		body2.visible = true;

		core.zRot = ageInTicks * 0.4f;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		core.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		body2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}