package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class AnimationSpawnBarakoa extends SimpleAnimationAI<EntityBarako> {
    private boolean spawnSunblockers;

    public AnimationSpawnBarakoa(EntityBarako entity, Animation animation, boolean spawnSunblockers) {
        super(entity, animation);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        this.spawnSunblockers = spawnSunblockers;
    }

    @Override
    public void start() {
        super.start();
        entity.barakoaSpawnCount++;
        entity.playSound(MMSounds.ENTITY_BARAKOA_INHALE.get(), 1.2f, 0.5f);
    }

    @Override
    public void stop() {
        super.stop();
        if (entity.barakoaSpawnCount < 3 && (entity.targetDistance > 6 || entity.getTarget() == null || spawnSunblockers)) {
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
            double angle = entity.yHeadRot;
            if (angle < 0) {
                angle = angle + 360;
            }
            if (angle - entity.yRot > 70) {
                angle = 70 + entity.yRot;
            } else if (angle - entity.yRot < -70) {
                angle = -70 + entity.yRot;
            }
            EntityBarakoaVillager barakoa;
            if (spawnSunblockers) {
                barakoa = new EntityBarakoaya(EntityHandler.BARAKOAYA.get(), entity.level);
                ((EntityBarakoaya)barakoa).hasTriedOrSucceededTeleport = false;
            }
            else barakoa = new EntityBarakoaVillager(EntityHandler.BARAKOA_VILLAGER.get(), entity.level);
            barakoa.absMoveTo(entity.getX() + 2 * Math.sin(-angle * (Math.PI / 180)), entity.getY() + 1.5, entity.getZ() + 2 * Math.cos(-angle * (Math.PI / 180)), entity.yHeadRot, 0);
            barakoa.setActive(false);
            barakoa.active = false;
            barakoa.finalizeSpawn((ServerLevelAccessor) entity.getCommandSenderWorld(), entity.level.getCurrentDifficultyAt(barakoa.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            barakoa.restrictTo(entity.getRestrictCenter(), 25);
            if (entity.getTeam() instanceof PlayerTeam) {
                barakoa.level.getScoreboard().addPlayerToTeam(barakoa.getScoreboardName(), (PlayerTeam) entity.getTeam());
            }
            entity.level.addFreshEntity(barakoa);
            barakoa.setDeltaMovement(0.7 * Math.sin(-angle * (Math.PI / 180)), 0.5, 0.7 * Math.cos(-angle * (Math.PI / 180)));
            barakoa.setTarget(entity.getTarget());
            if (entity.getTarget() instanceof Player) {
                barakoa.setMisbehavedPlayerId(entity.getTarget().getUUID());
            }
        }
    }
}
