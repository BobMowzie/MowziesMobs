package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by BobMowzie on 5/25/2017.
 */
public class EntityIceBreath extends EntityMagicEffect {
    private static final int RANGE = 10;
    private static final int ARC = 45;
    private static final int DAMAGE_PER_HIT = 1;

    public EntityIceBreath(EntityType<? extends EntityIceBreath> type, World world) {
        super(type, world);
    }

    public EntityIceBreath(EntityType<? extends EntityIceBreath> type, World world, LivingEntity caster) {
        super(type, world);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted == 1) {
            if (world.isRemote) {
                MowziesMobs.PROXY.playIceBreathSound(this);
            }
        }
        if (caster != null && !caster.isAlive()) this.remove();
        if (ticksExisted == 1) playSound(MMSounds.ENTITY_FROSTMAW_ICEBREATH_START.get(), 1, 0.6f);
        if (caster instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) caster;
            setPositionAndRotation(player.getPosX(), player.getPosY() + player.getStandingEyeHeight(player.getPose(), player.getSize(player.getPose())) - 0.5f, player.getPosZ(), player.rotationYaw, player.rotationPitch);
        }

        float yaw = (float) Math.toRadians(-rotationYaw);
        float pitch = (float) Math.toRadians(-rotationPitch);
        float spread = 0.25f;
        float speed = 0.56f;
        float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
        float yComp = (float) (Math.sin(pitch));
        float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
        if (world.isRemote) {
            if (ticksExisted % 8 == 0) {
                world.addParticle(new ParticleRing.RingData(yaw, -pitch, 40, 1f, 1f, 1f, 1f, 110f * spread, false, ParticleRing.EnumRingBehavior.GROW), getPosX(), getPosY(), getPosZ(), 0.5f * xComp, 0.5f * yComp, 0.5f * zComp);
            }

            for (int i = 0; i < 6; i++) {
                double xSpeed = speed * 1f * xComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
                double ySpeed = speed * 1f * yComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
                double zSpeed = speed * 1f * zComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
                world.addParticle(new ParticleSnowFlake.SnowflakeData(37f, true), getPosX(), getPosY(), getPosZ(), xSpeed, ySpeed, zSpeed);
            }
            for (int i = 0; i < 5; i++) {
                double xSpeed = speed * xComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
                double ySpeed = speed * yComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
                double zSpeed = speed * zComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
                float value = rand.nextFloat() * 0.15f;
                world.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f + value, 0.75f + value,1f, 10f + rand.nextFloat() * 20f, 40, ParticleCloud.EnumCloudBehavior.GROW, 1f), getPosX(), getPosY(), getPosZ(), xSpeed, ySpeed, zSpeed);
            }
        }
        if (ticksExisted > 10) hitEntities();
        if (ticksExisted > 10) freezeBlocks();

        if (ticksExisted > 65 && !(caster instanceof PlayerEntity)) remove();
    }

    public void hitEntities() {
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(RANGE, RANGE, RANGE, RANGE);
        float damage = DAMAGE_PER_HIT;
        if (caster instanceof EntityFrostmaw) damage *= ConfigHandler.COMMON.MOBS.FROSTMAW.combatConfig.attackMultiplier.get();
        if (caster instanceof PlayerEntity) damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.attackMultiplier.get();
        for (LivingEntity entityHit : entitiesHit) {
            if (entityHit == caster) continue;

            List<? extends String> freezeImmune = ConfigHandler.COMMON.GENERAL.freeze_blacklist.get();
            ResourceLocation mobName = EntityType.getKey(entityHit.getType());
            if (freezeImmune.contains(mobName.toString())) continue;

            float entityHitYaw = (float) ((Math.atan2(entityHit.getPosZ() - getPosZ(), entityHit.getPosX() - getPosX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingYaw = rotationYaw % 360;
            if (entityHitYaw < 0) {
                entityHitYaw += 360;
            }
            if (entityAttackingYaw < 0) {
                entityAttackingYaw += 360;
            }
            float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

            float xzDistance = (float) Math.sqrt((entityHit.getPosZ() - getPosZ()) * (entityHit.getPosZ() - getPosZ()) + (entityHit.getPosX() - getPosX()) * (entityHit.getPosX() - getPosX()));
            float entityHitPitch = (float) ((Math.atan2((entityHit.getPosY() - getPosY()), xzDistance) * (180 / Math.PI)) % 360);
            float entityAttackingPitch = -rotationPitch % 360;
            if (entityHitPitch < 0) {
                entityHitPitch += 360;
            }
            if (entityAttackingPitch < 0) {
                entityAttackingPitch += 360;
            }
            float entityRelativePitch = entityHitPitch - entityAttackingPitch;

            float entityHitDistance = (float) Math.sqrt((entityHit.getPosZ() - getPosZ()) * (entityHit.getPosZ() - getPosZ()) + (entityHit.getPosX() - getPosX()) * (entityHit.getPosX() - getPosX()) + (entityHit.getPosY() - getPosY()) * (entityHit.getPosY() - getPosY()));

            boolean inRange = entityHitDistance <= RANGE;
            boolean yawCheck = (entityRelativeYaw <= ARC / 2f && entityRelativeYaw >= -ARC / 2f) || (entityRelativeYaw >= 360 - ARC / 2f || entityRelativeYaw <= -360 + ARC / 2f);
            boolean pitchCheck = (entityRelativePitch <= ARC / 2f && entityRelativePitch >= -ARC / 2f) || (entityRelativePitch >= 360 - ARC / 2f || entityRelativePitch <= -360 + ARC / 2f);
            if (inRange && yawCheck && pitchCheck) {
                // Raytrace to mob center to avoid damaging through walls
                Vec3d from = getPositionVec();
                Vec3d to = entityHit.getPositionVec().add(0, entityHit.getHeight() / 2f, 0);
                BlockRayTraceResult result = world.rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                if (result.getType() == RayTraceResult.Type.BLOCK) {
                    continue;
                }

                if (entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, caster), damage)) {
                    entityHit.setMotion(entityHit.getMotion().mul(0.25, 1, 0.25));
                    FrozenCapability.IFrozenCapability capability = CapabilityHandler.getCapability(entityHit, FrozenCapability.FrozenProvider.FROZEN_CAPABILITY);
                    if (capability != null) capability.addFreezeProgress(entityHit, 0.23f);
                }
            }
        }
    }

    public void freezeBlocks() {
        int checkDist = 10;
        for (int i = (int)getPosX() - checkDist; i < (int)getPosX() + checkDist; i++) {
            for (int j = (int)getPosY() - checkDist; j < (int)getPosY() + checkDist; j++) {
                for (int k = (int)getPosZ() - checkDist; k < (int)getPosZ() + checkDist; k++) {
                    BlockPos pos = new BlockPos(i, j, k);

                    BlockState blockState = world.getBlockState(pos);
                    BlockState blockStateAbove = world.getBlockState(pos.up());
                    if (blockState.getBlock() != Blocks.WATER || blockStateAbove.getBlock() != Blocks.AIR) {
                        continue;
                    }

                    float blockHitYaw = (float) ((Math.atan2(pos.getZ() - getPosZ(), pos.getX() - getPosX()) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingYaw = rotationYaw % 360;
                    if (blockHitYaw < 0) {
                        blockHitYaw += 360;
                    }
                    if (entityAttackingYaw < 0) {
                        entityAttackingYaw += 360;
                    }
                    float blockRelativeYaw = blockHitYaw - entityAttackingYaw;

                    float xzDistance = (float) Math.sqrt((pos.getZ() - getPosZ()) * (pos.getZ() - getPosZ()) + (pos.getX() - getPosX()) * (pos.getX() - getPosX()));
                    float blockHitPitch = (float) ((Math.atan2((pos.getY() - getPosY()), xzDistance) * (180 / Math.PI)) % 360);
                    float entityAttackingPitch = -rotationPitch % 360;
                    if (blockHitPitch < 0) {
                        blockHitPitch += 360;
                    }
                    if (entityAttackingPitch < 0) {
                        entityAttackingPitch += 360;
                    }
                    float blockRelativePitch = blockHitPitch - entityAttackingPitch;

                    float blockHitDistance = (float) Math.sqrt((pos.getZ() - getPosZ()) * (pos.getZ() - getPosZ()) + (pos.getX() - getPosX()) * (pos.getX() - getPosX()) + (pos.getY() - getPosY()) * (pos.getY() - getPosY()));

                    boolean inRange = blockHitDistance <= RANGE;
                    boolean yawCheck = (blockRelativeYaw <= ARC / 2f && blockRelativeYaw >= -ARC / 2f) || (blockRelativeYaw >= 360 - ARC / 2f || blockRelativeYaw <= -360 + ARC / 2f);
                    boolean pitchCheck = (blockRelativePitch <= ARC / 2f && blockRelativePitch >= -ARC / 2f) || (blockRelativePitch >= 360 - ARC / 2f || blockRelativePitch <= -360 + ARC / 2f);
                    if (inRange && yawCheck && pitchCheck) {
                        EntityBlockSwapper.swapBlock(world, pos, Blocks.ICE.getDefaultState(), 140, false, false);
                    }
                }
            }
        }
    }

    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f && e.getPosY() <= getPosY() + dY);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }
}
