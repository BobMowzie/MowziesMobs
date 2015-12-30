package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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

    private final double RADIUS = 15;

    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    int blockSide = -1;

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
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!on && appear.getTimer() == 0) setDead();
        if (on) appear.increaseTimer();
        else appear.decreaseTimer();
        calculateEndPos();
        List<Entity> hit = raytraceEntities(worldObj, Vec3.createVectorHelper(posX, posY, posZ), Vec3.createVectorHelper(endPosX, endPosY, endPosZ), false, true, true).entities;
        if (!worldObj.isRemote) for (int i = 0; i < hit.size(); i++) {
            if (hit.get(i) instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) hit.get(i);
                if (target != caster) target.attackEntityFrom(DamageSource.onFire, 3);
            }
        }

        if (ticksExisted > getDuration()) on = false;
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

        private List<Entity> entities = new ArrayList<Entity>();

        public void setBlockHit(MovingObjectPosition blockHit) {
            this.blockHit = blockHit;
        }

        public MovingObjectPosition getBlockHit() {
            return blockHit;
        }

        public void addEntityHit(Entity entity) {
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
        List<Entity> entities = world.selectEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(from.xCoord, from.yCoord, from.zCoord, collidePosX, collidePosY, collidePosZ).expand(1, 1, 1), new IEntitySelector() {
            @Override
            public boolean isEntityApplicable(Entity entity) {
                return entity.canBeCollidedWith();
            }
        });
        for (Entity entity : entities) {
            float pad = entity.getCollisionBorderSize();
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
