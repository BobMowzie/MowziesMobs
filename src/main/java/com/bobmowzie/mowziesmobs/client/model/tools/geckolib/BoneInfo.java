package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.util.math.vector.Matrix4f;

public class BoneInfo {
    public Matrix4f modelSpaceXform;

    public BoneInfo() {
        modelSpaceXform = new Matrix4f();
        modelSpaceXform.setIdentity();
    }

}
