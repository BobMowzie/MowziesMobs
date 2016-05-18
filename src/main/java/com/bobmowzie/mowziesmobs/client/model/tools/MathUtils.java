package com.bobmowzie.mowziesmobs.client.model.tools;

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
}
