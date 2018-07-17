package com.bobmowzie.mowziesmobs.server.entity.grottol.ai;

import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.init.SoundEvents;

public class EntityAIGrottolIdle extends AnimationAI<EntityGrottol> {
    private static final Animation ANIMATION = Animation.create(47);

    public EntityAIGrottolIdle(EntityGrottol entity) {
        super(entity, ANIMATION, false);
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }

    @Override
    public boolean shouldAnimate() {
        return entity.getAnimation() == IAnimatedEntity.NO_ANIMATION && entity.getRNG().nextInt(180) == 0;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entity.getAnimationTick() == 28 || entity.getAnimationTick() == 33) {
            entity.playSound(SoundEvents.BLOCK_STONE_STEP, 0.5F, 1.4F);
        }
    }

    public static Animation animation() {
        return ANIMATION;
    }
}
