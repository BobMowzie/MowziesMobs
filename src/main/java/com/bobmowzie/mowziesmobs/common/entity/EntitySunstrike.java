package com.bobmowzie.mowziesmobs.common.entity;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.block.Block;
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

import com.bobmowzie.mowziesmobs.client.audio.MovingSoundSuntrike;
import com.bobmowzie.mowziesmobs.client.particle.EntityOrbFX;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Created by jnad325 on 11/16/15.
 */
public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData
{
    private static final int STRIKE_LENGTH = 43;

    private static final int STRIKE_EXPLOSION = 35;

    // 1 minute past strike end
    private static final int STRIKE_LINGER = STRIKE_LENGTH + 20 * 60;

    private int prevStrikeTime;

    private int strikeTime;

    public EntitySunstrike(World world)
    {
        super(world);
        setSize(0.1F, 0.1F);
        ignoreFrustumCheck = true;
    }

    public EntitySunstrike(World world, int x, int y, int z)
    {
        this(world);
        setPosition(x + 0.5F, y + 1.0625F, z + 0.5F);
    }

    @Override
    protected void entityInit()
    {
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
            if (!b.isOpaqueCube() && !(b instanceof BlockLeaves))
            {
                if (strikeTime <= STRIKE_LENGTH) posY -= 1;
                else setDead();
            }
            else break;
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
            	while (orbCount --> 0)
            	{
            		double theta = rand.nextDouble() * Math.PI * 2;
            		final double min = 0.2, max = 1.9;
            		double r = rand.nextDouble() * (max - min) + min;
            		double ox = r * Math.cos(theta);
            		double oz = r * Math.sin(theta);
            		final double minY = 0.1;
            		double oy = rand.nextDouble() * (time * 6 - minY) + minY;
                	effectRenderer.addEffect(new EntityOrbFX(worldObj, posX + ox, posY + oy, posZ + oz, posX, posZ));
            	}
            }
            else if (strikeTime > STRIKE_EXPLOSION && strikeTime % 8 == 0)
            {
            	spawnSmoke(1);
            }
            else if (strikeTime == STRIKE_EXPLOSION)
            {
            	spawnExplosionParticles(10);
            }
        }
        else
        {
            if (strikeTime == STRIKE_LINGER || !worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)))
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
                entity.attackEntityFrom(DamageSource.onFire, 10);
                entity.setFire(5);
            }
        }
    }

    private void spawnSmoke(int howMuch) {
        for (int i = 1; i <= howMuch; i++) worldObj.spawnParticle("largesmoke", posX, posY + 0.1, posZ, 0.0D, 0.0D, 0.0D);
    }

    private void spawnExplosionParticles(int howMuch) {
        for (int i = 0; i < howMuch; i++) {
            float velocity = 0.1f;
            float yaw = i * (360/howMuch);
            float vy = rand.nextFloat() * 0.08f;
            float vx = (float) (velocity * Math.cos(yaw * Math.PI/180));
            float vz = (float) (velocity * Math.sin(yaw * Math.PI / 180));
            worldObj.spawnParticle("flame", posX, posY + 0.1, posZ, vx, vy, vz);
        }
        for (int i = 0; i < howMuch/2; i++) {
            worldObj.spawnParticle("lava", posX, posY + 0.1, posZ, 0, 0, 0);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("strikeTime", strikeTime);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        setStrikeTime(compound.getInteger("strikeTime"));
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
