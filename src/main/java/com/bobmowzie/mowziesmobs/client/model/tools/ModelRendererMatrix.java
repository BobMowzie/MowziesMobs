package com.bobmowzie.mowziesmobs.client.model.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;

public class ModelRendererMatrix extends ModelRenderer {
    private Matrix4f worldXform;
    private Matrix3f worldNormal;

    private boolean useMatrixMode;

    public ModelRendererMatrix(ModelRenderer original) {
        super((int) original.textureWidth, (int) original.textureHeight, original.textureOffsetX, original.textureOffsetY);
        copyModelAngles(original);
        cubeList.addAll(original.cubeList);
        childModels.addAll(original.childModels);

        worldNormal = new Matrix3f();
        worldNormal.setIdentity();
        worldXform = new Matrix4f();
        worldXform.setIdentity();

        useMatrixMode = true;
    }

    @Override
    public void translateRotate(MatrixStack matrixStackIn) {
        if (!useMatrixMode || getWorldNormal() == null || getWorldXform() == null) {
            super.translateRotate(matrixStackIn);
        }
        else {
            MatrixStack.Entry last = matrixStackIn.getLast();
            last.getMatrix().setIdentity();
            last.getNormal().setIdentity();
            last.getMatrix().mul(getWorldXform());
            last.getNormal().mul(getWorldNormal());
        }
        useMatrixMode = false;
    }

    @Override
    public void copyModelAngles(ModelRenderer modelRendererIn) {
        if (modelRendererIn instanceof ModelRendererMatrix) {
            ModelRendererMatrix other = (ModelRendererMatrix) modelRendererIn;
            this.setWorldNormal(other.getWorldNormal());
            this.setWorldXform(other.getWorldXform());
        }
        super.copyModelAngles(modelRendererIn);
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
