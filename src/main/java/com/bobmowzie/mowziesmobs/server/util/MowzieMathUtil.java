package com.bobmowzie.mowziesmobs.server.util;

import net.minecraft.resources.math.MathHelper;

public class MowzieMathUtil {
    public static float approachSmooth(float current, float previous, float desired, float desiredSpeed, float deltaSpeed) {
        float prevSpeed = current - previous;
        desiredSpeed = MathHelper.abs(desiredSpeed);
        desiredSpeed = current < desired ? desiredSpeed : -desiredSpeed;
        float speed = MathHelper.approach(prevSpeed, desiredSpeed, deltaSpeed);
        float speedApproachReduction = (float) (1.0f - Math.pow(MathHelper.clamp(-MathHelper.abs(current - desired)/MathHelper.abs(2 * desiredSpeed/deltaSpeed) + 1.0f, 0, 1), 4)); // Extra math to make speed smaller when current is close to desired
        speed *= speedApproachReduction;
        return current < desired ? MathHelper.clamp(current + speed, current, desired) : MathHelper.clamp(current + speed, desired, current);
    }

    public static float approachDegreesSmooth(float current, float previous, float desired, float desiredSpeed, float deltaSpeed) {
        float desiredDifference = MathHelper.warpDegreeDifference(current, desired);
        float previousDifference = MathHelper.warpDegreeDifference(current, previous);
        return approachSmooth(current, current + previousDifference, current + desiredDifference, desiredSpeed, deltaSpeed);
    }
}
