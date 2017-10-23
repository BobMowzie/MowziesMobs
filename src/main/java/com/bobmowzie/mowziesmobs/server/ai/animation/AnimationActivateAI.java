package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class AnimationActivateAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
	public AnimationActivateAI(T entity, Animation animation) {
		super(entity, animation);
	}

	@Override
	public void resetTask() {
		super.resetTask();
		animatingEntity.active = true;
	}
}
