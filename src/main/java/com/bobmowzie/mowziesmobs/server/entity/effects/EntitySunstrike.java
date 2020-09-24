package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.network.play.server.SEntityTeleportPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.List;

public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData {
    public static final int STRIKE_EXPLOSION = 35;

    private static final int STRIKE_LENGTH = 43;
    // 1 minute past strike end
    private static final int STRIKE_LINGER = STRIKE_LENGTH + 20 * 60;

    private int prevStrikeTime;

    private int strikeTime;

    private LivingEntity caster;

    private static final DataParameter<Integer> VARIANT_LEAST = EntityDataManager.createKey(EntitySunstrike.class, DataSerializers.VARINT);

    private static final DataParameter<Integer> VARIANT_MOST = EntityDataManager.createKey(EntitySunstrike.class, DataSerializers.VARINT);

    public EntitySunstrike(EntityType<? extends EntitySunstrike> type, World world) {
        super(type, world);
        ignoreFrustumCheck = true;
    }

    public EntitySunstrike(EntityType<? extends EntitySunstrike> type, World world, LivingEntity caster, int x, int y, int z) {
        this(type, world);
        this.caster = caster;
        this.setPosition(x + 0.5F, y + 1.0625F, z + 0.5F);
    }

    @Override
    protected void registerData() {
        getDataManager().register(VARIANT_LEAST, 0);
        getDataManager().register(VARIANT_MOST, 0);
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
        return (((long) getDataManager().get(VARIANT_MOST)) << 32) | (getDataManager().get(VARIANT_LEAST) & 0xFFFFFFFFL);
    }

    private void setVariant(long variant) {
        getDataManager().set(VARIANT_MOST, (int) (variant >> 32));
        getDataManager().set(VARIANT_LEAST, (int) variant);
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

    @Override
    public void tick() {
        super.tick();
        prevStrikeTime = strikeTime;

        if (world.isRemote) {
            if (strikeTime == 0) {
                MowziesMobs.PROXY.playSunstrikeSound(this);
            } else if (strikeTime < STRIKE_EXPLOSION - 10) {
                float time = getStrikeTime(1);
                int timeBonus = (int) (time * 5);
                int orbCount = rand.nextInt(4 + timeBonus) + timeBonus + 1;
                while (orbCount-- > 0) {
                    float theta = rand.nextFloat() * MathUtils.TAU;
                    final float min = 0.2F, max = 1.9F;
                    float r = rand.nextFloat() * (max - min) + min;
                    float ox = r * MathHelper.cos(theta);
                    float oz = r * MathHelper.sin(theta);
                    final float minY = 0.1F;
                    float oy = rand.nextFloat() * (time * 6 - minY) + minY;
                    MMParticle.ORB.spawn(world, posX + ox, posY + oy, posZ + oz, ParticleArgs.get().withData(posX, posZ));
                }
            } else if (strikeTime > STRIKE_EXPLOSION) {
                this.smolder();
            } else if (strikeTime == STRIKE_EXPLOSION) {
                this.spawnExplosionParticles(10);
            }
        } else {
            this.moveDownToGround();
            if (strikeTime >= STRIKE_LINGER || !world.canBlockSeeSky(getPosition())) {
                this.remove();
            } else if (strikeTime == STRIKE_EXPLOSION) {
                this.damageEntityLivingBaseNearby(3);
            }
        }
        strikeTime++;
    }

    public void moveDownToGround() {
        RayTraceResult rayTrace = rayTrace(this);
        if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult hitResult = (BlockRayTraceResult) rayTrace.hitInfo;
            if (hitResult.getFace() == Direction.UP) {
                BlockState hitBlock = world.getBlockState(hitResult.getPos());
                if (strikeTime > STRIKE_LENGTH && hitBlock != world.getBlockState(getPosition().down())) {
                    this.remove();
                }
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.get(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) {
                    this.setPosition(posX, hitResult.getPos().getY() + 1.0625F - 0.5f, posZ);
                } else {
                    this.setPosition(posX, hitResult.getPos().getY() + 1.0625F, posZ);
                }
                if (this.world instanceof ServerWorld) {
                    ((ServerWorld) this.world).getChunkProvider().sendToAllTracking(this, new SEntityTeleportPacket(this));
                }
            }
        }
    }

    public void damageEntityLivingBaseNearby(double radius) {
        AxisAlignedBB region = new AxisAlignedBB(posX - radius, posY - 0.5, posZ - radius, posX + radius, Double.POSITIVE_INFINITY, posZ + radius);
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, region);
        double radiusSq = radius * radius;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && getDistanceSqXZToEntity(entity) < radiusSq) {
                if (caster instanceof EntityBarako && (entity instanceof LeaderSunstrikeImmune)) {
                    continue;
                }
                if (caster instanceof PlayerEntity && entity == caster) {
                    continue;
                }
                float damageFire = 2.5f;
                float damageMob = 2.5f;
                if (caster instanceof EntityBarako) {
                    damageFire *= ConfigHandler.MOBS.BARAKO.combatData.attackMultiplier;
                    damageMob *= ConfigHandler.MOBS.BARAKO.combatData.attackMultiplier;
                }
                if (caster instanceof PlayerEntity) {
                    damageFire *= ConfigHandler.TOOLS_AND_ABILITIES.sunsBlessingAttackMultiplier;
                    damageMob *= ConfigHandler.TOOLS_AND_ABILITIES.sunsBlessingAttackMultiplier;
                }
                if (entity.attackEntityFrom(DamageSource.causeMobDamage(caster), damageMob)) entity.hurtResistantTime = 0;
                if (entity.attackEntityFrom(DamageSource.ON_FIRE, damageFire)) {
                    entity.setFire(5);
                }
            }
        }
    }

    public double getDistanceSqXZToEntity(Entity entityIn)
    {
        double d0 = this.posX - entityIn.posX;
        double d2 = this.posZ - entityIn.posZ;
        return d0 * d0 + d2 * d2;
    }

    private void smolder() {
        if (rand.nextFloat() < 0.1F) {
            int amount = rand.nextInt(2) + 1;
            while (amount-- > 0) {
                float theta = rand.nextFloat() * MathUtils.TAU;
                float r = rand.nextFloat() * 0.7F;
                float x = r * MathHelper.cos(theta);
                float z = r * MathHelper.sin(theta);
//                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + x, posY + 0.1, posZ + z, 0, 0, 0);
            }
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = rand.nextFloat() * 0.08F;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
//            world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 0.1, posZ, vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++) {
//            world.spawnParticle(EnumParticleTypes.LAVA, posX, posY + 0.1, posZ, 0, 0, 0);
        }
    }

    public void onSummon() {
        this.setVariant(rand.nextLong());
    }

    private RayTraceResult rayTrace(EntitySunstrike entity) {
        Vec3d startPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
        Vec3d endPos = new Vec3d(entity.posX, 0, entity.posZ);
        return entity.world.rayTraceBlocks(new RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putInt("strikeTime", strikeTime);
        compound.putLong("variant", getVariant());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        setStrikeTime(compound.getInt("strikeTime"));
        setVariant(compound.getLong("variant"));
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(strikeTime);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        setStrikeTime(buffer.readInt());
    }
}
