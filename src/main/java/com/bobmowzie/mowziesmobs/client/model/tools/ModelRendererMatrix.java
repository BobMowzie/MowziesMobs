package com.bobmowzie.mowziesmobs.client.model.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.resources.model.ModelRenderer;
import net.minecraft.world.phys.Matrix3f;
import net.minecraft.world.phys.Matrix4f;
import net.minecraft.world.phys.Vector3f;

public class ModelRendererMatrix extends ModelRenderer {
    private Matrix4f worldXform;
    private Matrix3f worldNormal;

    public ModelRendererMatrix(ModelRenderer original) {
        super((int) original.textureWidth, (int) original.textureHeight, original.textureOffsetX, original.textureOffsetY);
        copyModelAngles(original);
        cubeList.addAll(original.cubeList);
        childModels.addAll(original.childModels);

        worldNormal = new Matrix3f();
        worldNormal.setIdentity();
        worldXform = new Matrix4f();
        worldXform.setIdentity();
    }

    @Override
    public void translateRotate(MatrixStack matrixStackIn) {
        if (getWorldNormal() == null || getWorldXform() == null) {
            super.translateRotate(matrixStackIn);
        }
        else {
            MatrixStack.Entry last = matrixStackIn.getLast();
            last.getMatrix().setIdentity();
            last.getNormal().setIdentity();
            last.getMatrix().mul(getWorldXform());
            last.getNormal().mul(getWorldNormal());
        }
    }

    @Override
    public void copyModelAngles(ModelRenderer modelRendererIn) {
        if (modelRendererIn instanceof ModelRendererMatrix) {
            ModelRendererMatrix other = (ModelRendererMatrix) modelRendererIn;
            this.setWorldNormal(other.getWorldNormal());
            this.setWorldXform(other.getWorldXform());
        }
        else {
            super.copyModelAngles(modelRendererIn);
        }
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
}
