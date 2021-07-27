package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.HashMap;
import java.util.Map;

public abstract class MowzieAnimatedGeoModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
    public MowzieAnimatedGeoModel() {
    }

    public MowzieGeoBone getMowzieBone(String boneName) {
        IBone bone = this.getBone(boneName);
        return (MowzieGeoBone) bone;
    }
}
