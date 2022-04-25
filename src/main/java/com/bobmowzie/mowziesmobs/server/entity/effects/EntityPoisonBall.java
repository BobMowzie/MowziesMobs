package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Created by BobMowzie on 11/16/2018.
 */
public class EntityPoisonBall extends EntityMagicEffect {

    public static float GRAVITY = 0.05f;

    public double prevMotionX, prevMotionY, prevMotionZ;

    public EntityPoisonBall(EntityType<? extends EntityPoisonBall> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityPoisonBall(EntityType<? extends EntityPoisonBall> type, Level worldIn, LivingEntity caster) {
        super(type, worldIn);
        if (!level.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        setDeltaMovement(x * velocity, y * velocity, z * velocity);
    }

    @Override
    public void tick() {
        prevMotionX = getDeltaMovement().x;
        prevMotionY = getDeltaMovement().y;
        prevMotionZ = getDeltaMovement().z;

        super.tick();
        setDeltaMovement(getDeltaMovement().subtract(0, GRAVITY, 0));
        move(MoverType.SELF, getDeltaMovement());

        yRot = -((float) Mth.atan2(getDeltaMovement().x, getDeltaMovement().z)) * (180F / (float)Math.PI);

        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(1);
        if (!entitiesHit.isEmpty()) {
            for (LivingEntity entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity instanceof EntityNaga) continue;
                if (entity.hurt(DamageSource.indirectMagic(this, caster), 3 * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier.get().floatValue())) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1, false, true));
                }
            }
        }

        if (!level.noCollision(this, getBoundingBox().inflate(0.1))) explode();

        if (level.isClientSide) {
            float scale = 1f;
            int steps = 4;
            double motionX = getDeltaMovement().x;
            double motionY = getDeltaMovement().y;
            double motionZ = getDeltaMovement().z;
            for (int step = 0; step < steps; step++) {
                double x = xo + step * (getX() - xo) / (double)steps;
                double y = yo + step * (getY() - yo) / (double)steps + getBbHeight() / 2f;
                double z = zo + step * (getZ() - zo) / (double)steps;
                for (int i = 0; i < 1; i++) {
                    double xSpeed = scale * 0.02 * (random.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.02 * (random.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.02 * (random.nextFloat() * 2 - 1);
                    double value = random.nextFloat() * 0.1f;
                    double life = random.nextFloat() * 10f + 15f;
                    ParticleVanillaCloudExtended.spawnVanillaCloud(level, x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed, scale, 0.25d + value, 0.75d + value, 0.25d + value, 0.99, life);
                }
                for (int i = 0; i < 2; i++) {
                    double xSpeed = scale * 0.06 * (random.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.06 * (random.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.06 * (random.nextFloat() * 2 - 1);
                    double value = random.nextFloat() * 0.1f;
                    double life = random.nextFloat() * 5f + 10f;
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL.get(), x + xSpeed - motionX * 0.5, y + ySpeed - motionY * 0.5, z + zSpeed - motionZ * 0.5, xSpeed, ySpeed, zSpeed, true, 0, 0, 0, 0, scale * 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.99, life * 0.9, false, true);
                }
                for (int i = 0; i < 1; i++) {
                    if (random.nextFloat() < 0.9f) {
                        double xSpeed = scale * 0.06 * (random.nextFloat() * 2 - 1);
                        double ySpeed = scale * 0.06 * (random.nextFloat() * 2 - 1);
                        double zSpeed = scale * 0.06 * (random.nextFloat() * 2 - 1);
                        double value = random.nextFloat() * 0.1f;
                        double life = random.nextFloat() * 5f + 10f;
                        AdvancedParticleBase.spawnParticle(level, ParticleHandler.BUBBLE.get(), x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.85, life, false, true);
                    }
                }
            }
        }
        if (tickCount > 50) remove();
    }

    private void explode() {
        float explodeSpeed = 3.5f;
        if (level.isClientSide) {
            for (int i = 0; i < 26; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                double value = random.nextFloat() * 0.1f;
                double life = random.nextFloat() * 17f + 30f;
                ParticleVanillaCloudExtended.spawnVanillaCloud(level, getX(), getY(), getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
            }
            for (int i = 0; i < 26; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                double value = random.nextFloat() * 0.1f;
                double life = random.nextFloat() * 5f + 10f;
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL.get(), getX() + particlePos.x, getY() + particlePos.y, getZ() + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
            }
            for (int i = 0; i < 23; i++) {
                Vec3 particlePos = new Vec3(random.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.yRot((float) (random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.xRot((float) (random.nextFloat() * 2 * Math.PI));
                double value = random.nextFloat() * 0.1f;
                double life = random.nextFloat() * 10f + 20f;
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.BUBBLE.get(), getX() + particlePos.x, getY() + particlePos.y, getZ() + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
            }
        }

        playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1, 1);

        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (LivingEntity entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity instanceof EntityNaga) continue;
                if (entity.hurt(DamageSource.indirectMagic(this, caster), 3 * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier.get().floatValue())) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0, false, true));
                }
            }
        }

        remove();
    }
}
