package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.damage.DamageUtil;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntitySolarBeam extends Entity {
    public static final double RADIUS_BARAKO = 30;
    public static final double RADIUS_PLAYER = 20;
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float renderYaw, renderPitch;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HAS_PLAYER = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(EntitySolarBeam.class, EntityDataSerializers.INT);

    public float prevYaw;
    public float prevPitch;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, Level world) {
        super(type, world);
        noCulling = true;
        if (world.isClientSide) {
            attractorPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
    }

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        this.playSound(MMSounds.LASER.get(), 2f, 1);
        if (!world.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        prevCollidePosX = collidePosX;
        prevCollidePosY = collidePosY;
        prevCollidePosZ = collidePosZ;
        prevYaw = renderYaw;
        prevPitch = renderPitch;
        xo = getX();
        yo = getY();
        zo = getZ();
        if (tickCount == 1 && level.isClientSide) {
            caster = (LivingEntity) level.getEntity(getCasterID());
        }
        if (getHasPlayer()) {
            if (!level.isClientSide) {
                this.updateWithPlayer();
            }
        }
        if (caster != null) {
            renderYaw = (float) ((caster.yHeadRot + 90.0d) * Math.PI / 180.0d);
            renderPitch = (float) (-caster.getXRot() * Math.PI / 180.0d);
        }

        if (!on && appear.getTimer() == 0) {
            this.discard() ;
        }
        if (on && tickCount > 20) {
            appear.increaseTimer();
        } else {
            appear.decreaseTimer();
        }

        if (caster != null && !caster.isAlive()) discard() ;

        if (level.isClientSide && tickCount <= 10 && caster != null) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f * caster.getBbWidth();
                double yaw = random.nextFloat() * 2 * Math.PI;
                double pitch = random.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                double rootX = caster.getX();
                double rootY = caster.getY() + caster.getBbHeight() / 2f + 0.3f;
                double rootZ = caster.getZ();
                if (getHasPlayer()) {
                    if (caster instanceof Player && !(caster == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON)) {
                        GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer((Player) caster, GeckoPlayer.Perspective.THIRD_PERSON);
                        if (geckoPlayer != null) {
                            GeckoRenderPlayer renderPlayer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();
                            if (renderPlayer.betweenHandsPos != null) {
                                rootX += renderPlayer.betweenHandsPos.x();
                                rootY += renderPlayer.betweenHandsPos.y();
                                rootZ += renderPlayer.betweenHandsPos.z();
                            }
                        }
                    }
                }
                attractorPos[0] = new Vec3(rootX, rootY, rootZ);
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.ORB2.get(), rootX + ox, rootY + oy, rootZ + oz, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 7, true, false, new ParticleComponent[]{
                        new ParticleComponent.Attractor(attractorPos, 1.7f, 0.0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                new float[]{0f, 0.8f},
                                new float[]{0f, 1f}
                        ), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{3f, 6f},
                                new float[]{0f, 1f}
                        ), false)
                });
            }
        }
        if (tickCount > 20) {
            this.calculateEndPos();
            List<LivingEntity> hit = raytraceEntities(level, new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ), false, true, true).entities;
            if (blockSide != null) {
                spawnExplosionParticles(2);
            }
            if (!level.isClientSide) {
                for (LivingEntity target : hit) {
                    if (caster instanceof EntityBarako && target instanceof LeaderSunstrikeImmune) {
                        continue;
                    }
                    float damageFire = 1f;
                    float damageMob = 1.5f;
                    if (caster instanceof EntityBarako) {
                        damageFire *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                        damageMob *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                    }
                    if (caster instanceof Player) {
                        damageFire *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier.get();
                        damageMob *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier.get();
                    }
                    DamageUtil.dealMixedDamage(target, DamageSource.indirectMobAttack(this, caster), damageMob, DamageSource.ON_FIRE, damageFire);
                }
            } else {
                if (tickCount - 15 < getDuration()) {
                    int particleCount = 4;
                    while (particleCount --> 0) {
                        double radius = 1f;
                        double yaw = (float) (random.nextFloat() * 2 * Math.PI);
                        double pitch = (float) (random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        double o2x = (float) (-1 * Math.cos(getYaw()) * Math.cos(getPitch()));
                        double o2y = (float) (-1 * Math.sin(getPitch()));
                        double o2z = (float) (-1 * Math.sin(getYaw()) * Math.cos(getPitch()));
                        level.addParticle(new ParticleOrb.OrbData((float) (collidePosX + o2x + ox), (float) (collidePosY + o2y + oy), (float) (collidePosZ + o2z + oz), 15), getX() + o2x + ox, getY() + o2y + oy, getZ() + o2z + oz, 0, 0, 0);
                    }
                    particleCount = 4;
                    while (particleCount --> 0) {
                        double radius = 2f;
                        double yaw = random.nextFloat() * 2 * Math.PI;
                        double pitch = random.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(getYaw()) * Math.cos(getPitch());
                        double o2y = -1 * Math.sin(getPitch());
                        double o2z = -1 * Math.sin(getYaw()) * Math.cos(getPitch());
                        level.addParticle(new ParticleOrb.OrbData((float) (collidePosX + o2x + ox), (float) (collidePosY + o2y + oy), (float) (collidePosZ + o2z + oz), 20), collidePosX + o2x, collidePosY + o2y, collidePosZ + o2z, 0, 0, 0);
                    }
                }
            }
        }
        if (tickCount - 20 > getDuration()) {
            on = false;
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (random.nextFloat() * 2 * Math.PI);
            float motionY = random.nextFloat() * 0.08F;
            float motionX = velocity * Mth.cos(yaw);
            float motionZ = velocity * Mth.sin(yaw);
            level.addParticle(ParticleTypes.FLAME, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            level.addParticle(ParticleTypes.LAVA, collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(YAW, 0F);
        getEntityData().define(PITCH, 0F);
        getEntityData().define(DURATION, 0);
        getEntityData().define(HAS_PLAYER, false);
        getEntityData().define(CASTER, -1);
    }

    public float getYaw() {
        return getEntityData().get(YAW);
    }

    public void setYaw(float yaw) {
        getEntityData().set(YAW, yaw);
    }

    public float getPitch() {
        return getEntityData().get(PITCH);
    }

    public void setPitch(float pitch) {
        getEntityData().set(PITCH, pitch);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    public boolean getHasPlayer() {
        return getEntityData().get(HAS_PLAYER);
    }

    public void setHasPlayer(boolean player) {
        getEntityData().set(HAS_PLAYER, player);
    }

    public int getCasterID() {
        return getEntityData().get(CASTER);
    }

    public void setCasterID(int id) {
        getEntityData().set(CASTER, id);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {}

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        double radius = caster instanceof EntityBarako ? RADIUS_BARAKO : RADIUS_PLAYER;
        if (level.isClientSide()) {
            endPosX = getX() + radius * Math.cos(renderYaw) * Math.cos(renderPitch);
            endPosZ = getZ() + radius * Math.sin(renderYaw) * Math.cos(renderPitch);
            endPosY = getY() + radius * Math.sin(renderPitch);
        }
        else {
            endPosX = getX() + radius * Math.cos(getYaw()) * Math.cos(getPitch());
            endPosZ = getZ() + radius * Math.sin(getYaw()) * Math.cos(getPitch());
            endPosY = getY() + radius * Math.sin(getPitch());
        }
    }

    public SolarbeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        SolarbeamHitResult result = new SolarbeamHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            collidePosX = hitVec.x;
            collidePosY = hitVec.y;
            collidePosZ = hitVec.z;
            blockSide = result.blockHit.getDirection();
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(getX(), collidePosX), Math.min(getY(), collidePosY), Math.min(getZ(), collidePosZ), Math.max(getX(), collidePosX), Math.max(getY(), collidePosY), Math.max(getZ(), collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    private void updateWithPlayer() {
        this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0d));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + 1.2f + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (caster instanceof Player) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(caster, CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                playerCapability.setUsingSolarBeam(false);
            }
        }
    }

    public static class SolarbeamHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
