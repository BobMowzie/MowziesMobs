package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleRing;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
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
    public EntityIceBall(World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.5f);
    }

    public EntityIceBall(World worldIn, LivingEntity caster) {
        this(worldIn);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        move(MoverType.SELF, motionX, motionY, motionZ);

        if (ticksExisted == 1) {
            if (world.isRemote) {
                MowziesMobs.PROXY.playIceBreathSound(this);
            }
        }

        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (LivingEntity entity : entitiesHit) {
                if (entity == caster) continue;
                List<String> freezeImmune = Arrays.asList(ConfigHandler.GENERAL.freeze_blacklist);
                ResourceLocation mobName = EntityList.getKey(entity);
                if (mobName != null && freezeImmune.contains(mobName.toString())) continue;
                if (entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, caster), 3f * ConfigHandler.MOBS.FROSTMAW.combatData.attackMultiplier)) {
                    MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
                    if (property != null) property.addFreezeProgress(entity, 1);
                }
            }
        }

        if (world.collidesWithAnyBlock(getEntityBoundingBox().grow(0.15))) {
            explode();
        }

        if (world.isRemote) {
            float scale = 2f;
            double x = posX;
            double y = posY + height / 2;
            double z = posZ;
            for (int i = 0; i < 4; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double value = rand.nextFloat() * 0.15f;
                MMParticle.CLOUD.spawn(world, x + xSpeed, y + ySpeed, z + zSpeed, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.75d + value, 0.75d + value, 1d, 1, scale * (10d + rand.nextDouble() * 20d), 20, ParticleCloud.EnumCloudBehavior.SHRINK));
            }
            for (int i = 0; i < 1; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                MMParticle.CLOUD.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 1d, 1d, 1d, 1, scale * (5d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK));
            }

            for (int i = 0; i < 5; i++) {
                double xSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                MMParticle.SNOWFLAKE.spawn(world, x - 20 * (xSpeed) + motionX, y - 20 * ySpeed + motionY, z - 20 * zSpeed + motionZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed));
            }

            if (ticksExisted % 3 == 0) {
                float yaw = (float) Math.atan2(motionX, motionZ);
                float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
                MMParticle.RING.spawn(world, x + 1.5f * motionX, y + 1.5f *motionY, z + 1.5f * motionZ, ParticleFactory.ParticleArgs.get().withData(yaw, pitch, 40, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, 0f, 0f, 0f, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK));
            }

            if (ticksExisted == 1) {
                float yaw = (float) Math.atan2(motionX, motionZ);
                float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
                MMParticle.RING.spawn(world, x, y, z, ParticleFactory.ParticleArgs.get().withData(yaw, pitch, 20, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, 0f, 0f, 0f, ParticleRing.EnumRingBehavior.GROW));
            }
        }
        if (ticksExisted > 50) setDead();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    protected void readEntityFromNBT(CompoundNBT compound) {

    }

    @Override
    protected void writeEntityToNBT(CompoundNBT compound) {

    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        motionX = x * velocity;
        motionY = y * velocity;
        motionZ = z * velocity;
    }

    private void explode() {
        if (world.isRemote) {
            for (int i = 0; i < 8; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.3, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                double value = rand.nextFloat() * 0.15f;
                MMParticle.CLOUD.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x, particlePos.y, particlePos.z, 0.75d + value, 0.75d + value, 1d, 1, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW));
            }
            for (int i = 0; i < 10; i++) {
                Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.3, 0, 0);
                particlePos = particlePos.rotateYaw((float) (rand.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotatePitch((float) (rand.nextFloat() * 2 * Math.PI));
                MMParticle.SNOWFLAKE.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x, particlePos.y, particlePos.z));
            }
        }
        setDead();
    }
}
