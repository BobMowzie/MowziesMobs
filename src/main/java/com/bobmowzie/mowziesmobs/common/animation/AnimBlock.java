package com.bobmowzie.mowziesmobs.common.animation;

import thehippomaster.AnimationAPI.IAnimatedEntity;

/**
 * Created by jnad325 on 8/3/15.
 */
public class AnimBlock extends MMAnimBase {
    public AnimBlock(IAnimatedEntity entity, int id, int duration) {
        super(entity, id, duration);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        animatingEntity.faceEntity(animatingEntity.blockingEntity, 100, 100);
        animatingEntity.getLookHelper().setLookPositionWithEntity(animatingEntity.blockingEntity, 200F, 30F);
    }
}