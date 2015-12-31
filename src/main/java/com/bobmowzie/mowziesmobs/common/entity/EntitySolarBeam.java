package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnad325 on 12/26/15.
 */
public class EntitySolarBeam extends Entity {
    private EntityLivingBase caster;

    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;

    private final double RADIUS = 20;

    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public int blockSide = -1;

    public EntitySolarBeam(World world)
    {
        super(world);
        setSize(0.1F, 0.1F);
        ignoreFrustumCheck = true;
    }

    public EntitySolarBeam(World world, EntityLivingBase caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(world);
        this.caster = caster;
        setYaw(yaw);
        setPitch(pitch);
        setDuration(duration);
        setPosition(x, y, z);
        calculateEndPos();
        playSound("mowziesmobs:laser", 2f, 1);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
//        setPitch((float) (getPitch() + 0.02));
//        setYaw((float) (getYaw() + 0.02));
        if (!on && appear.getTimer() == 0) setDead();
        if (on && ticksExisted > 20) appear.increaseTimer();
        else appear.decreaseTimer();

        if (worldObj.isRemote && ticksExisted <= 10) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f;
                double yaw = rand.nextFloat() * 2 * Math.PI;
                double pitch = rand.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                double offsetX = -2 * Math.cos(getYaw());
                double offsetZ = -2 * Math.sin(getYaw());
                MowziesMobs.proxy.spawnOrbFX(worldObj, posX + ox + offsetX, posY + oy + 0.3, posZ + oz + offsetZ, posX + offsetX, posY + 0.3, posZ + offsetZ, 10);
            }
        }
        if (ticksExisted > 20) {
            calculateEndPos();
            List<EntityLivingBase> hit = raytraceEntities(worldObj, Vec3.createVectorHelper(posX, posY, posZ), Vec3.createVectorHelper(endPosX, endPosY, endPosZ), false, true, true).entities;
            if (blockSide != -1) spawnExplosionParticles(2);
            if (!worldObj.isRemote) {
                for (EntityLivingBase target : hit) {
                    if (caster instanceof EntityTribeLeader && target instanceof LeaderSunstrikeImmune) continue;
                    target.attackEntityFrom(DamageSource.onFire, 3);
                }
            } else {
                if (ticksExisted - 15 < getDuration()) {
                    int particleCount = 4;
                    while (--particleCount != 0) {
                        double radius = 1f;
                        double yaw = rand.nextFloat() * 2 * Math.PI;
                        double pitch = rand.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(getYaw()) * Math.cos(getPitch());
                        double o2y = -1 * Math.sin(getPitch());
                        double o2z = -1 * Math.sin(getYaw()) * Math.cos(getPitch());
                        MowziesMobs.proxy.spawnOrbFX(worldObj, posX + o2x + ox, posY + o2y + oy, posZ + o2z + oz, collidePosX + o2x + ox, collidePosY + o2y + oy, collidePosZ + o2z + oz, 15);
                    }
                    int particleCount2 = 4;
                    while (--particleCount2 != 0) {
                        double radius = 2f;
                        double yaw = rand.nextFloat() * 2 * Math.PI;
                        double pitch = rand.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(getYaw()) * Math.cos(getPitch());
                        double o2y = -1 * Math.sin(getPitch());
                        double o2z = -1 * Math.sin(getYaw()) * Math.cos(getPitch());
                        MowziesMobs.proxy.spawnOrbFX(worldObj, collidePosX + o2x, collidePosY + o2y, collidePosZ + o2z, collidePosX + o2x + ox, collidePosY + o2y + oy, collidePosZ + o2z + oz, 20);
                    }
                }
            }
        }
        if (ticksExisted - 20 > getDuration()) on = false;
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++)
        {
            final float velocity = 0.1F;
            float yaw = (float) (rand.nextFloat() * 2 * Math.PI);
            float vy = rand.nextFloat() * 0.08F;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
            worldObj.spawnParticle("flame", collidePosX, collidePosY + 0.1, collidePosZ, vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++)
        {
            worldObj.spawnParticle("lava", collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(2, 0f);
        dataWatcher.addObject(3, 0f);
        dataWatcher.addObject(4, 0);
    }

    public void setYaw(float yaw) {
        dataWatcher.updateObject(2, yaw);
    }

    public double getYaw() {
        return dataWatcher.getWatchableObjectFloat(2);
    }

    public void setPitch(float pitch) {
        dataWatcher.updateObject(3, pitch);
    }

    public double getPitch() {
        return dataWatcher.getWatchableObjectFloat(3);
    }

    public void setDuration(int duration) {
        dataWatcher.updateObject(4, duration);
    }

    public double getDuration() {
        return dataWatcher.getWatchableObjectInt(4);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

    }

    private void calculateEndPos() {
        endPosX = posX + RADIUS * Math.cos(getYaw()) * Math.cos(getPitch());
        endPosZ = posZ + RADIUS * Math.sin(getYaw()) * Math.cos(getPitch());
        endPosY = posY + RADIUS * Math.sin(getPitch());
    }

    public static class HitResult {
        private MovingObjectPosition blockHit;

        private List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();

        public void setBlockHit(MovingObjectPosition blockHit) {
            this.blockHit = blockHit;
        }

        public MovingObjectPosition getBlockHit() {
            return blockHit;
        }

        public void addEntityHit(EntityLivingBase entity) {
            entities.add(entity);
        }
    }

    public HitResult raytraceEntities(World world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        HitResult result = new HitResult();
        result.setBlockHit(world.func_147447_a(Vec3.createVectorHelper(from.xCoord, from.yCoord, from.zCoord), to, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock));
        if (result.blockHit != null) {
            collidePosX = result.blockHit.hitVec.xCoord;
            collidePosY = result.blockHit.hitVec.yCoord;
            collidePosZ = result.blockHit.hitVec.zCoord;
            blockSide = result.blockHit.sideHit;
        }
        else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = -1;
        }
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(Math.min(posX, collidePosX), Math.min(posY, collidePosY), Math.min(posZ, collidePosZ), Math.max(posX, collidePosX), Math.max(posY, collidePosY), Math.max(posZ, collidePosZ)).expand(1, 1, 1));
        for (EntityLivingBase entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getCollisionBorderSize() + 0.5f;
            AxisAlignedBB aabb = entity.boundingBox.expand(pad, pad, pad);
            MovingObjectPosition hit = aabb.calculateIntercept(from, to);
            if (aabb.isVecInside(from)) {
                result.addEntityHit(entity);
            } else if (hit != null) {
                result.addEntityHit(entity);
            }
        }
        return result;
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
}
