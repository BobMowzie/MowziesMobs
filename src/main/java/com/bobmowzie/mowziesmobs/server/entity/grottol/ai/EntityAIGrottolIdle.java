package com.bobmowzie.mowziesmobs.server.entity.grottol.ai;

import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.util.SoundEvents;

public class EntityAIGrottolIdle extends SimpleAnimationAI<EntityGrottol> {
    private static final Animation ANIMATION = Animation.create(47);

    public EntityAIGrottolIdle(EntityGrottol entity) {
        super(entity, ANIMATION, false);
    }

    @Override
    public boolean shouldExecute() {
        return entity.getAnimation() == IAnimatedEntity.NO_ANIMATION && entity.getRNG().nextInt(180) == 0;
    }

    @Override
    public void startExecuting() {
        AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, ANIMATION);
        super.startExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() == 28 || entity.getAnimationTick() == 33) {
            entity.playSound(SoundEvents.BLOCK_STONE_STEP, 0.5F, 1.4F);
        }
    }

    public static Animation animation() {
        return ANIMATION;
    }
}
