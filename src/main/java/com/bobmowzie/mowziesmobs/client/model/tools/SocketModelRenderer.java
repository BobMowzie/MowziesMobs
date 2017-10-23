package com.bobmowzie.mowziesmobs.client.model.tools;


import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 * Created by Josh on 5/8/2017.
 */
public class SocketModelRenderer extends AdvancedModelRenderer {
	public SocketModelRenderer(AdvancedModelBase model) {
		super(model);
	}

	public Vec3d getWorldPos(Entity entity) {
		Vec3d modelPos = getModelPos(this, new Vec3d(rotationPointX / 16, rotationPointY / 16, rotationPointZ / 16));
		double x = modelPos.x;
		double y = modelPos.y + 1.5f;
		double z = modelPos.z;
		Matrix4d entityTranslate = new Matrix4d();
		Matrix4d entityRotate = new Matrix4d();
		entityTranslate.set(new Vector3d(entity.posX, entity.posY, entity.posZ));
		entityRotate.rotY(-Math.toRadians(entity.rotationYaw));
		Point3d rendererPos = new Point3d(x, y, z);
		entityRotate.transform(rendererPos);
		entityTranslate.transform(rendererPos);
		return new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ());
	}

	public Vec3d getModelPos(AdvancedModelRenderer modelRenderer, Vec3d recurseValue) {
		double x = recurseValue.x;
		double y = recurseValue.y;
		double z = recurseValue.z;
		Point3d rendererPos = new Point3d(x, y, z);

		AdvancedModelRenderer parent = modelRenderer.getParent();
		if (parent != null) {
			Matrix4d boxTranslate = new Matrix4d();
			Matrix4d boxRotateX = new Matrix4d();
			Matrix4d boxRotateY = new Matrix4d();
			Matrix4d boxRotateZ = new Matrix4d();
			boxTranslate.set(new Vector3d(parent.rotationPointX / 16, -parent.rotationPointY / 16, -parent.rotationPointZ / 16));
			boxRotateX.rotX(parent.rotateAngleX);
			boxRotateY.rotY(-parent.rotateAngleY);
			boxRotateZ.rotZ(-parent.rotateAngleZ);

			boxRotateX.transform(rendererPos);
			boxRotateY.transform(rendererPos);
			boxRotateZ.transform(rendererPos);
			boxTranslate.transform(rendererPos);

			return getModelPos(parent, new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ()));
		}
		return new Vec3d(rendererPos.getX(), rendererPos.getY(), rendererPos.getZ());
	}
}
