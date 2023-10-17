package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class SmartBodyHelper extends BodyRotationControl {
	private static final float MAX_ROTATE = 75;

	private static final int HISTORY_SIZE = 10;

	private final Mob entity;

	private int rotateTime;

	private float targetYawHead;

    private final double[] histPosX = new double[HISTORY_SIZE];

    private final double[] histPosZ = new double[HISTORY_SIZE];

	public SmartBodyHelper(Mob entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public void clientTick() {
        for (int i = histPosX.length - 1; i > 0; i--) {
            histPosX[i] = histPosX[i - 1];
            histPosZ[i] = histPosZ[i - 1];
        }
        histPosX[0] = entity.getX();
        histPosZ[0] = entity.getZ();
        double dx = delta(histPosX);
        double dz = delta(histPosZ);
        double distSq = dx * dx + dz * dz;
		if (distSq > 2.5e-7) {
			boolean isStrafing = false;
			if (entity instanceof MowzieEntity) {
				isStrafing = ((MowzieEntity)entity).isStrafing();
			}
			if (!isStrafing) {
				double moveAngle = (float) Mth.atan2(dz, dx) * (180 / (float) Math.PI) - 90;
				entity.yBodyRot += Mth.wrapDegrees(moveAngle - entity.yBodyRot) * 0.6F;
				this.targetYawHead = this.entity.yHeadRot;
				this.rotateTime = 0;
			}
			else {
				super.clientTick();
			}
        } else if (entity.getPassengers().isEmpty() || !(entity.getPassengers().get(0) instanceof Mob)) {
			float limit = MAX_ROTATE;
			if (Math.abs(entity.yHeadRot - targetYawHead) > 15) {
				rotateTime = 0;
				targetYawHead = entity.yHeadRot;
			} else {
				rotateTime++;
				final int speed = 10;
				if (rotateTime > speed) {
					limit = Math.max(1 - (rotateTime - speed) / (float) speed, 0) * MAX_ROTATE;
				}
			}
			entity.yBodyRot = approach(entity.yHeadRot, entity.yBodyRot, limit);
		}
	}

	private double delta(double[] arr) {
		return mean(arr, 0) - mean(arr, HISTORY_SIZE / 2);
	}

    private double mean(double[] arr, int start) {
        double mean = 0;
        for (int i = 0; i < HISTORY_SIZE / 2; i++) {
            mean += arr[i + start];
        }
        return mean / arr.length;
    }

	public static float approach(float target, float current, float limit) {
		float delta = Mth.wrapDegrees(current - target);
		if (delta < -limit) {
			delta = -limit;
		} else if (delta >= limit) {
			delta = limit;
		}
		return target + delta * 0.55F;
	}
}
