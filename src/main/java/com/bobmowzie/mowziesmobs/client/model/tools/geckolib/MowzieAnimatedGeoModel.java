package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.HashMap;
import java.util.Map;

public abstract class MowzieAnimatedGeoModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
    public Map<String, BoneInfo> boneInfoMap;

    public MowzieAnimatedGeoModel() {
        boneInfoMap = new HashMap<>();
    }

    public void trackBone(String name) {
        boneInfoMap.put(name, new BoneInfo());
    }

}
