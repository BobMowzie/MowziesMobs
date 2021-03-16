package com.ilexiconn.llibrary.server.animation;

import net.minecraft.entity.Entity;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Animation {
    @Deprecated
    private int id;
    private final int duration;
    private boolean looping;

    protected Animation(int duration) {
        this.duration = duration;
    }

    /**
     * @param id       the animation id
     * @param duration the animation duration
     * @return an animation with the given id and duration
     * @deprecated use {@link Animation#create(int)} instead.
     */
    @Deprecated
    public static Animation create(int id, int duration) {
        Animation animation = Animation.create(duration);
        animation.id = id;
        return animation;
    }

    /**
     * @param duration the animation duration
     * @return an animation with the given id and duration
     * @since 1.1.0
     */
    public static Animation create(int duration) {
        return new Animation(duration);
    }

    /**
     * @return the id of this animation
     * @deprecated IDs aren't used anymore since 1.1.0.
     */
    @Deprecated
    public int getID() {
        return this.id;
    }

    /**
     * @return the duration of this animation
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Sets the 'looping' parameter of this animation. Used in {@link AnimationHandler#updateAnimations(Entity)}
     * @param loops true if the animation should loop, false if not
     * @return does the animation loop?
     */
    public Animation setLooping(boolean loops) {
        looping = loops;
        return this;
    }

    /**
     * Returns whether this animation is supposed to loop
     * @return does the animation loop?
     */
    public boolean doesLoop() {
        return looping;
    }
}
