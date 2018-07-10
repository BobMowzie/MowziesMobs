package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;

public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData {
    public static final int STRIKE_EXPLOSION = 35;

    private static final int STRIKE_LENGTH = 43;
    // 1 minute past strike end
    private static final int STRIKE_LINGER = STRIKE_LENGTH + 20 * 60;

    private int prevStrikeTime;

    private int strikeTime;

    private EntityLivingBase caster;

    private static final DataParameter<Integer> VARIANT_LEAST = EntityDataManager.createKey(EntitySunstrike.class, DataSerializers.VARINT);

    private static final DataParameter<Integer> VARIANT_MOST = EntityDataManager.createKey(EntitySunstrike.class, DataSerializers.VARINT);

    public EntitySunstrike(World world) {
        super(world);
        setSize(0.1F, 0.1F);
        ignoreFrustumCheck = true;
    }

    public EntitySunstrike(World world, EntityLivingBase caster, int x, int y, int z) {
        this(world);
        this.caster = caster;
        this.setPosition(x + 0.5F, y + 1.0625F, z + 0.5F);
    }

    @Override
    protected void entityInit() {
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
    public void onUpdate() {
        super.onUpdate();
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
                this.setDead();
            } else if (strikeTime == STRIKE_EXPLOSION) {
                this.damageEntityLivingBaseNearby(3);
            }
        }
        strikeTime++;
    }

    public void moveDownToGround() {
        RayTraceResult rayTrace = rayTrace(this);
        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK && rayTrace.sideHit == EnumFacing.UP) {
            IBlockState hitBlock = world.getBlockState(rayTrace.getBlockPos());
            if (strikeTime > STRIKE_LENGTH && hitBlock != world.getBlockState(getPosition().down())) {
                this.setDead();
            }
            if (hitBlock instanceof BlockSlab && hitBlock.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
                this.setPosition(posX, rayTrace.getBlockPos().getY() + 1.0625F - 0.5f, posZ);
            } else {
                this.setPosition(posX, rayTrace.getBlockPos().getY() + 1.0625F, posZ);
            }
            for (EntityPlayer entityPlayer : ((WorldServer) world).getEntityTracker().getTrackingPlayers(this)) {
                ((EntityPlayerMP) entityPlayer).connection.sendPacket(new SPacketEntityTeleport(this));
            }
        }
    }

    public void damageEntityLivingBaseNearby(double radius) {
        AxisAlignedBB region = new AxisAlignedBB(posX - radius, posY - 0.5, posZ - radius, posX + radius, Double.POSITIVE_INFINITY, posZ + radius);
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, region);
        double radiusSq = radius * radius;
        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase && getDistanceSqXZToEntity(entity) < radiusSq) {
                if (caster instanceof EntityBarako && (entity instanceof LeaderSunstrikeImmune)) {
                    continue;
                }
                if (caster instanceof EntityPlayer && entity == caster) {
                    continue;
                }
                entity.attackEntityFrom(DamageSource.ON_FIRE, 4.5f);
                entity.attackEntityFrom(DamageSource.causeMobDamage(caster), 4.5f);
                entity.setFire(5);
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
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + x, posY + 0.1, posZ + z, 0, 0, 0);
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
            world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 0.1, posZ, vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++) {
            world.spawnParticle(EnumParticleTypes.LAVA, posX, posY + 0.1, posZ, 0, 0, 0);
        }
    }

    public void onSummon() {
        this.setVariant(rand.nextLong());
    }

    private RayTraceResult rayTrace(EntitySunstrike entity) {
        Vec3d startPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
        Vec3d endPos = new Vec3d(entity.posX, 0, entity.posZ);
        return entity.world.rayTraceBlocks(startPos, endPos, false, true, true);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("strikeTime", strikeTime);
        compound.setLong("variant", getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        setStrikeTime(compound.getInteger("strikeTime"));
        setVariant(compound.getLong("variant"));
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(strikeTime);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        setStrikeTime(buffer.readInt());
    }
}
