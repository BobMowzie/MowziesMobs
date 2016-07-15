package com.bobmowzie.mowziesmobs.client.model.tools;

import net.minecraft.util.math.MathHelper;

/**
 * This is a timer that can be used to easily animate models between poses. You have to set the number of
 * ticks between poses, increase or decrease the timer, and get the percentage using a specific function.
 *
 * @author RafaMv
 */
public class ControlledAnimation {
    /**
     * It is the timer used to animate
     */
    private int timer;

    /**
     * It is the limit time, the maximum value that the timer can be. I represents the duration of the
     * animation
     */
    private int duration;

    private int timerChange;

    public ControlledAnimation(int d) {
        timer = 0;
        duration = d;
    }

    /**
     * Sets the duration of the animation in ticks. Try values around 50.
     *
     * @param d is the maximum number of ticks that the timer can reach.
     */
    public void setDuration(int d) {
        timer = 0;
        duration = d;
    }

    /**
     * Returns the timer of this animation. Useful to save the progress of the animation.
     */
    public int getTimer() {
        return timer;
    }

    /**
     * Sets the timer to a specific value.
     *
     * @param time is the number of ticks to be set.
     */
    public void setTimer(int time) {
        timer = time;

        if (timer > duration) {
            timer = duration;
        } else if (timer < 0) {
            timer = 0;
        }
    }

    /**
     * Sets the timer to 0.
     */
    public void resetTimer() {
        timer = 0;
    }

    /**
     * Increases the timer by 1.
     */
    public void increaseTimer() {
        if (timer < duration) {
            timer++;
            timerChange = 1;
        }
    }

    /**
     * Checks if the timer can be increased
     */
    public boolean canIncreaseTimer() {
        return timer < duration;
    }

    /**
     * Increases the timer by a specific value.
     *
     * @param time is the number of ticks to be increased in the timer
     */
    public void increaseTimer(int time) {
        int newTime = timer + time;
        if (newTime <= duration && newTime >= 0) {
            timer = newTime;
        } else {
            timer = newTime < 0 ? 0 : duration;
        }
    }

    /**
     * Decreases the timer by 1.
     */
    public void decreaseTimer() {
        if (timer > 0.0D) {
            timer--;
            timerChange = -1;
        }
    }

    /**
     * Checks if the timer can be decreased
     */
    public boolean canDecreaseTimer() {
        return timer > 0.0D;
    }

    /**
     * Decreases the timer by a specific value.
     *
     * @param time is the number of ticks to be decreased in the timer
     */
    public void decreaseTimer(int time) {
        if (timer - time > 0.0D) {
            timer -= time;
        } else {
            timer = 0;
        }
    }

    /**
     * Returns a float that represents a fraction of the animation, a value between 0.0F and 1.0F.
     */
    public float getAnimationFraction() {
        return timer / (float) duration;
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using 1/(1 + e^(4-8*x)). It is quite uniform but slow, and needs if statements.
     */
    public float getAnimationProgressSmooth() {
        if (timer > 0.0D) {
            if (timer < duration) {
                return (float) (1.0D / (1.0D + Math.exp(4.0D - 8.0D * getAnimationFraction())));
            } else {
                return 1.0F;
            }
        }
        return 0.0F;
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using 1/(1 + e^(6-12*x)). It is quite uniform, but fast.
     */
    public float getAnimationProgressSteep() {
        return (float) (1.0D / (1.0D + Math.exp(6.0D - 12.0D * getAnimationFraction())));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function. It is fast in the beginning and slow in the end.
     */
    public float getAnimationProgressSin() {
        return MathHelper.sin(1.57079632679F * getAnimationFraction());
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function squared. It is very smooth.
     */
    public float getAnimationProgressSinSqrt() {
        float result = MathHelper.sin(1.57079632679F * getAnimationFraction());
        return result * result;
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function to the power of ten. It is slow in the beginning and fast in the end.
     */
    public float getAnimationProgressSinToTen() {
        return (float) Math.pow(MathHelper.sin(1.57079632679F * getAnimationFraction()), 10);
    }

    public float getAnimationProgressSinToTenWithoutReturn() {
        if (timerChange == -1) {
            return MathHelper.sin(1.57079632679F * getAnimationFraction()) * MathHelper.sin(1.57079632679F * getAnimationFraction());
        }
        return (float) Math.pow(MathHelper.sin(1.57079632679F * getAnimationFraction()), 10);
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function to a specific power "i."
     *
     * @param i is the power of the sine function.
     */
    public float getAnimationProgressSinPowerOf(int i) {
        return (float) Math.pow(MathHelper.sin(1.57079632679F * getAnimationFraction()), i);
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using x^2 / (x^2 + (1-x)^2). It is smooth.
     */
    public float getAnimationProgressPoly2() {
        float x = getAnimationFraction();
        float x2 = x * x;
        return x2 / (x2 + (1 - x) * (1 - x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using x^3 / (x^3 + (1-x)^3). It is steep.
     */
    public float getAnimationProgressPoly3() {
        float x = getAnimationFraction();
        float x3 = x * x * x;
        return x3 / (x3 + (1 - x) * (1 - x) * (1 - x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using x^n / (x^n + (1-x)^n). It is steeper when n increases.
     *
     * @param n is the power of the polynomial function.
     */
    public float getAnimationProgressPolyN(int n) {
        double x = getAnimationFraction();
        double xi = Math.pow(x, n);
        return (float) (xi / (xi + Math.pow(1.0D - x, n)));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using 0.5 + arctan(PI * (x - 0.5)) / 2.00776964. It is super smooth.
     */
    public float getAnimationProgressArcTan() {
        return (float) (0.5F + 0.49806510671F * Math.atan(3.14159265359D * (getAnimationFraction() - 0.5D)));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. This value
     * starts at 1.0F and ends at 1.0F. The equation used is 0.5 - 0.5 * cos(2 * PI * x + sin(2 * PI * x)). It
     * is smooth.
     */
    public float getAnimationProgressTemporary() {
        float x = 6.28318530718F * getAnimationFraction();
        return 0.5F - 0.5F * MathHelper.cos(x + MathHelper.sin(x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. This value
     * starts at 0.0F and ends at 0.0F. The equation used is sin(x * PI + sin(x * PI)). It is fast in the
     * beginning and slow in the end.
     */
    public float getAnimationProgressTemporaryFS() {
        float x = 3.14159265359F * getAnimationFraction();
        return MathHelper.sin(x + MathHelper.sin(x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. This value
     * starts at 1.0F and ends at 1.0F. The equation used is 0.5 + 0.5 * cos(2 PI * x + sin(2 * PI * x)). It
     * is smooth.
     */
    public float getAnimationProgressTemporaryInvesed() {
        float x = 6.28318530718F * getAnimationFraction();
        return 0.5F + 0.5F * MathHelper.cos(x + MathHelper.sin(x));
    }
}
