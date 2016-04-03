package com.bobmowzie.mowziesmobs.common.animation;

import com.bobmowzie.mowziesmobs.common.entity.MMEntityBase;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationBabyFoliaathEatAI<T extends MMEntityBase & IAnimatedEntity> extends AnimationAI<T> {
    public AnimationBabyFoliaathEatAI(T entity, Animation animation) {
        super(entity, animation);
    }
}
