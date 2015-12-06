package com.bobmowzie.mowziesmobs.client.particle;

import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;

public class EntityOrbFX extends EntityMMFX
{
    private double targetX;
    private double targetY;
    private double targetZ;
    private double startX;
    private double startY;
    private double startZ;

    private double signX;

    private double signZ;

    private int mode;

    private double duration;

    public EntityOrbFX(World world, double x, double y, double z, double targetX, double targetZ)
    {
        super(world, x, y, z);
        this.targetX = targetX;
        this.targetZ = targetZ;
        setTextureIndex(0, 0);
        particleScale = 1.5F + rand.nextFloat() * 0.5F;
        particleMaxAge = 120;
        signX = Math.signum(targetX - posX);
        signZ = Math.signum(targetZ - posZ);
        mode = 0;
    }
    public EntityOrbFX(World world, double x, double y, double z, double targetX, double targetY, double targetZ, double speed) {
        this(world, x, y, z, targetX, targetZ);
        this.targetY = targetY;
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.duration = speed;
        mode = 1;
    }

    @Override
    public int getBrightnessForRender(float delta)
    {
        return 240 | super.getBrightnessForRender(delta) & 0xFF0000;
    }

    @Override
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (mode == 0) {
            double vecX = targetX - posX;
            double vecZ = targetZ - posZ;
            double dist = Math.sqrt(vecX * vecX + vecZ * vecZ);
            if (dist > 2 || Math.signum(vecX) != signX || Math.signum(vecZ) != signZ || particleAge > particleMaxAge) {
                setDead();
                return;
            }
            final double peak = 0.5;
            particleAlpha = (float) (dist > peak ? MathUtils.linearTransformd(dist, peak, 2, 1, 0) : MathUtils.linearTransformd(dist, 0.1F, peak, 0, 1));
            final double minVel = 0.05, maxVel = 0.3;
            double progress = Math.sin(-Math.PI / 4 * dist) + 1;
            double magMultipler = (progress * (maxVel - minVel) + minVel) / dist;
            vecX *= magMultipler;
            vecZ *= magMultipler;
            motionX = vecX;
            motionY = progress;
            motionZ = vecZ;
            moveEntity(motionX, motionY, motionZ);
        }
        else if (mode == 1) {
            particleAlpha = (float) (10 * Math.sqrt(Math.pow(posX - startX, 2) + Math.pow(posY - startY, 2) + Math.pow(posZ - startZ, 2))/Math.sqrt(Math.pow(targetX - startX, 2) + Math.pow(targetY - startY, 2) + Math.pow(targetZ - startZ, 2)));
            posX = startX + (targetX - startX)/( 1+ Math.exp(-(8/duration) * (particleAge - duration/2)));
            posY = startY + (targetY - startY)/( 1+ Math.exp(-(8/duration) * (particleAge - duration/2)));
            posZ = startZ + (targetZ - startZ)/( 1+ Math.exp(-(8/duration) * (particleAge - duration/2)));
            if (particleAge == duration) setDead();
        }
        particleAge++;
    }
}
