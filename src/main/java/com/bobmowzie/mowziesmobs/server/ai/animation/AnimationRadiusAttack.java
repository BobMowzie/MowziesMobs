package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

public class AnimationRadiusAttack<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    private final float radius;
    private final float damageMultiplier;
    private final float applyKnockbackMultiplier;
    private final int damageFrame;
    private final boolean pureapplyKnockback;

    public AnimationRadiusAttack(T entity, Animation animation, float radius, float damageMultiplier, float applyKnockbackMultiplier, int damageFrame, boolean pureapplyKnockback) {
        super(entity, animation);
        this.radius = radius;
        this.damageMultiplier = damageMultiplier;
        this.applyKnockbackMultiplier = applyKnockbackMultiplier;
        this.damageFrame = damageFrame;
        this.pureapplyKnockback = pureapplyKnockback;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() == damageFrame) {
            List<LivingEntity> hit = entity.getEntityLivingBaseNearby(radius, 2 * radius, radius, radius);
            for (LivingEntity aHit : hit) {
                if (entity instanceof EntityUmvuthi && aHit instanceof LeaderSunstrikeImmune) {
                    continue;
                }
                entity.doHurtTarget(aHit, damageMultiplier, applyKnockbackMultiplier);
                if (pureapplyKnockback && !aHit.isInvulnerable()) {
                    if (aHit instanceof Player && ((Player)aHit).getAbilities().invulnerable) continue;
                    double angle = entity.getAngleBetweenEntities(entity, aHit);
                    double x = applyKnockbackMultiplier * Math.cos(Math.toRadians(angle - 90));
                    double z = applyKnockbackMultiplier * Math.sin(Math.toRadians(angle - 90));
                    aHit.setDeltaMovement(x, 0.3, z);
                    if (aHit instanceof ServerPlayer) {
                        ((ServerPlayer) aHit).connection.send(new ClientboundSetEntityMotionPacket(aHit));
                    }
                }
            }
        }
    }
}