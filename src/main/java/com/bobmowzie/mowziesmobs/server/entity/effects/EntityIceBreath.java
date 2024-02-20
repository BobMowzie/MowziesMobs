package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Created by BobMowzie on 5/25/2017.
 */
public class EntityIceBreath extends EntityMagicEffect {
    private static final int RANGE = 10;
    private static final int ARC = 45;
    private static final int DAMAGE_PER_HIT = 1;

    public EntityIceBreath(EntityType<? extends EntityIceBreath> type, Level world) {
        super(type, world);
    }

    public EntityIceBreath(EntityType<? extends EntityIceBreath> type, Level world, LivingEntity caster) {
        super(type, world, caster);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount == 1) {
            if (level.isClientSide) {
                MowziesMobs.PROXY.playIceBreathSound(this);
            }
        }
        if (caster == null) this.discard() ;
        if (caster != null && !caster.isAlive()) this.discard() ;
        if (tickCount == 1) playSound(MMSounds.ENTITY_FROSTMAW_ICEBREATH_START.get(), 1, 0.6f);
        if (caster instanceof Player) {
            Player player = (Player) caster;
            absMoveTo(player.getX(), player.getY() + player.getStandingEyeHeight(player.getPose(), player.getDimensions(player.getPose())) - 0.5f, player.getZ(), player.getYRot(), player.getXRot());
        }

        float yaw = (float) Math.toRadians(-getYRot());
        float pitch = (float) Math.toRadians(-getXRot());
        float spread = 0.25f;
        float speed = 0.56f;
        float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
        float yComp = (float) (Math.sin(pitch));
        float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
        if (level.isClientSide) {
            if (tickCount % 8 == 0) {
                level.addParticle(new ParticleRing.RingData(yaw, -pitch, 40, 1f, 1f, 1f, 1f, 110f * spread, false, ParticleRing.EnumRingBehavior.GROW), getX(), getY(), getZ(), 0.5f * xComp, 0.5f * yComp, 0.5f * zComp);
            }

            for (int i = 0; i < 6; i++) {
                double xSpeed = speed * 1f * xComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
                double ySpeed = speed * 1f * yComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
                double zSpeed = speed * 1f * zComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
                level.addParticle(new ParticleSnowFlake.SnowflakeData(37f, true), getX(), getY(), getZ(), xSpeed, ySpeed, zSpeed);
            }
            for (int i = 0; i < 5; i++) {
                double xSpeed = speed * xComp + (spread * 0.7 * (random.nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
                double ySpeed = speed * yComp + (spread * 0.7 * (random.nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
                double zSpeed = speed * zComp + (spread * 0.7 * (random.nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
                float value = random.nextFloat() * 0.15f;
                level.addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD.get(), 0.75f + value, 0.75f + value,1f, 10f + random.nextFloat() * 20f, 40, ParticleCloud.EnumCloudBehavior.GROW, 1f), getX(), getY(), getZ(), xSpeed, ySpeed, zSpeed);
            }
        }
        if (tickCount > 10) hitEntities();
        if (tickCount > 10) freezeBlocks();

        if (tickCount > 65 && !(caster instanceof Player)) discard() ;
    }

    public void hitEntities() {
        List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(RANGE, RANGE, RANGE, RANGE);
        float damage = DAMAGE_PER_HIT;
        if (caster instanceof EntityFrostmaw) damage *= ConfigHandler.COMMON.MOBS.FROSTMAW.combatConfig.attackMultiplier.get();
        if (caster instanceof Player) damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.attackMultiplier.get();
        for (LivingEntity entityHit : entitiesHit) {
            if (entityHit == caster) continue;

            if (entityHit.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || entityHit instanceof EnderDragon) continue;

            float entityHitYaw = (float) ((Math.atan2(entityHit.getZ() - getZ(), entityHit.getX() - getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingYaw = getYRot() % 360;
            if (entityHitYaw < 0) {
                entityHitYaw += 360;
            }
            if (entityAttackingYaw < 0) {
                entityAttackingYaw += 360;
            }
            float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

            float xzDistance = (float) Math.sqrt((entityHit.getZ() - getZ()) * (entityHit.getZ() - getZ()) + (entityHit.getX() - getX()) * (entityHit.getX() - getX()));
            double hitY = entityHit.getY() + entityHit.getBbHeight() / 2.0;
            float entityHitPitch = (float) ((Math.atan2((hitY - getY()), xzDistance) * (180 / Math.PI)) % 360);
            float entityAttackingPitch = -getXRot() % 360;
            if (entityHitPitch < 0) {
                entityHitPitch += 360;
            }
            if (entityAttackingPitch < 0) {
                entityAttackingPitch += 360;
            }
            float entityRelativePitch = entityHitPitch - entityAttackingPitch;

            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - getZ()) * (entityHit.getZ() - getZ()) + (entityHit.getX() - getX()) * (entityHit.getX() - getX()) + (hitY - getY()) * (hitY - getY()));

            boolean inRange = entityHitDistance <= RANGE;
            boolean yawCheck = (entityRelativeYaw <= ARC / 2f && entityRelativeYaw >= -ARC / 2f) || (entityRelativeYaw >= 360 - ARC / 2f || entityRelativeYaw <= -360 + ARC / 2f);
            boolean pitchCheck = (entityRelativePitch <= ARC / 2f && entityRelativePitch >= -ARC / 2f) || (entityRelativePitch >= 360 - ARC / 2f || entityRelativePitch <= -360 + ARC / 2f);
            boolean frostmawCloseCheck = caster instanceof EntityFrostmaw && entityHitDistance <= 2;
            if (inRange && yawCheck && pitchCheck || frostmawCloseCheck) {
                // Do raycast check to prevent damaging through walls
                if (!raytraceCheckEntity(entityHit)) continue;

                if (entityHit.hurt(DamageSource.FREEZE, damage)) {
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().multiply(0.25, 1, 0.25));
                    FrozenCapability.IFrozenCapability capability = CapabilityHandler.getCapability(entityHit, CapabilityHandler.FROZEN_CAPABILITY);
                    if (capability != null) capability.addFreezeProgress(entityHit, 0.23f);
                }
            }
        }
    }

    public void freezeBlocks() {
        int checkDist = 10;
        for (int i = (int)getX() - checkDist; i < (int)getX() + checkDist; i++) {
            for (int j = (int)getY() - checkDist; j < (int)getY() + checkDist; j++) {
                for (int k = (int)getZ() - checkDist; k < (int)getZ() + checkDist; k++) {
                    BlockPos pos = new BlockPos(i, j, k);

                    BlockState blockState = level.getBlockState(pos);
                    BlockState blockStateAbove = level.getBlockState(pos.above());
                    if (blockState.getBlock() != Blocks.WATER || blockStateAbove.getBlock() != Blocks.AIR) {
                        continue;
                    }

                    float blockHitYaw = (float) ((Math.atan2(pos.getZ() - getZ(), pos.getX() - getX()) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingYaw = getYRot() % 360;
                    if (blockHitYaw < 0) {
                        blockHitYaw += 360;
                    }
                    if (entityAttackingYaw < 0) {
                        entityAttackingYaw += 360;
                    }
                    float blockRelativeYaw = blockHitYaw - entityAttackingYaw;

                    float xzDistance = (float) Math.sqrt((pos.getZ() - getZ()) * (pos.getZ() - getZ()) + (pos.getX() - getX()) * (pos.getX() - getX()));
                    float blockHitPitch = (float) ((Math.atan2((pos.getY() - getY()), xzDistance) * (180 / Math.PI)) % 360);
                    float entityAttackingPitch = -getXRot() % 360;
                    if (blockHitPitch < 0) {
                        blockHitPitch += 360;
                    }
                    if (entityAttackingPitch < 0) {
                        entityAttackingPitch += 360;
                    }
                    float blockRelativePitch = blockHitPitch - entityAttackingPitch;

                    float blockHitDistance = (float) Math.sqrt((pos.getZ() - getZ()) * (pos.getZ() - getZ()) + (pos.getX() - getX()) * (pos.getX() - getX()) + (pos.getY() - getY()) * (pos.getY() - getY()));

                    boolean inRange = blockHitDistance <= RANGE;
                    boolean yawCheck = (blockRelativeYaw <= ARC / 2f && blockRelativeYaw >= -ARC / 2f) || (blockRelativeYaw >= 360 - ARC / 2f || blockRelativeYaw <= -360 + ARC / 2f);
                    boolean pitchCheck = (blockRelativePitch <= ARC / 2f && blockRelativePitch >= -ARC / 2f) || (blockRelativePitch >= 360 - ARC / 2f || blockRelativePitch <= -360 + ARC / 2f);
                    if (inRange && yawCheck && pitchCheck) {
                        EntityBlockSwapper.swapBlock(level, pos, Blocks.ICE.defaultBlockState(), 140, false, false);
                    }
                }
            }
        }
    }

    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return level.getEntitiesOfClass(entityClass, getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= getY() + dY);
    }
}
