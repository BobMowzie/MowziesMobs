package com.bobmowzie.mowziesmobs.client.model.tools;


import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 * Created by Josh on 5/8/2017.
 */
public class SocketModelRenderer extends AdvancedModelRenderer{
    public SocketModelRenderer(AdvancedModelBase model) {
        super(model);
    }

    public Point3d getWorldPos(Entity entity) {
//        if (getParent() != null) {
//            return
//        }
//        else {
            Matrix4d entityTranslate = new Matrix4d();
            Matrix4d entityRotate = new Matrix4d();
            entityTranslate.set(new Vector3d(-entity.posX, -entity.posY, -entity.posZ));
            entityRotate.rotY(-Math.toRadians(entity.rotationYaw));
            Point3d rendererPos = new Point3d(rotationPointX, rotationPointY, rotationPointZ);
            entityTranslate.transform(rendererPos);
            entityRotate.transform(rendererPos);
            return rendererPos;
//        }
    }
}
