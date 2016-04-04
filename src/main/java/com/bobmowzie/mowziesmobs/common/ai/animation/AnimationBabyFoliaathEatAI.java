package com.bobmowzie.mowziesmobs.common.ai.animation;

import com.bobmowzie.mowziesmobs.common.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationBabyFoliaathEatAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationBabyFoliaathEatAI(T entity, Animation animation) {
        super(entity, animation);
    }
}
