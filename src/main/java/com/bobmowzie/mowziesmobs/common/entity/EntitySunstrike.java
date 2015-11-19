package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by jnad325 on 11/16/15.
 */
public class EntitySunstrike extends EntityLiving {
    public EntitySunstrike(World world) {
        super(world);
    }

    public EntitySunstrike(World world, float x, float y, float z) {
        super(world);
        setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.canBlockSeeTheSky((int)posX, (int)posY, (int)posZ)) setDead();
//        System.out.println(ticksExisted);
        if (ticksExisted == 40) damageEntityLivingBaseNearby(3, 3, 3);
        if (ticksExisted == 60) setDead();
    }

    public void damageEntityLivingBaseNearby(double distanceX, double distanceZ, double radius)
    {
        List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(distanceX, 50, distanceZ));
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityLivingBase && getDistanceToEntity(entityNeighbor) <= radius && entityNeighbor.posY >= posY) ((EntityLivingBase) entityNeighbor).attackEntityFrom(DamageSource.causeMobDamage(this), 100);
        }
    }
}
