package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleRing;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Josh on 11/16/2018.
 */
public class EntityPoisonBall extends EntityMagicEffect implements IProjectile {

    public static float GRAVITY = 0.05f;

    public EntityPoisonBall(World worldIn) {
        super(worldIn);
        setSize(0, 0);
    }

    public EntityPoisonBall(World worldIn, EntityLivingBase caster) {
        super(worldIn);
        setSize(0, 0);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        motionX = x * velocity;
        motionY = y * velocity;
        motionZ = z * velocity;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        motionY -= GRAVITY;
        move(MoverType.SELF, motionX, motionY, motionZ);

        List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(1);
        if (!entitiesHit.isEmpty()) {
            for (EntityLivingBase entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity.getIsInvulnerable()) continue;
                if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) continue;
                if (entity instanceof EntityNaga) continue;
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), 3 * MowziesMobs.CONFIG.attackScaleNaga);
                entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 1, false, true));
            }
        }

        if (world.collidesWithAnyBlock(getEntityBoundingBox().grow(1,1,1))) explode();

        if (world.isRemote) {
            float scale = 1f;
            for (int i = 0; i < 4; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double value = rand.nextFloat() * 0.1f;
                MMParticle.CLOUD.spawn(world, posX + xSpeed, posY + ySpeed, posZ + zSpeed, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.1d + value, 0.4d, 0.1d + value, 2, scale * (10d + rand.nextDouble() * 20d), 20, ParticleCloud.EnumCloudBehavior.GROW));
            }
            for (int i = 0; i < 4; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double value = rand.nextFloat() * 0.15f;
                MMParticle.CLOUD.spawn(world, posX + xSpeed, posY + ySpeed, posZ + zSpeed, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.3d + value, 1d, 0.3d + value, 2, scale * (3d + rand.nextDouble() * 20d), 13, ParticleCloud.EnumCloudBehavior.GROW));
            }
            for (int i = 0; i < 3; i++) {
                double xSpeed = scale * 0.1 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.1 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.1 * (rand.nextFloat() * 2 - 1);
                MMParticle.CLOUD.spawn(world, posX + xSpeed, posY + ySpeed, posZ + zSpeed, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.08d, 0.16d, 0.08d, 0, scale * 2d, 20, ParticleCloud.EnumCloudBehavior.CONSTANT));
            }
        }
        if (ticksExisted > 50) setDead();
    }

    private void explode() {
        float explodeSpeed = 3.5f;
        if (world.isRemote) {
            for (int i = 0; i < 13; i++) {
                Vec3d particlePos = new Vec3d(Math.random() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.15f;
                MMParticle.CLOUD.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0.1d + value, 0.4d, 0.1d + value, 2, 10d + rand.nextDouble() * 20d, 70, ParticleCloud.EnumCloudBehavior.GROW, 0.7d));
            }
            for (int i = 0; i < 13; i++) {
                Vec3d particlePos = new Vec3d(Math.random() * 0.2, 0, 0);
                particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.15f;
                MMParticle.CLOUD.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0.3d + value, 1d, 0.3d + value, 2, 10d + rand.nextDouble() * 20d, 70, ParticleCloud.EnumCloudBehavior.GROW, 0.7d));
            }
            for (int i = 0; i < 9; i++) {
                Vec3d particlePos = new Vec3d(Math.random() * 0.25, 0, 0);
                particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
                MMParticle.CLOUD.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 0.1d, 0.2d, 0.1d, 0, 2d, 30, ParticleCloud.EnumCloudBehavior.CONSTANT, 0.7d));
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
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), 3 * MowziesMobs.CONFIG.attackScaleNaga);
                entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 0, false, true));
            }
        }

        setDead();
    }
}
