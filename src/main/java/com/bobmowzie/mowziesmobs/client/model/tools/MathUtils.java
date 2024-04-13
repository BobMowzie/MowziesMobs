package com.bobmowzie.mowziesmobs.client.model.tools;

import org.joml.Quaternionf;

/**
 * @author Paul Fulham
 */
public final class MathUtils {
    public static final float TAU = (float) (2 * StrictMath.PI);

    public static final float PI = (float) StrictMath.PI;

    public static double linearTransformd(double x, double domainMin, double domainMax, double rangeMin, double rangeMax) {
        x = x < domainMin ? domainMin : x > domainMax ? domainMax : x;
        return (rangeMax - rangeMin) * (x - domainMin) / (domainMax - domainMin) + rangeMin;
    }

    public static double fit(double pct, double lbound, double hbound, double start, double end) {
        double npct = (pct - lbound) / (hbound - lbound);
        npct = Math.max(Math.min(1, npct), 0);
        return start + npct * (end - start);
    }

    public static Quaternionf quatFromRotationXYZ(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= ((float)Math.PI / 180F);
            y *= ((float)Math.PI / 180F);
            z *= ((float)Math.PI / 180F);
        }
        return (new Quaternionf()).rotationXYZ(x, y, z);
    }
}
