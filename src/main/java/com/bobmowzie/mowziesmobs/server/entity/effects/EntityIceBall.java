package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particles.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability.IFrozenCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Josh on 9/2/2018.
 */
public class EntityIceBall extends EntityMagicEffect implements IProjectile {
    public EntityIceBall(World world) {
        super(EntityHandler.ICE_BALL, world);
    }

    public EntityIceBall(EntityType<? extends EntityIceBall> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityIceBall(EntityType<? extends EntityIceBall> type, World worldIn, LivingEntity caster) {
        this(type, worldIn);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void tick() {
        super.tick();
        move(MoverType.SELF, getMotion());

        if (ticksExisted == 1) {
            if (world.isRemote) {
                MowziesMobs.PROXY.playIceBreathSound(this);
            }
        }

        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (LivingEntity entity : entitiesHit) {
                if (entity == caster) continue;
                List<String> freezeImmune = Arrays.asList(ConfigHandler.getFreezeBlacklist());
                ResourceLocation mobName = EntityType.getKey(entity.getType());
                if (freezeImmune.contains(mobName.toString())) continue;
                if (entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, caster), 3f * ConfigHandler.MOBS.FROSTMAW.combatData.attackMultiplier)) {
                    IFrozenCapability capability = CapabilityHandler.getCapability(entity, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
                    if (capability != null) capability.addFreezeProgress(entity, 1);
                }
            }
        }

        if (world.checkBlockCollision(getBoundingBox().grow(0.15))) {
            explode();
        }

        if (world.isRemote) {
            float scale = 2f;
            double x = posX;
            double y = posY + getHeight() / 2;
            double z = posZ;
            double motionX = getMotion().x;
            double motionY = getMotion().y;
            double motionZ = getMotion().z;
            for (int i = 0; i < 4; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                float value = rand.nextFloat() * 0.15f;
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f + value, 0.75f + value,1f, scale * (10f + rand.nextFloat() * 20f), 20, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xSpeed, y + ySpeed, z + zSpeed, xSpeed, ySpeed, zSpeed);
            }
            for (int i = 0; i < 1; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 1f, 1f, 1f, scale * (5f + rand.nextFloat() * 10f), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x, y, z, xSpeed, ySpeed, zSpeed);
            }

            for (int i = 0; i < 5; i++) {
                double xSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x - 20 * (xSpeed) + motionX, y - 20 * ySpeed + motionY, z - 20 * zSpeed + motionZ, xSpeed, ySpeed, zSpeed);
            }

            float yaw = (float) Math.atan2(motionX, motionZ);
            float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
            if (ticksExisted % 3 == 0) {
                world.addParticle(new ParticleRing.RingData(yaw, pitch, 40, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), x + 1.5f * motionX, y + 1.5f *motionY, z + 1.5f * motionZ, 0, 0, 0);
            }

            if (ticksExisted == 1) {
                world.addParticle(new ParticleRing.RingData(yaw, pitch, 20, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, ParticleRing.EnumRingBehavior.GROW), x, y, z, 0, 0, 0);
            }
        }
        if (ticksExisted > 50) remove();
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        setMotion(x * velocity, y * velocity, z * velocity);
    }

    private void explode() {
        if (world.isRemote) {
            for (int i = 0; i < 8; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.3, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                float value = rand.nextFloat() * 0.15f;
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f + value, 0.75f + value, 1f, 10f + rand.nextFloat() * 20f, 40, ParticleCloud.EnumCloudBehavior.GROW, 1f), posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
            }
            for (int i = 0; i < 10; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.3, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                world.addParticle(new ParticleSnowFlake.SnowflakeData(40, false), posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
            }
        }
        remove();
    }
}
