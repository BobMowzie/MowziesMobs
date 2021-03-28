package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by BobMowzie on 11/16/2018.
 */
public class EntityPoisonBall extends EntityMagicEffect implements IProjectile {

    public static float GRAVITY = 0.05f;

    public double prevMotionX, prevMotionY, prevMotionZ;

    public EntityPoisonBall(EntityType<? extends EntityPoisonBall> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityPoisonBall(EntityType<? extends EntityPoisonBall> type, World worldIn, LivingEntity caster) {
        super(type, worldIn);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        setMotion(x * velocity, y * velocity, z * velocity);
    }

    @Override
    public void tick() {
        prevMotionX = getMotion().x;
        prevMotionY = getMotion().y;
        prevMotionZ = getMotion().z;

        super.tick();
        setMotion(getMotion().subtract(0, GRAVITY, 0));
        move(MoverType.SELF, getMotion());

        rotationYaw = -((float) MathHelper.atan2(getMotion().x, getMotion().z)) * (180F / (float)Math.PI);

        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(1);
        if (!entitiesHit.isEmpty()) {
            for (LivingEntity entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity instanceof EntityNaga) continue;
                if (entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, caster), 3 * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier.get().floatValue())) {
                    entity.addPotionEffect(new EffectInstance(Effects.POISON, 80, 1, false, true));
                }
            }
        }

        if (!world.hasNoCollisions(this, getBoundingBox().grow(0.1))) explode();

        if (world.isRemote) {
            float scale = 1f;
            int steps = 4;
            double motionX = getMotion().x;
            double motionY = getMotion().y;
            double motionZ = getMotion().z;
            for (int step = 0; step < steps; step++) {
                double x = prevPosX + step * (getPosX() - prevPosX) / (double)steps;
                double y = prevPosY + step * (getPosY() - prevPosY) / (double)steps + getHeight() / 2f;
                double z = prevPosZ + step * (getPosZ() - prevPosZ) / (double)steps;
                for (int i = 0; i < 1; i++) {
                    double xSpeed = scale * 0.02 * (rand.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.02 * (rand.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.02 * (rand.nextFloat() * 2 - 1);
                    double value = rand.nextFloat() * 0.1f;
                    double life = rand.nextFloat() * 10f + 15f;
                    ParticleVanillaCloudExtended.spawnVanillaCloud(world, x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed, scale, 0.25d + value, 0.75d + value, 0.25d + value, 0.99, life);
                }
                for (int i = 0; i < 2; i++) {
                    double xSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                    double value = rand.nextFloat() * 0.1f;
                    double life = rand.nextFloat() * 5f + 10f;
                    AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), x + xSpeed - motionX * 0.5, y + ySpeed - motionY * 0.5, z + zSpeed - motionZ * 0.5, xSpeed, ySpeed, zSpeed, true, 0, 0, 0, 0, scale * 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.99, life * 0.9, false);
                }
                for (int i = 0; i < 1; i++) {
                    if (rand.nextFloat() < 0.9f) {
                        double xSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                        double ySpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                        double zSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 5f + 10f;
                        AdvancedParticleBase.spawnParticle(world, ParticleHandler.BUBBLE.get(), x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.85, life, false);
                    }
                }
            }
        }
        if (ticksExisted > 50) remove();
    }

    private void explode() {
        float explodeSpeed = 3.5f;
        if (world.isRemote) {
            for (int i = 0; i < 26; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.1f;
                double life = rand.nextFloat() * 17f + 30f;
                ParticleVanillaCloudExtended.spawnVanillaCloud(world, getPosX(), getPosY(), getPosZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
            }
            for (int i = 0; i < 26; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.1f;
                double life = rand.nextFloat() * 5f + 10f;
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.PIXEL.get(), getPosX() + particlePos.x, getPosY() + particlePos.y, getPosZ() + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false);
            }
            for (int i = 0; i < 23; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.1f;
                double life = rand.nextFloat() * 10f + 20f;
                AdvancedParticleBase.spawnParticle(world, ParticleHandler.BUBBLE.get(), getPosX() + particlePos.x, getPosY() + particlePos.y, getPosZ() + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false);
            }
        }

        playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1, 1);

        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (LivingEntity entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity instanceof EntityNaga) continue;
                if (entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, caster), 3 * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier.get().floatValue())) {
                    entity.addPotionEffect(new EffectInstance(Effects.POISON, 80, 0, false, true));
                }
            }
        }

        remove();
    }
}
