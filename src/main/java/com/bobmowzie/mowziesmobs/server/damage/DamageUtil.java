package com.bobmowzie.mowziesmobs.server.damage;

import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;

import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

public class DamageUtil {
    // TODO: Works for current use cases, but possibly not for future edge cases. Use reflection to get hurt sound for onHit2?
    public static Pair<Boolean, Boolean> dealMixedDamage(EntityLivingBase target, DamageSource source1, float amount1, DamageSource source2, float amount2) {
        MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(target, MowzieLivingProperties.class);
        if (property != null) {
            property.lastDamage = -1;
            boolean hit1 = target.attackEntityFrom(source1, amount1);
            boolean hit1Registered = hit1;
            if (property.lastDamage != -1) hit1Registered = true;
            boolean hit2 = target.attackEntityFrom(source2, amount2 + property.lastDamage);
            if (hit2 && hit1Registered) {
                onHit2(target, source2);
                if (target instanceof EntityPlayer) {
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

    private static float getSoundPitch(EntityLivingBase target) {
        return (target.getRNG().nextFloat() - target.getRNG().nextFloat()) * 0.2F + 1.0F;
    }

    private static void onHit2(EntityLivingBase target, DamageSource source) {
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
            double d1 = entity1.posX - target.posX;
            double d0;

            for (d0 = entity1.posZ - target.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
            {
                d1 = (Math.random() - Math.random()) * 0.01D;
            }

            target.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (180D / Math.PI) - (double)target.rotationYaw);
            target.knockBack(entity1, 0.4F, d1, d0);
        }
        else
        {
            target.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
        }
    }
}
