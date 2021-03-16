package com.ilexiconn.llibrary.server.event;

import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class AnimationEvent<T extends Entity & IAnimatedEntity> extends Event {
    protected Animation animation;
    private final T entity;

    AnimationEvent(T entity, Animation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity() {
        return this.entity;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    @Cancelable
    public static class Start<T extends Entity & IAnimatedEntity> extends AnimationEvent<T> {
        public Start(T entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }

    public static class Tick<T extends Entity & IAnimatedEntity> extends AnimationEvent<T> {
        protected int tick;

        public Tick(T entity, Animation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick() {
            return this.tick;
        }
    }
}
