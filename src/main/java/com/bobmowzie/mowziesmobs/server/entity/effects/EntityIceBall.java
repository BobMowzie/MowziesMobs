package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particles.ParticleRing;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Josh on 9/2/2018.
 */
public class EntityIceBall extends EntityMagicEffect implements IProjectile {
    public EntityIceBall(World worldIn) {
        super(worldIn);
        setSize(0, 0);
        if (world.isRemote) {
            MowziesMobs.PROXY.playIceBreathSound(this);
        }
    }

    public EntityIceBall(World worldIn, EntityLivingBase caster) {
        super(worldIn);
        setSize(0, 0);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        move(MoverType.SELF, motionX, motionY, motionZ);

        List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(3);
        if (!entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (entity == caster) continue;
                if (entity.getIsInvulnerable()) continue;
                if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) continue;
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), 3 * MowziesMobs.CONFIG.attackScaleFrostmaw);
                MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
                if (property != null) property.freezeProgress += 1;
            }
        }

        if (world.collidesWithAnyBlock(getEntityBoundingBox().grow(1,1,1))) explode();

        if (world.isRemote) {
            float scale = 2f;
            for (int i = 0; i < 4; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double value = rand.nextFloat() * 0.15f;
                MMParticle.CLOUD.spawn(world, posX + xSpeed, posY + ySpeed, posZ + zSpeed, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.75d + value, 0.75d + value, 1d, 1, scale * (10d + rand.nextDouble() * 20d), 20, ParticleCloud.EnumCloudBehavior.SHRINK));
            }
            for (int i = 0; i < 1; i++) {
                double xSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (rand.nextFloat() * 2 - 1);
                MMParticle.CLOUD.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 1d, 1d, 1d, 1, scale * (5d + rand.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK));
            }

            for (int i = 0; i < 5; i++) {
                double xSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.05 * (rand.nextFloat() * 2 - 1);
                MMParticle.SNOWFLAKE.spawn(world, posX - 20 * (xSpeed) + motionX, posY - 20 * ySpeed + motionY, posZ - 20 * zSpeed + motionZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed));
            }

            if (ticksExisted % 3 == 0) {
                float yaw = (float) Math.atan2(motionX, motionZ);
                float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
                MMParticle.RING.spawn(world, posX + scale * motionX * 1.5, posY + motionY * 2, posZ + motionZ * 2, ParticleFactory.ParticleArgs.get().withData(yaw, pitch, 40, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, 0f, 0f, 0f, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK));
            }

            if (ticksExisted == 1) {
                float yaw = (float) Math.atan2(motionX, motionZ);
                float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
                MMParticle.RING.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(yaw, pitch, 20, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, 0f, 0f, 0f, ParticleRing.EnumRingBehavior.GROW));
            }
        }
        if (ticksExisted > 50) setDead();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        motionX = x * velocity;
        motionY = y * velocity;
        motionZ = z * velocity;
    }

    private void explode() {
        for (int i = 0; i < 8; i++) {
            Vec3d particlePos = new Vec3d(Math.random() * 0.2, 0, 0);
            particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
            particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
            double value = rand.nextFloat() * 0.15f;
            MMParticle.CLOUD.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x, particlePos.y, particlePos.z, 0.75d + value, 0.75d + value, 1d, 1, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW));
        }
        for (int i = 0; i < 10; i++) {
            Vec3d particlePos = new Vec3d(Math.random() * 0.2, 0, 0);
            particlePos = particlePos.rotateYaw((float) (Math.random() * 2 * Math.PI));
            particlePos = particlePos.rotatePitch((float) (Math.random() * 2 * Math.PI));
            MMParticle.SNOWFLAKE.spawn(world, posX + particlePos.x, posY + particlePos.y, posZ + particlePos.z, ParticleFactory.ParticleArgs.get().withData(particlePos.x, particlePos.y, particlePos.z));
        }
        setDead();
    }
}
