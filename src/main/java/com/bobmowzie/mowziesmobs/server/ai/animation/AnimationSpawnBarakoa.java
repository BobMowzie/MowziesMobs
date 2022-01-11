package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.entity.SpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.IServerWorld;

import java.util.EnumSet;

public class AnimationSpawnBarakoa extends SimpleAnimationAI<EntityBarako> {
    private boolean spawnSunblockers;

    public AnimationSpawnBarakoa(EntityBarako entity, Animation animation, boolean spawnSunblockers) {
        super(entity, animation);
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        this.spawnSunblockers = spawnSunblockers;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entity.barakoaSpawnCount++;
        entity.playSound(MMSounds.ENTITY_BARAKOA_INHALE.get(), 1.2f, 0.5f);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        if (entity.barakoaSpawnCount < 3 && (entity.targetDistance > 6 || entity.getAttackTarget() == null || spawnSunblockers)) {
            if (spawnSunblockers) AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityBarako.SPAWN_SUNBLOCKERS_ANIMATION);
            else AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityBarako.SPAWN_ANIMATION);
        } else {
            entity.barakoaSpawnCount = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();
//        if (entity.getAnimationTick() == 1) {
//            entity.playSound(MMSounds.ENTITY_BARAKOA_INHALE, 1.2f, 0.5f);
        if (entity.getAnimationTick() == 6) {
            entity.playSound(MMSounds.ENTITY_BARAKO_BELLY.get(), 1.5f, 1);
            entity.playSound(MMSounds.ENTITY_BARAKOA_BLOWDART.get(), 1.5f, 0.5f);
            double angle = entity.getYRot()Head;
            if (angle < 0) {
                angle = angle + 360;
            }
            if (angle - entity.getYRot() > 70) {
                angle = 70 + entity.getYRot();
            } else if (angle - entity.getYRot() < -70) {
                angle = -70 + entity.getYRot();
            }
            EntityBarakoaVillager barakoa;
            if (spawnSunblockers) {
                barakoa = new EntityBarakoaya(EntityHandler.BARAKOAYA, entity.world);
                ((EntityBarakoaya)barakoa).hasTriedOrSucceededTeleport = false;
            }
            else barakoa = new EntityBarakoaVillager(EntityHandler.BARAKOA_VILLAGER, entity.world);
            barakoa.setPositionAndRotation(entity.getPosX() + 2 * Math.sin(-angle * (Math.PI / 180)), entity.getPosY() + 1.5, entity.getPosZ() + 2 * Math.cos(-angle * (Math.PI / 180)), entity.getYRot()Head, 0);
            barakoa.setActive(false);
            barakoa.active = false;
            barakoa.onInitialSpawn((IServerWorld) entity.getEntityWorld(), entity.world.getDifficultyForLocation(barakoa.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
            barakoa.setHomePosAndDistance(entity.getHomePosition(), 25);
            if (entity.getTeam() instanceof ScorePlayerTeam) {
                barakoa.world.getScoreboard().addPlayerToTeam(barakoa.getScoreboardName(), (ScorePlayerTeam) entity.getTeam());
            }
            entity.world.addEntity(barakoa);
            barakoa.setMotion(0.7 * Math.sin(-angle * (Math.PI / 180)), 0.5, 0.7 * Math.cos(-angle * (Math.PI / 180)));
            barakoa.setAttackTarget(entity.getAttackTarget());
            if (entity.getAttackTarget() instanceof Player) {
                barakoa.setMisbehavedPlayerId(entity.getAttackTarget().getUniqueID());
            }
        }
    }
}
