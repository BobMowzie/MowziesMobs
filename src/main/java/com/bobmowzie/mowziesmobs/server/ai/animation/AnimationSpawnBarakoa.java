package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;

public class AnimationSpawnBarakoa extends AnimationAI<EntityBarako> {
	public AnimationSpawnBarakoa(EntityBarako entity, Animation animation) {
		super(entity, animation);
		setMutexBits(8);
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		animatingEntity.barakoaSpawnCount++;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		if (animatingEntity.barakoaSpawnCount < 3 && animatingEntity.targetDistance > 5) {
			AnimationHandler.INSTANCE.sendAnimationMessage(animatingEntity, EntityBarako.SPAWN_ANIMATION);
		} else {
			animatingEntity.barakoaSpawnCount = 0;
		}
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (animatingEntity.getAnimationTick() == 1) {
			animatingEntity.playSound(MMSounds.ENTITY_BARAKOA_INHALE, 1.2f, 0.5f);
		} else if (animatingEntity.getAnimationTick() == 7) {
			animatingEntity.playSound(MMSounds.ENTITY_BARAKO_BELLY, 1.5f, 1);
			animatingEntity.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART, 1.5f, 0.5f);
			double angle = animatingEntity.rotationYawHead;
			if (angle < 0) {
				angle = angle + 360;
			}
			if (angle - animatingEntity.rotationYaw > 70) {
				angle = 70 + animatingEntity.rotationYaw;
			} else if (angle - animatingEntity.rotationYaw < -70) {
				angle = -70 + animatingEntity.rotationYaw;
			}
			EntityBarakoaya barakoa = new EntityBarakoaya(animatingEntity.world);
			barakoa.setPositionAndRotation(animatingEntity.posX + 2 * Math.sin(-angle * (Math.PI / 180)), animatingEntity.posY + 1.5, animatingEntity.posZ + 2 * Math.cos(-angle * (Math.PI / 180)), animatingEntity.rotationYawHead, 0);
			barakoa.setActive(false);
			barakoa.active = false;
			animatingEntity.world.spawnEntity(barakoa);
			barakoa.onInitialSpawn(animatingEntity.world.getDifficultyForLocation(barakoa.getPosition()), null);
			barakoa.motionX = 0.7 * Math.sin(-angle * (Math.PI / 180));
			barakoa.motionY = 0.5;
			barakoa.motionZ = 0.7 * Math.cos(-angle * (Math.PI / 180));
		}
	}
}
