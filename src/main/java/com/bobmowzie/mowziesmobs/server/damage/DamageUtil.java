package com.bobmowzie.mowziesmobs.server.damage;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

public class DamageUtil {
    // TODO: Works for current use cases, but possibly not for future edge cases. Use reflection to get hurt sound for onHit2?
    public static Pair<Boolean, Boolean> dealMixedDamage(LivingEntity target, DamageSource source1, float amount1, DamageSource source2, float amount2) {
        if (target.world.isRemote()) return Pair.of(false, false);
        LivingCapability.ILivingCapability lastDamageCapability = CapabilityHandler.getCapability(target, LivingCapability.LivingProvider.LIVING_CAPABILITY);
        if (lastDamageCapability != null) {
            lastDamageCapability.setLastDamage(-1);
            float damageSoFar = 0;
            float origLastDamage = target.lastDamage;
            boolean hit1 = target.attackEntityFrom(source1, amount1);
            boolean hit1Registered = hit1;
            if (lastDamageCapability.getLastDamage() != -1) {
                hit1Registered = true;
            }
            if (lastDamageCapability.getLastDamage() != 0) {
                damageSoFar += amount1;
            }
            target.lastDamage = Math.max(target.lastDamage - amount1, 0);
            lastDamageCapability.setLastDamage(-1);
            boolean hit2 = target.attackEntityFrom(source2, amount2);
            boolean hit2Registered = hit2;
            if (lastDamageCapability.getLastDamage() != -1) {
                hit2Registered = true;
            }
            if (lastDamageCapability.getLastDamage() != 0) {
                damageSoFar += amount2;
            }
            target.lastDamage = origLastDamage;
            if (damageSoFar > target.lastDamage) target.lastDamage = damageSoFar;

            if (hit2 && hit1Registered) {
                onHit2(target, source2);
                if (target instanceof PlayerEntity) {
                    SoundEvent sound = SoundEvents.ENTITY_PLAYER_HURT;
                    if (source2 == DamageSource.ON_FIRE) sound = SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
                    else if (source2 == DamageSource.DROWN) sound = SoundEvents.ENTITY_PLAYER_HURT_DROWN;
                    target.playSound(sound, 1F, getSoundPitch(target));
                }
            }
            return Pair.of(hit1, hit2);
        }
        return Pair.of(false, false);
    }

    private static float getSoundPitch(LivingEntity target) {
        return (target.getRNG().nextFloat() - target.getRNG().nextFloat()) * 0.2F + 1.0F;
    }

    private static void onHit2(LivingEntity target, DamageSource source) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
        {
            target.world.setEntityState(target, (byte)33);
        }
        else
        {
            byte b0;

            if (source == DamageSource.DROWN)
            {
                b0 = 36;
            }
            else if (source.isFireDamage())
            {
                b0 = 37;
            }
            else
            {
                b0 = 2;
            }

            target.world.setEntityState(target, b0);
        }

        Entity entity1 = source.getTrueSource();
        if (entity1 != null)
        {
            double d1 = entity1.getPosX() - target.getPosX();
            double d0;

            for (d0 = entity1.getPosZ() - target.getPosZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
            {
                d1 = (Math.random() - Math.random()) * 0.01D;
            }

            target.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (180D / Math.PI) - (double)target.rotationYaw);
            target.applyKnockback(0.4F, d1, d0);
        }
        else
        {
            target.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
        }
    }
}
