package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.damage.DamageUtil;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntitySolarBeam extends Entity {
    private final double RADIUS = 20;
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final DataParameter<Float> YAW = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> PITCH = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.FLOAT);

    private static final DataParameter<Integer> DURATION = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.VARINT);

    private static final DataParameter<Boolean> HAS_PLAYER = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Integer> CASTER = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.VARINT);

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, World world) {
        super(type, world);
        ignoreFrustumCheck = true;
    }

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, World world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPosition(x, y, z);
        this.calculateEndPos();
        this.playSound(MMSounds.LASER.get(), 2f, 1);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted == 1 && world.isRemote) {
            caster = (LivingEntity) world.getEntityByID(getCasterID());
        }
        if (!world.isRemote && getHasPlayer()) {
            this.updateWithPlayer();
        }

        if (!on && appear.getTimer() == 0) {
            this.remove();
        }
        if (on && ticksExisted > 20) {
            appear.increaseTimer();
        } else {
            appear.decreaseTimer();
        }

        if (caster != null && !caster.isAlive()) remove();

        if (world.isRemote && ticksExisted <= 10) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f;
                double yaw = rand.nextFloat() * 2 * Math.PI;
                double pitch = rand.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                float offsetX = (float) (-2 * Math.cos(getYaw()));
                float offsetZ = (float) (-2 * Math.sin(getYaw()));
                if (getHasPlayer()) {
                    offsetX = offsetZ = 0;
                }
                world.addParticle(new ParticleOrb.OrbData((float) getPosX() + offsetX, (float) getPosY() + 0.3f, (float) getPosZ() + offsetZ, 10), getPosX() + ox + offsetX, getPosY() + oy + 0.3, getPosZ() + oz + offsetZ, 0, 0, 0);
            }
        }
        if (ticksExisted > 20) {
            this.calculateEndPos();
            List<LivingEntity> hit = raytraceEntities(world, new Vec3d(getPosX(), getPosY(), getPosZ()), new Vec3d(endPosX, endPosY, endPosZ), false, true, true).entities;
            if (blockSide != null) {
                spawnExplosionParticles(2);
            }
            if (!world.isRemote) {
                for (LivingEntity target : hit) {
                    if (caster instanceof EntityBarako && target instanceof LeaderSunstrikeImmune) {
                        continue;
                    }
                    float damageFire = 1.5f;
                    float damageMob = 3f;
                    if (caster instanceof EntityBarako) {
                        damageFire *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                        damageMob *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                    }
                    if (caster instanceof PlayerEntity) {
                        damageFire *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.sunsBlessingAttackMultiplier.get();
                        damageMob *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.sunsBlessingAttackMultiplier.get();
                    }
                    DamageUtil.dealMixedDamage(target, DamageSource.causeMobDamage(caster), damageMob, DamageSource.ON_FIRE, damageFire);
                }
            } else {
                for (LivingEntity e : hit) {
                    if (e instanceof EntityWroughtnaut) {
                        MowziesMobs.PROXY.solarBeamHitWroughtnaught(caster);
                        break;
                    }
                }
                if (ticksExisted - 15 < getDuration()) {
                    int particleCount = 4;
                    while (particleCount --> 0) {
                        double radius = 1f;
                        double yaw = (float) (rand.nextFloat() * 2 * Math.PI);
                        double pitch = (float) (rand.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        double o2x = (float) (-1 * Math.cos(getYaw()) * Math.cos(getPitch()));
                        double o2y = (float) (-1 * Math.sin(getPitch()));
                        double o2z = (float) (-1 * Math.sin(getYaw()) * Math.cos(getPitch()));
                        world.addParticle(new ParticleOrb.OrbData((float) (collidePosX + o2x + ox), (float) (collidePosY + o2y + oy), (float) (collidePosZ + o2z + oz), 15), getPosX() + o2x + ox, getPosY() + o2y + oy, getPosZ() + o2z + oz, 0, 0, 0);
                    }
                    particleCount = 4;
                    while (particleCount --> 0) {
                        double radius = 2f;
                        double yaw = rand.nextFloat() * 2 * Math.PI;
                        double pitch = rand.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(getYaw()) * Math.cos(getPitch());
                        double o2y = -1 * Math.sin(getPitch());
                        double o2z = -1 * Math.sin(getYaw()) * Math.cos(getPitch());
                        world.addParticle(new ParticleOrb.OrbData((float) (collidePosX + o2x + ox), (float) (collidePosY + o2y + oy), (float) (collidePosZ + o2z + oz), 20), collidePosX + o2x, collidePosY + o2y, collidePosZ + o2z, 0, 0, 0);
                    }
                }
            }
        }
        if (ticksExisted - 20 > getDuration()) {
            on = false;
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (rand.nextFloat() * 2 * Math.PI);
            float motionY = rand.nextFloat() * 0.08F;
            float motionX = velocity * MathHelper.cos(yaw);
            float motionZ = velocity * MathHelper.sin(yaw);
            world.addParticle(ParticleTypes.FLAME, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            world.addParticle(ParticleTypes.LAVA, collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }

    @Override
    protected void registerData() {
        getDataManager().register(YAW, 0F);
        getDataManager().register(PITCH, 0F);
        getDataManager().register(DURATION, 0);
        getDataManager().register(HAS_PLAYER, false);
        getDataManager().register(CASTER, -1);
    }

    public float getYaw() {
        return getDataManager().get(YAW);
    }

    public void setYaw(float yaw) {
        getDataManager().set(YAW, yaw);
    }

    public float getPitch() {
        return getDataManager().get(PITCH);
    }

    public void setPitch(float pitch) {
        getDataManager().set(PITCH, pitch);
    }

    public int getDuration() {
        return getDataManager().get(DURATION);
    }

    public void setDuration(int duration) {
        getDataManager().set(DURATION, duration);
    }

    public boolean getHasPlayer() {
        return getDataManager().get(HAS_PLAYER);
    }

    public void setHasPlayer(boolean player) {
        getDataManager().set(HAS_PLAYER, player);
    }

    public int getCasterID() {
        return getDataManager().get(CASTER);
    }

    public void setCasterID(int id) {
        getDataManager().set(CASTER, id);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {}

    @Override
    protected void writeAdditional(CompoundNBT nbt) {}

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        endPosX = getPosX() + RADIUS * Math.cos(getYaw()) * Math.cos(getPitch());
        endPosZ = getPosZ() + RADIUS * Math.sin(getYaw()) * Math.cos(getPitch());
        endPosY = getPosY() + RADIUS * Math.sin(getPitch());
    }

    public HitResult raytraceEntities(World world, Vec3d from, Vec3d to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        HitResult result = new HitResult();
        result.setBlockHit(world.rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)));
        if (result.blockHit != null) {
            Vec3d hitVec = result.blockHit.getHitVec();
            collidePosX = hitVec.x;
            collidePosY = hitVec.y;
            collidePosZ = hitVec.z;
            blockSide = result.blockHit.getFace();
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(Math.min(getPosX(), collidePosX), Math.min(getPosY(), collidePosY), Math.min(getPosZ(), collidePosZ), Math.max(getPosX(), collidePosX), Math.max(getPosY(), collidePosY), Math.max(getPosZ(), collidePosZ)).grow(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getCollisionBorderSize() + 0.5f;
            AxisAlignedBB aabb = entity.getBoundingBox().grow(pad, pad, pad);
            Optional<Vec3d> hit = aabb.rayTrace(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 1024;
    }

    private void updateWithPlayer() {
        this.setYaw((float) ((caster.rotationYawHead + 90) * Math.PI / 180));
        this.setPitch((float) (-caster.rotationPitch * Math.PI / 180));
        this.setPosition(caster.getPosX(), caster.getPosY() + 1.2f, caster.getPosZ());
    }

    public static class HitResult {
        private BlockRayTraceResult blockHit;

        private List<LivingEntity> entities = new ArrayList<>();

        public BlockRayTraceResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(RayTraceResult rayTraceResult) {
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK)
                this.blockHit = (BlockRayTraceResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
