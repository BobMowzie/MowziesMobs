package com.bobmowzie.mowziesmobs.ai.animation;

import com.bobmowzie.mowziesmobs.entity.MMEntityBase;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class MMAnimBase extends AIAnimation
    {
        MMEntityBase animatingEntity;
        int duration;

        public MMAnimBase(IAnimatedEntity entity, int duration)
        {
            super(entity);
            this.duration = duration;
            animatingEntity = (MMEntityBase)entity;
        }

        @Override
        public int getAnimID()
        {
            return 1;
        }

        @Override
        public boolean isAutomatic()
        {
            return false;
        }

        @Override
        public int getDuration()
        {
            return duration;
        }

        @Override
        public void startExecuting()
        {
            super.startExecuting();
            animatingEntity.currentAnim = this;
        }

        @Override
        public void resetTask()
        {
            super.resetTask();
            animatingEntity.currentAnim = null;
        }
    }
