package com.bobmowzie.mowziesmobs.server.damage;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;

public class DamageUtil {
    // TODO: Works for current use cases, but possibly not for future edge cases. Use reflection to get hurt sound for onHit2?
    public static Pair<Boolean, Boolean> dealMixedDamage(LivingEntity target, DamageSource source1, float amount1, DamageSource source2, float amount2) {
        if (target.level.isClientSide()) return Pair.of(false, false);
        LivingCapability.ILivingCapability lastDamageCapability = CapabilityHandler.getCapability(target, LivingCapability.LivingProvider.LIVING_CAPABILITY);
        if (lastDamageCapability != null) {
            lastDamageCapability.setLastDamage(-1);
            float damageSoFar = 0;
            float origLastDamage = target.lastHurt;
            boolean hit1 = target.hurt(source1, amount1);
            boolean hit1Registered = hit1;
            if (lastDamageCapability.getLastDamage() != -1) {
                hit1Registered = true;
            }
            if (lastDamageCapability.getLastDamage() != 0) {
                damageSoFar += amount1;
            }
            target.lastHurt = Math.max(target.lastHurt - amount1, 0);
            lastDamageCapability.setLastDamage(-1);
            boolean hit2 = target.hurt(source2, amount2);
            boolean hit2Registered = hit2;
            if (lastDamageCapability.getLastDamage() != -1) {
                hit2Registered = true;
            }
            if (lastDamageCapability.getLastDamage() != 0) {
                damageSoFar += amount2;
            }
            target.lastHurt = origLastDamage;
            if (damageSoFar > target.lastHurt) target.lastHurt = damageSoFar;

            if (hit2 && hit1Registered) {
                onHit2(target, source2);
                if (target instanceof Player) {
                    SoundEvent sound = SoundEvents.PLAYER_HURT;
                    if (source2 == DamageSource.ON_FIRE) sound = SoundEvents.PLAYER_HURT_ON_FIRE;
                    else if (source2 == DamageSource.DROWN) sound = SoundEvents.PLAYER_HURT_DROWN;
                    target.playSound(sound, 1F, getSoundPitch(target));
                }
            }
            return Pair.of(hit1, hit2);
        }
        return Pair.of(false, false);
    }

    private static float getSoundPitch(LivingEntity target) {
        return (target.getRandom().nextFloat() - target.getRandom().nextFloat()) * 0.2F + 1.0F;
    }

    private static void onHit2(LivingEntity target, DamageSource source) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).isThorns())
        {
            target.level.broadcastEntityEvent(target, (byte)33);
        }
        else
        {
            byte b0;

            if (source == DamageSource.DROWN)
            {
                b0 = 36;
            }
            else if (source.isFire())
            {
                b0 = 37;
            }
            else
            {
                b0 = 2;
            }

            target.level.broadcastEntityEvent(target, b0);
        }

        Entity entity1 = source.getEntity();
        if (entity1 != null)
        {
            double d1 = entity1.getX() - target.getX();
            double d0;

            for (d0 = entity1.getZ() - target.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
            {
                d1 = (Math.random() - Math.random()) * 0.01D;
            }

            target.hurtDir = (float)(Mth.atan2(d0, d1) * (180D / Math.PI) - (double)target.yRot);
            target.knockback(0.4F, d1, d0);
        }
        else
        {
            target.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
        }
    }
}
