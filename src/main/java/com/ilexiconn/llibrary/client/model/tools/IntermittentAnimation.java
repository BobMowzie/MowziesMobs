package com.ilexiconn.llibrary.client.model.tools;

import com.ilexiconn.llibrary.server.entity.IIntermittentEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * This is a timer that can be used to easily animate models with intermittent poses. You have to set the
 * number of ticks between poses, a number of ticks that represents the interval of the pose change, increase
 * or decrease the timer, and get the percentage using a specific function.
 *
 * @author RafaMv
 * @author Paul Fulham
 */
@OnlyIn(Dist.CLIENT)
public class IntermittentAnimation<T extends Entity & IIntermittentEntity> {
    /**
     * It is the random used to randomize the movement.
     */
    private final Random rand = new Random();

    private final T entity;
    /**
     * It is the timer used to animate.
     */
    private int timeRunning;
    /**
     * It is the limit time, the maximum value that the timer can be. I represents the duration of the
     * animation.
     */
    private int duration;
    /**
     * It is a boolean that shows if the animation is already in the new pose.
     */
    private boolean isRunning;
    /**
     * It is an inverter for the timer.
     */
    private int runDirection;
    /**
     * It is the timer used for the interval.
     */
    private int timeIdle;
    /**
     * It is the interval to return to the first animation.
     */
    private final int minIdleTime;
    /**
     * It is the chance to go to the new animation.
     */
    private final int startProbability;

    private final boolean isOperator;

    private byte id;

    public IntermittentAnimation(T entity, int duration, int intervalDuration, int startPropbability, boolean isOperator) {
        this.entity = entity;
        this.duration = duration;
        this.minIdleTime = intervalDuration;
        this.startProbability = startPropbability;
        this.isOperator = isOperator;
        this.timeRunning = 0;
        this.isRunning = false;
        this.runDirection = -1;
    }

    public void setID(byte id) {
        this.id = id;
    }

    /**
     * Sets the duration of the animation in ticks. Try values around 50.
     *
     * @param duration is the maximum number of ticks that the timer can reach.
     */
    public void setDuration(int duration) {
        this.timeRunning = 0;
        this.duration = duration;
    }

    /**
     * Returns the timer of this animation. Useful to save the progress of the animation.
     */
    public int getTimeRunning() {
        return this.timeRunning;
    }

    /**
     * Sets the timer to a specific value.
     *
     * @param timeRunning is the number of ticks to be set.
     */
    public void setTimeRunning(int timeRunning) {
        this.timeRunning = timeRunning;

        if (this.timeRunning > this.duration) {
            this.timeRunning = this.duration;
        } else if (this.timeRunning < 0) {
            this.timeRunning = 0;
        }
    }

    /**
     * Sets the timer to 0.
     */
    public void resetTimeRunning() {
        this.timeRunning = 0;
    }

    /**
     * Increases the timer by 1.
     */
    public void update() {
        if (this.isRunning) {
            if (this.timeRunning < this.duration && this.timeRunning > 0) {
                this.timeRunning += this.runDirection;
            } else {
                if (this.timeRunning >= this.duration) {
                    this.timeRunning = this.duration;
                } else if (this.timeRunning <= 0) {
                    this.timeRunning = 0;
                }
                this.timeIdle = 0;
                this.isRunning = false;
            }
        } else if (this.isOperator) {
            if (this.timeIdle < this.minIdleTime) {
                this.timeIdle++;
            } else {
                if (this.random.nextInt(this.startProbability) == 0) {
                    this.start();
                    this.entity.level.broadcastEntityEvent(this.entity, (byte) (this.entity.getOffsetEntityState() + this.id));
                }
            }
        }
    }

    public void start() {
        this.runDirection = -this.runDirection;
        this.timeRunning += this.runDirection;
        this.isRunning = true;
    }

    /**
     * Decreases the timer by 1.
     */
    public void stop() {
        if (this.timeRunning > 0) {
            this.timeRunning--;
        } else {
            this.timeRunning = 0;
            this.isRunning = false;
            this.timeIdle = 0;
            this.runDirection = 1;
        }
    }

    /**
     * Decreases the timer by a specific value.
     *
     * @param timeDelta is the number of ticks to be decreased in the timer
     */
    public void stop(int timeDelta) {
        if (this.timeRunning - timeDelta > 0) {
            this.timeRunning -= timeDelta;
        } else {
            this.timeRunning = 0;
            this.isRunning = false;
            this.timeIdle = 0;
            this.runDirection = 1;
        }
    }

    /**
     * Returns a float that represents a fraction of the animation, a value between 0.0F and 1.0F.
     */
    public float getAnimationFraction() {
        return this.timeRunning / (float) this.duration;
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using 1/(1 + e^(4-8*x)). It is quite uniform but slow, and needs if statements.
     */
    public float getAnimationProgressSmooth() {
        if (this.timeRunning > 0) {
            if (this.timeRunning < this.duration) {
                return (float) (1.0D / (1.0D + Math.exp(4.0D - 8.0D * this.getAnimationFraction())));
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
        return (float) (1.0D / (1.0D + Math.exp(6.0D - 12.0D * this.getAnimationFraction())));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function. It is fast in the beginning and slow in the end.
     */
    public float getAnimationProgressSin() {
        return Mth.sin(1.57079632679F * this.getAnimationFraction());
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function squared. It is very smooth.
     */
    public float getAnimationProgressSinSqrt() {
        float result = Mth.sin(1.57079632679F * this.getAnimationFraction());
        return result * result;
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function to the power of ten. It is slow in the beginning and fast in the end.
     */
    public float getAnimationProgressSinToTen() {
        return (float) Math.pow(Mth.sin(1.57079632679F * this.getAnimationFraction()), 10);
    }

    public float getAnimationProgressSinToTenWithoutReturn() {
        if (this.runDirection == -1) {
            return Mth.sin(1.57079632679F * this.getAnimationFraction()) * Mth.sin(1.57079632679F * this.getAnimationFraction());
        }
        return (float) Math.pow(Mth.sin(1.57079632679F * this.getAnimationFraction()), 10);
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using a sine function to a specific power "i."
     *
     * @param i is the power of the sine function.
     */
    public float getAnimationProgressSinPowerOf(int i) {
        return (float) Math.pow(Mth.sin(1.57079632679F * this.getAnimationFraction()), i);
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using x^2 / (x^2 + (1-x)^2). It is smooth.
     */
    public float getAnimationProgressPoly2() {
        float x = this.getAnimationFraction();
        float x2 = x * x;
        return x2 / (x2 + (1 - x) * (1 - x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using x^3 / (x^3 + (1-x)^3). It is steep.
     */
    public float getAnimationProgressPoly3() {
        float x = this.getAnimationFraction();
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
        double x = this.timeRunning / this.duration;
        double xi = Math.pow(x, n);
        return (float) (xi / (xi + Math.pow(1.0D - x, n)));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. It reaches
     * 1.0F using 0.5 + arctan(PI * (x - 0.5)) / 2.00776964. It is super smooth.
     */
    public float getAnimationProgressArcTan() {
        return (float) (0.5F + 0.49806510671F * Math.atan(3.14159265359D * (this.timeRunning / this.duration - 0.5D)));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. This value
     * starts at 1.0F and ends at 1.0F. The equation used is 0.5 - 0.5 * cos(2 * PI * x + sin(2 * PI * x)). It
     * is smooth.
     */
    public float getAnimationProgressTemporary() {
        float x = 6.28318530718F * this.getAnimationFraction();
        return 0.5F - 0.5F * Mth.cos(x + Mth.sin(x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. This value
     * starts at 0.0F and ends at 0.0F. The equation used is sin(x * PI + sin(x * PI)). It is fast in the
     * beginning and slow in the end.
     */
    public float getAnimationProgressTemporaryFS() {
        float x = 3.14159265359F * this.getAnimationFraction();
        return Mth.sin(x + Mth.sin(x));
    }

    /**
     * Returns a value between 0.0F and 1.0F depending on the timer and duration of the animation. This value
     * starts at 1.0F and ends at 1.0F. The equation used is 0.5 + 0.5 * cos(2 PI * x + sin(2 * PI * x)). It
     * is smooth.
     */
    public float getAnimationProgressTemporaryInvesed() {
        float x = 6.28318530718F * this.getAnimationFraction();
        return 0.5F + 0.5F * Mth.cos(x + Mth.sin(x));
    }
}
