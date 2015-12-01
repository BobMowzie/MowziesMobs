package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.client.audio.MovingSoundSuntrike;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.particle.EntityOrbFX;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by jnad325 on 11/16/15.
 */
public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData
{
    private static final int VARIANT_MIN_ID = 2;

    private static final int VARIANT_MAX_ID = 3;

    private static final int STRIKE_LENGTH = 43;

    private static final int STRIKE_EXPLOSION = 35;

    // 1 minute past strike end
    private static final int STRIKE_LINGER = STRIKE_LENGTH + 20 * 60;

    private int prevStrikeTime;

    private int strikeTime;

    private EntityLivingBase caster;

    public EntitySunstrike(World world)
    {
        super(world);
        setSize(0.1F, 0.1F);
        ignoreFrustumCheck = true;
    }

    public EntitySunstrike(World world, EntityLivingBase caster, int x, int y, int z)
    {
        this(world);
        this.caster = caster;
        setPosition(x + 0.5F, y + 1.0625F, z + 0.5F);
    }

    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(VARIANT_MIN_ID, 0);
        dataWatcher.addObject(VARIANT_MAX_ID, 0);
    }

    public float getStrikeTime(float delta)
    {
        return getActualStrikeTime(delta) / STRIKE_LENGTH;
    }

    public float getStrikeDrawTime(float delta)
    {
        return getActualStrikeTime(delta) / STRIKE_EXPLOSION;
    }

    public float getStrikeDamageTime(float delta)
    {
        return (getActualStrikeTime(delta) - STRIKE_EXPLOSION) / (STRIKE_LENGTH - STRIKE_EXPLOSION);
    }

    public boolean isStrikeDrawing(float delta)
    {
        return getActualStrikeTime(delta) < STRIKE_EXPLOSION;
    }

    public boolean isLingering(float delta)
    {
        return getActualStrikeTime(delta) > STRIKE_EXPLOSION + 5;
    }

    public boolean isStriking(float delta)
    {
        return getActualStrikeTime(delta) < STRIKE_LENGTH;
    }

    private float getActualStrikeTime(float delta)
    {
        return prevStrikeTime + (strikeTime - prevStrikeTime) * delta;
    }

    private void setStrikeTime(int strikeTime)
    {
        this.prevStrikeTime = this.strikeTime = strikeTime;
    }

    private void setVariant(long variant)
    {
        dataWatcher.updateObject(VARIANT_MAX_ID, (int) (variant >> 32));
        dataWatcher.updateObject(VARIANT_MIN_ID, (int) variant);
    }

    public long getVariant()
    {
        return (((long) dataWatcher.getWatchableObjectInt(VARIANT_MAX_ID)) << 32) | (dataWatcher.getWatchableObjectInt(VARIANT_MIN_ID) & 0xFFFFFFFFL);
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 1024;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        prevStrikeTime = strikeTime;

        for (int i = 1; i < 20; i++)
        {
            Block b = worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 1), MathHelper.floor_double(posZ));
            if (!b.isOpaqueCube() && !(b instanceof BlockLeaves || b instanceof BlockGlass))
            {
                if (strikeTime <= STRIKE_LENGTH)
                {
                    posY -= 1;
                }
                else
                {
                    setDead();
                    break;
                }
            }
            else
            {
                break;
            }
        }

        if (worldObj.isRemote)
        {
            if (strikeTime == 0)
            {
                Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundSuntrike(this));
            }
            else if (strikeTime < STRIKE_EXPLOSION - 10)
            {
                EffectRenderer effectRenderer = Minecraft.getMinecraft().effectRenderer;
                float time = getStrikeTime(1);
                int timeBonus = (int) (time * 5);
                int orbCount = rand.nextInt(4 + timeBonus) + timeBonus + 1;
                while (orbCount-- > 0)
                {
                    float theta = rand.nextFloat() * MathUtils.TAU;
                    final float min = 0.2F, max = 1.9F;
                    float r = rand.nextFloat() * (max - min) + min;
                    float ox = r * MathHelper.cos(theta);
                    float oz = r * MathHelper.sin(theta);
                    final float minY = 0.1F;
                    float oy = rand.nextFloat() * (time * 6 - minY) + minY;
                    effectRenderer.addEffect(new EntityOrbFX(worldObj, posX + ox, posY + oy, posZ + oz, posX, posZ));
                }
            }
            else if (strikeTime > STRIKE_EXPLOSION)
            {
                smolder();
            }
            else if (strikeTime == STRIKE_EXPLOSION)
            {
                spawnExplosionParticles(10);
            }
        }
        else
        {

            if (strikeTime >= STRIKE_LINGER || !worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)))
            {
                setDead();
            }
            else if (strikeTime == STRIKE_EXPLOSION)
            {
                damageEntityLivingBaseNearby(3);
            }
        }
        strikeTime++;
    }

    public void damageEntityLivingBaseNearby(double radius)
    {
        AxisAlignedBB region = AxisAlignedBB.getBoundingBox(posX - radius, posY - 0.5, posZ - radius, posX + radius, Double.POSITIVE_INFINITY, posZ + radius);
        List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, region);
        double radiusSq = radius * radius;
        for (Entity entity : entities)
        {
            if (entity instanceof EntityLivingBase && getDistanceSqToEntity(entity) < radiusSq)
            {
                if (caster instanceof EntityTribeLeader && (entity instanceof LeaderSunstrikeImmune))
                {
                    continue;

                }
                entity.attackEntityFrom(DamageSource.onFire, 10);
                entity.setFire(5);
            }
        }
    }

    private void smolder()
    {
        if (rand.nextFloat() < 0.1F)
        {
            int amount = rand.nextInt(2) + 1;
            while (amount-- > 0)
            {
                float theta = rand.nextFloat() * MathUtils.TAU;
                float r = rand.nextFloat() * 0.7F;
                float x = r * MathHelper.cos(theta);
                float z = r * MathHelper.sin(theta);
                worldObj.spawnParticle("largesmoke", posX + x, posY + 0.1, posZ + z, 0, 0, 0);
            }
        }
    }

    private void spawnExplosionParticles(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            final float velocity = 0.1F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = rand.nextFloat() * 0.08F;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
            worldObj.spawnParticle("flame", posX, posY + 0.1, posZ, vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++)
        {
            worldObj.spawnParticle("lava", posX, posY + 0.1, posZ, 0, 0, 0);
        }
    }

    public void onSummon()
    {
        setVariant(rand.nextLong());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("strikeTime", strikeTime);
        compound.setLong("variant", getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        setStrikeTime(compound.getInteger("strikeTime"));
        setVariant(compound.getLong("variant"));
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(strikeTime);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        setStrikeTime(buffer.readInt());
    }
}
