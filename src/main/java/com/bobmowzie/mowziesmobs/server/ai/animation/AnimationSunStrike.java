package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AnimationSunStrike<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
	protected EntityLivingBase entityTarget;
	private double prevX;
	private double prevZ;
	private int newX;
	private int newZ;
	private int y;

	public AnimationSunStrike(T entity, Animation animation) {
		super(entity, animation, false);
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		entityTarget = animatingEntity.getAttackTarget();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (entityTarget == null) {
			return;
		}
		if (animatingEntity.getAnimationTick() < 9) {
			animatingEntity.getLookHelper().setLookPositionWithEntity(entityTarget, 30, 30);
		}

		if (animatingEntity.getAnimationTick() == 1) {
			prevX = entityTarget.posX;
			prevZ = entityTarget.posZ;
		}
		if (animatingEntity.getAnimationTick() == 7) {
			double x = entityTarget.posX;
			y = MathHelper.floor(entityTarget.posY - 1);
			double z = entityTarget.posZ;
			double vx = (x - prevX) / 9;
			double vz = (z - prevZ) / 9;
			int t = EntitySunstrike.STRIKE_EXPLOSION + 3;
			newX = MathHelper.floor(x + vx * t);
			newZ = MathHelper.floor(z + vz * t);
			for (int i = 0; i < 5; i++) {
				if (!animatingEntity.world.canBlockSeeSky(new BlockPos(newX, y, newZ))) {
					y++;
				} else {
					break;
				}
			}
		}
		if (!animatingEntity.world.isRemote && animatingEntity.getAnimationTick() == 9) {
			animatingEntity.playSound(MMSounds.ENTITY_BARAKO_ATTACK, 1.4f, 1);
			EntitySunstrike sunstrike = new EntitySunstrike(animatingEntity.world, animatingEntity, newX, y, newZ);
			sunstrike.onSummon();
			animatingEntity.world.spawnEntity(sunstrike);
		}
		if (animatingEntity.getAnimationTick() > 6) {
			animatingEntity.getLookHelper().setLookPosition(newX, y, newZ, 20, 20);
		}
	}
}
