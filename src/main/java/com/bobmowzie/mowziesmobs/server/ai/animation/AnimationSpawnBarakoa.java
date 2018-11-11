package com.bobmowzie.mowziesmobs.server.ai.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

public class AnimationSpawnBarakoa extends AnimationAI<EntityBarako> {
    public AnimationSpawnBarakoa(EntityBarako entity, Animation animation) {
        super(entity, animation);
        setMutexBits(8);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entity.barakoaSpawnCount++;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        if (entity.barakoaSpawnCount < 3 && entity.targetDistance > 5) {
            AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityBarako.SPAWN_ANIMATION);
        } else {
            entity.barakoaSpawnCount = 0;
        }
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entity.getAnimationTick() == 1) {
            entity.playSound(MMSounds.ENTITY_BARAKOA_INHALE, 1.2f, 0.5f);
        } else if (entity.getAnimationTick() == 7) {
            entity.playSound(MMSounds.ENTITY_BARAKO_BELLY, 1.5f, 1);
            entity.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART, 1.5f, 0.5f);
            double angle = entity.rotationYawHead;
            if (angle < 0) {
                angle = angle + 360;
            }
            if (angle - entity.rotationYaw > 70) {
                angle = 70 + entity.rotationYaw;
            } else if (angle - entity.rotationYaw < -70) {
                angle = -70 + entity.rotationYaw;
            }
            EntityBarakoaya barakoa = new EntityBarakoaya(entity.world);
            barakoa.setPositionAndRotation(entity.posX + 2 * Math.sin(-angle * (Math.PI / 180)), entity.posY + 1.5, entity.posZ + 2 * Math.cos(-angle * (Math.PI / 180)), entity.rotationYawHead, 0);
            barakoa.setActive(false);
            barakoa.active = false;
            entity.world.spawnEntity(barakoa);
            barakoa.onInitialSpawn(entity.world.getDifficultyForLocation(barakoa.getPosition()), null);
            barakoa.motionX = 0.7 * Math.sin(-angle * (Math.PI / 180));
            barakoa.motionY = 0.5;
            barakoa.motionZ = 0.7 * Math.cos(-angle * (Math.PI / 180));
            barakoa.setAttackTarget(entity.getAttackTarget());
        }
    }
}
