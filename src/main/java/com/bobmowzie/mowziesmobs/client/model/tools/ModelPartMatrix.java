package com.bobmowzie.mowziesmobs.client.model.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.model.geom.ModelPart;

public class ModelPartMatrix extends ModelPart {
    private Matrix4f worldXform;
    private Matrix3f worldNormal;

    private boolean resetUseMatrixMode;
    private boolean useMatrixMode;

    public ModelPartMatrix(ModelPart original) {
        this(original, true);
    }

    public ModelPartMatrix(ModelPart original, boolean resetUseMatrixMode) {
        super(original.cubes, original.children);
        copyFrom(original);

        worldNormal = new Matrix3f();
        worldNormal.setIdentity();
        worldXform = new Matrix4f();
        worldXform.setIdentity();

        useMatrixMode = true;
        this.resetUseMatrixMode = resetUseMatrixMode;
    }

    @Override
    public void translateAndRotate(PoseStack matrixStackIn) {
        if (!useMatrixMode || getWorldNormal() == null || getWorldXform() == null) {
            super.translateAndRotate(matrixStackIn);
        }
        else {
            PoseStack.Pose last = matrixStackIn.last();
            last.pose().setIdentity();
            last.normal().setIdentity();
            last.pose().multiply(getWorldXform());
            last.normal().mul(getWorldNormal());
        }
        if (resetUseMatrixMode) useMatrixMode = false;
    }

    @Override
    public void copyFrom(ModelPart modelRendererIn) {
        if (modelRendererIn instanceof ModelPartMatrix) {
            ModelPartMatrix other = (ModelPartMatrix) modelRendererIn;
            this.setWorldNormal(other.getWorldNormal());
            this.setWorldXform(other.getWorldXform());
        }
        super.copyFrom(modelRendererIn);
    }

    public Matrix3f getWorldNormal() {
        return worldNormal;
    }

    public void setWorldNormal(Matrix3f worldNormal) {
        this.worldNormal = worldNormal;
    }

    public Matrix4f getWorldXform() {
        return worldXform;
    }

    public void setWorldXform(Matrix4f worldXform) {
        this.worldXform = worldXform;
    }

    public void setUseMatrixMode(boolean useMatrixMode) {
        this.useMatrixMode = useMatrixMode;
    }

    public boolean isUseMatrixMode() {
        return useMatrixMode;
    }
}
