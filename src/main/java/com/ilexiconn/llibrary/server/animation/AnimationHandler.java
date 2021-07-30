package com.ilexiconn.llibrary.server.animation;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.ilexiconn.llibrary.server.event.AnimationEvent;
import com.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum AnimationHandler {
    INSTANCE;

    /**
     * Sends an animation packet to all clients, notifying them of a changed animation
     *
     * @param entity    the entity with an animation to be updated
     * @param animation the animation to be updated
     * @param <T>       the entity type
     */
    public <T extends Entity & IAnimatedEntity> void sendAnimationMessage(T entity, Animation animation) {
        if (entity.world.isRemote) {
            return;
        }
        entity.setAnimation(animation);
        MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new AnimationMessage(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
    }

    /**
     * Updates all animations for a given entity
     *
     * @param entity the entity with an animation to be updated
     * @param <T>    the entity type
     */
    public <T extends Entity & IAnimatedEntity> void updateAnimations(T entity) {
        if (entity.getAnimation() == null) {
            entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
        } else {
            if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
                if (entity.getAnimationTick() == 0) {
                    AnimationEvent event = new AnimationEvent.Start<>(entity, entity.getAnimation());
                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        this.sendAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getAnimationTick() < entity.getAnimation().getDuration()) {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                    MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick<>(entity, entity.getAnimation(), entity.getAnimationTick()));
                }
                if (entity.getAnimationTick() == entity.getAnimation().getDuration()) {
                    if( ! entity.getAnimation().doesLoop()) {
                        entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                    }
                    entity.setAnimationTick(0);
                }
            }
        }
    }
}
