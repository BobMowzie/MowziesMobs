package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

/**
 * Created by Josh on 11/16/2018.
 */
public class EntityPoisonBall extends EntityMagicEffect implements IProjectile {

    public EntityPoisonBall(World worldIn) {
        super(worldIn);
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        motionX = x * velocity;
        motionY = y * velocity;
        motionZ = z * velocity;
    }
}
