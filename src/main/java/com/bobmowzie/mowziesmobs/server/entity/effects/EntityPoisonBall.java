package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Josh on 11/16/2018.
 */
public class EntityPoisonBall extends EntityMagicEffect implements IProjectile {

    public static float GRAVITY = 0.05f;

    public double prevMotionX, prevMotionY, prevMotionZ;

    public EntityPoisonBall(World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.5f);
    }

    public EntityPoisonBall(World worldIn, EntityLivingBase caster) {
        super(worldIn);
        setSize(0.5f, 0.5f);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        motionX = x * velocity;
        motionY = y * velocity;
        motionZ = z * velocity;
    }

    @Override
    public void onUpdate() {
        prevMotionX = motionX;
        prevMotionY = motionY;
        prevMotionZ = motionZ;

        super.onUpdate();
        motionY -= GRAVITY;
        move(MoverType.SELF, motionX, motionY, motionZ);

        rotationYaw = -((float) MathHelper.atan2(motionX, motionZ)) * (180F / (float)Math.PI);

        List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(1);
        if (!entitiesHit.isEmpty()) {
            for (EntityLivingBase entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity.getIsInvulnerable()) continue;
                if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) continue;
                if (entity instanceof EntityNaga) continue;
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), 3 * ConfigHandler.NAGA.combatData.attackMultiplier);
                entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 1, false, true));
            }
        }

        if (world.collidesWithAnyBlock(getEntityBoundingBox().grow(0.1,0.1,0.1))) explode();

        if (world.isRemote) {
            float scale = 1f;
            int steps = 4;
            for (int step = 0; step < steps; step++) {
                double x = prevPosX + step * (posX - prevPosX) / (double)steps;
                double y = prevPosY + step * (posY - prevPosY) / (double)steps + height / 2f;
                double z = prevPosZ + step * (posZ - prevPosZ) / (double)steps;
                for (int i = 0; i < 1; i++) {
                    double xSpeed = scale * 0.02 * (rand.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.02 * (rand.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.02 * (rand.nextFloat() * 2 - 1);
                    double value = rand.nextFloat() * 0.1f;
                    double life = rand.nextFloat() * 10f + 15f;
                    ParticleVanillaCloudExtended.spawnVanillaCloud(world, x, y, z, xSpeed, ySpeed, zSpeed, scale, 0.25d + value, 0.75d + value, 0.25d + value, 0.99, life);
                }
                for (int i = 0; i < 2; i++) {
                    double xSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                    double value = rand.nextFloat() * 0.1f;
                    double life = rand.nextFloat() * 10f + 15f;
                    MowzieParticleBase.spawnParticle(world, MMParticle.PIXEL, x + xSpeed - motionX * 0.5, y + ySpeed - motionY * 0.5, z + zSpeed - motionZ * 0.5, xSpeed, ySpeed, zSpeed, 0, 0, 0,scale * 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.99, life * 0.9, true);
                }
                for (int i = 0; i < 1; i++) {
                    if (rand.nextFloat() < 0.5f) {
                        double xSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                        double ySpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                        double zSpeed = scale * 0.06 * (rand.nextFloat() * 2 - 1);
                        double value = rand.nextFloat() * 0.1f;
                        double life = rand.nextFloat() * 10f + 15f;
                        MowzieParticleBase.spawnParticle(world, MMParticle.BUBBLE, x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed, 0, 0, 0,3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.85, life, true);
                    }
                }
            }
        }
        if (ticksExisted > 50) setDead();
    }

    private void explode() {
        float explodeSpeed = 3.5f;
        if (world.isRemote) {
            for (int i = 0; i < 26; i++) {
                Vec3d particlePos = new Vec3d(Math.random() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.1f;
                double life = rand.nextFloat() * 10f + 20f;
                ParticleVanillaCloudExtended.spawnVanillaCloud(world, posX, posY, posZ, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
            }
            for (int i = 0; i < 26; i++) {
                Vec3d particlePos = new Vec3d(Math.random() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.1f;
                double life = rand.nextFloat() * 10f + 20f;
                MowzieParticleBase.spawnParticle(world, MMParticle.PIXEL, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0, 0, 0,3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, true);
            }
            for (int i = 0; i < 13; i++) {
                Vec3d particlePos = new Vec3d(Math.random() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.1f;
                double life = rand.nextFloat() * 10f + 20f;
                MowzieParticleBase.spawnParticle(world, MMParticle.BUBBLE, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0, 0, 0,3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, true);
            }
        }

        playSound(MMSounds.ENTITY_NAGA_ACID_HIT, 1, 1);

        List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (EntityLivingBase entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity.getIsInvulnerable()) continue;
                if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) continue;
                if (entity instanceof EntityNaga) continue;
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), 3 * ConfigHandler.NAGA.combatData.attackMultiplier);
                entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 0, false, true));
            }
        }

        setDead();
    }
}
