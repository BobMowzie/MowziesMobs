package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData {
    public static final int STRIKE_EXPLOSION = 35;

    private static final int STRIKE_LENGTH = 43;
    // 1 minute past strike end
    private static final int STRIKE_LINGER = STRIKE_LENGTH + 20 * 60;

    private int prevStrikeTime;

    private int strikeTime;

    private LivingEntity caster;

    private static final EntityDataAccessor<Integer> VARIANT_LEAST = SynchedEntityData.defineId(EntitySunstrike.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT_MOST = SynchedEntityData.defineId(EntitySunstrike.class, EntityDataSerializers.INT);

    public EntitySunstrike(EntityType<? extends EntitySunstrike> type, Level world) {
        super(type, world);
        noCulling = true;
    }

    public EntitySunstrike(EntityType<? extends EntitySunstrike> type, Level world, LivingEntity caster, int x, int y, int z) {
        this(type, world);
        this.caster = caster;
        this.setPos(x + 0.5F, y + 1.0625F, z + 0.5F);
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(VARIANT_LEAST, 0);
        getEntityData().define(VARIANT_MOST, 0);
    }

    public float getStrikeTime(float delta) {
        return getActualStrikeTime(delta) / STRIKE_LENGTH;
    }

    public float getStrikeDrawTime(float delta) {
        return getActualStrikeTime(delta) / STRIKE_EXPLOSION;
    }

    public float getStrikeDamageTime(float delta) {
        return (getActualStrikeTime(delta) - STRIKE_EXPLOSION) / (STRIKE_LENGTH - STRIKE_EXPLOSION);
    }

    public boolean isStrikeDrawing(float delta) {
        return getActualStrikeTime(delta) < STRIKE_EXPLOSION;
    }

    public boolean isLingering(float delta) {
        return getActualStrikeTime(delta) > STRIKE_EXPLOSION + 5;
    }

    public boolean isStriking(float delta) {
        return getActualStrikeTime(delta) < STRIKE_LENGTH;
    }

    private float getActualStrikeTime(float delta) {
        return prevStrikeTime + (strikeTime - prevStrikeTime) * delta;
    }

    private void setStrikeTime(int strikeTime) {
        this.prevStrikeTime = this.strikeTime = strikeTime;
    }

    public boolean isStriking() {
        return isStriking(1);
    }

    public long getVariant() {
        return (((long) getEntityData().get(VARIANT_MOST)) << 32) | (getEntityData().get(VARIANT_LEAST) & 0xFFFFFFFFL);
    }

    private void setVariant(long variant) {
        getEntityData().set(VARIANT_MOST, (int) (variant >> 32));
        getEntityData().set(VARIANT_LEAST, (int) variant);
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

    @Override
    public void tick() {
        super.tick();
        prevStrikeTime = strikeTime;

        if (level.isClientSide) {
            if (strikeTime == 0) {
                MowziesMobs.PROXY.playSunstrikeSound(this);
            } else if (strikeTime < STRIKE_EXPLOSION - 10) {
                float time = getStrikeTime(1);
                int timeBonus = (int) (time * 5);
                int orbCount = random.nextInt(4 + timeBonus) + timeBonus + 1;
                while (orbCount-- > 0) {
                    float theta = random.nextFloat() * MathUtils.TAU;
                    final float min = 0.2F, max = 1.9F;
                    float r = random.nextFloat() * (max - min) + min;
                    float ox = r * Mth.cos(theta);
                    float oz = r * Mth.sin(theta);
                    final float minY = 0.1F;
                    float oy = random.nextFloat() * (time * 6 - minY) + minY;
                    level.addParticle(new ParticleOrb.OrbData((float) getX(), (float) getZ()), getX() + ox, getY() + oy, getZ() + oz, 0, 0, 0);
                }
            } else if (strikeTime > STRIKE_EXPLOSION) {
                this.smolder();
            } else if (strikeTime == STRIKE_EXPLOSION) {
                this.spawnExplosionParticles(10);
            }
        } else {
            this.moveDownToGround();
            if (strikeTime >= STRIKE_LINGER || !level.canSeeSkyFromBelowWater(blockPosition())) {
                this.discard() ;
            } else if (strikeTime == STRIKE_EXPLOSION) {
                this.damageEntityLivingBaseNearby(3);
            }
        }
        strikeTime++;
    }

    public void moveDownToGround() {
        HitResult rayTrace = rayTrace(this);
        if (rayTrace.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitResult = (BlockHitResult) rayTrace;
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = level.getBlockState(hitResult.getBlockPos());
                if (strikeTime > STRIKE_LENGTH && hitBlock != level.getBlockState(blockPosition().below())) {
                    this.discard() ;
                }
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) {
                    this.setPos(getX(), hitResult.getBlockPos().getY() + 1.0625F - 0.5f, getZ());
                } else {
                    this.setPos(getX(), hitResult.getBlockPos().getY() + 1.0625F, getZ());
                }
                if (this.level instanceof ServerLevel) {
                    ((ServerLevel) this.level).getChunkSource().broadcast(this, new ClientboundTeleportEntityPacket(this));
                }
            }
        }
    }

    public void damageEntityLivingBaseNearby(double radius) {
        AABB region = new AABB(getX() - radius, getY() - 0.5, getZ() - radius, getX() + radius, Double.POSITIVE_INFINITY, getZ() + radius);
        List<Entity> entities = level.getEntities(this, region);
        double radiusSq = radius * radius;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && getDistanceSqXZToEntity(entity) < radiusSq) {
                if (caster instanceof EntityBarako && (entity instanceof LeaderSunstrikeImmune)) {
                    continue;
                }
                if (caster instanceof Player && entity == caster) {
                    continue;
                }
                float damageFire = 2f;
                float damageMob = 2f;
                if (caster instanceof EntityBarako) {
                    damageFire *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                    damageMob *= ConfigHandler.COMMON.MOBS.BARAKO.combatConfig.attackMultiplier.get();
                }
                if (caster instanceof Player) {
                    damageFire *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier.get();
                    damageMob *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier.get();
                }
                if (entity.hurt(DamageSource.indirectMobAttack(this, caster), damageMob)) entity.invulnerableTime = 0;
                if (entity.hurt(DamageSource.ON_FIRE, damageFire)) {
                    entity.setSecondsOnFire(3);
                }
            }
        }
    }

    public double getDistanceSqXZToEntity(Entity entityIn)
    {
        double d0 = this.getX() - entityIn.getX();
        double d2 = this.getZ() - entityIn.getZ();
        return d0 * d0 + d2 * d2;
    }

    private void smolder() {
        if (random.nextFloat() < 0.1F) {
            int amount = random.nextInt(2) + 1;
            while (amount-- > 0) {
                float theta = random.nextFloat() * MathUtils.TAU;
                float r = random.nextFloat() * 0.7F;
                float x = r * Mth.cos(theta);
                float z = r * Mth.sin(theta);
                level.addParticle(ParticleTypes.LARGE_SMOKE, getX() + x, getY() + 0.1, getZ() + z, 0, 0, 0);
            }
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = random.nextFloat() * 0.08F;
            float vx = velocity * Mth.cos(yaw);
            float vz = velocity * Mth.sin(yaw);
            level.addParticle(ParticleTypes.FLAME, getX(), getY() + 0.1, getZ(), vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++) {
            level.addParticle(ParticleTypes.LAVA, getX(), getY() + 0.1, getZ(), 0, 0, 0);
        }
    }

    public void onSummon() {
        this.setVariant(random.nextLong());
    }

    private HitResult rayTrace(EntitySunstrike entity) {
        Vec3 startPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        Vec3 endPos = new Vec3(entity.getX(), level.getMinBuildHeight(), entity.getZ());
        return entity.level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("strikeTime", strikeTime);
        compound.putLong("variant", getVariant());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        setStrikeTime(compound.getInt("strikeTime"));
        setVariant(compound.getLong("variant"));
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(strikeTime);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        setStrikeTime(buffer.readInt());
    }
}
