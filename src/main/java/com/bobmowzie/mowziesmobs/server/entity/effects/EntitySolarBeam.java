package com.bobmowzie.mowziesmobs.server.entity.effects;

import java.util.ArrayList;
import java.util.List;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;

public class EntitySolarBeam extends Entity {
    private final double RADIUS = 20;
    public EntityLivingBase caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public EnumFacing blockSide = null;

    private static final DataParameter<Float> YAW = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> PITCH = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.FLOAT);

    private static final DataParameter<Integer> DURATION = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.VARINT);

    private static final DataParameter<Boolean> HAS_PLAYER = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Integer> CASTER = EntityDataManager.createKey(EntitySolarBeam.class, DataSerializers.VARINT);

    public EntitySolarBeam(World world) {
        super(world);
        setSize(0.1F, 0.1F);
        ignoreFrustumCheck = true;
    }

    public EntitySolarBeam(World world, EntityLivingBase caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPosition(x, y, z);
        this.calculateEndPos();
        this.playSound(MMSounds.LASER, 2f, 1);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1 && world.isRemote) {
            caster = (EntityLivingBase) world.getEntityByID(getCasterID());
        }
        if (!world.isRemote && getHasPlayer()) {
            this.updateWithPlayer();
        }

        if (!on && appear.getTimer() == 0) {
            this.setDead();
        }
        if (on && ticksExisted > 20) {
            appear.increaseTimer();
        } else {
            appear.decreaseTimer();
        }

        if (caster != null && caster.isDead) setDead();

        if (world.isRemote && ticksExisted <= 10) {
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
                if (getHasPlayer()) {
                    offsetX = offsetZ = 0;
                }
                MMParticle.ORB.spawn(world, posX + ox + offsetX, posY + oy + 0.3, posZ + oz + offsetZ, ParticleArgs.get().withData(posX + offsetX, posY + 0.3, posZ + offsetZ, 10));
            }
        }
        if (ticksExisted > 20) {
            this.calculateEndPos();
            List<EntityLivingBase> hit = raytraceEntities(world, new Vec3d(posX, posY, posZ), new Vec3d(endPosX, endPosY, endPosZ), false, true, true).entities;
            if (blockSide != null) {
                spawnExplosionParticles(2);
            }
            if (!world.isRemote) {
                for (EntityLivingBase target : hit) {
                    if (caster instanceof EntityBarako && target instanceof LeaderSunstrikeImmune) {
                        continue;
                    }
                    float damageFire = 1.5f;
                    float damageMob = 3f;
                    if (caster instanceof EntityBarako) {
                        damageFire *= ConfigHandler.BARAKO.combatData.attackMultiplier;
                        damageMob *= ConfigHandler.BARAKO.combatData.attackMultiplier;
                    }
                    if (caster instanceof EntityPlayer) {
                        damageFire *= ConfigHandler.TOOLS_AND_ABILITIES.sunsBlessingAttackMultiplier;
                        damageMob *= ConfigHandler.TOOLS_AND_ABILITIES.sunsBlessingAttackMultiplier;
                    }
                    boolean hitWithFire = target.attackEntityFrom(DamageSource.ON_FIRE, damageFire);
                    if (hitWithFire) target.hurtResistantTime = 0;
                    target.attackEntityFrom(DamageSource.causeMobDamage(caster), damageMob);
                    if (hitWithFire) target.hurtResistantTime = target.maxHurtResistantTime;
                }
            } else {
                for (EntityLivingBase e : hit) {
                    if (e instanceof EntityWroughtnaut) {
                        MowziesMobs.PROXY.solarBeamHitWroughtnaught(caster);
                        break;
                    }
                }
                if (ticksExisted - 15 < getDuration()) {
                    int particleCount = 4;
                    while (particleCount --> 0) {
                        double radius = 1f;
                        double yaw = rand.nextFloat() * 2 * Math.PI;
                        double pitch = rand.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(getYaw()) * Math.cos(getPitch());
                        double o2y = -1 * Math.sin(getPitch());
                        double o2z = -1 * Math.sin(getYaw()) * Math.cos(getPitch());
                        MMParticle.ORB.spawn(world, posX + o2x + ox, posY + o2y + oy, posZ + o2z + oz, ParticleArgs.get().withData(collidePosX + o2x + ox, collidePosY + o2y + oy, collidePosZ + o2z + oz, 15));
                    }
                    particleCount = 4;
                    while (particleCount --> 0) {
                        double radius = 2f;
                        double yaw = rand.nextFloat() * 2 * Math.PI;
                        double pitch = rand.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(getYaw()) * Math.cos(getPitch());
                        double o2y = -1 * Math.sin(getPitch());
                        double o2z = -1 * Math.sin(getYaw()) * Math.cos(getPitch());
                        MMParticle.ORB.spawn(world, collidePosX + o2x, collidePosY + o2y, collidePosZ + o2z, ParticleArgs.get().withData(collidePosX + o2x + ox, collidePosY + o2y + oy, collidePosZ + o2z + oz, 20));
                    }
                }
            }
        }
        if (ticksExisted - 20 > getDuration()) {
            on = false;
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (rand.nextFloat() * 2 * Math.PI);
            float motionY = rand.nextFloat() * 0.08F;
            float motionX = velocity * MathHelper.cos(yaw);
            float motionZ = velocity * MathHelper.sin(yaw);
            world.spawnParticle(EnumParticleTypes.FLAME, collidePosX, collidePosY + 0.1, collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            world.spawnParticle(EnumParticleTypes.LAVA, collidePosX, collidePosY + 0.1, collidePosZ, 0, 0, 0);
        }
    }

    @Override
    protected void entityInit() {
        getDataManager().register(YAW, 0F);
        getDataManager().register(PITCH, 0F);
        getDataManager().register(DURATION, 0);
        getDataManager().register(HAS_PLAYER, false);
        getDataManager().register(CASTER, -1);
    }

    public float getYaw() {
        return getDataManager().get(YAW);
    }

    public void setYaw(float yaw) {
        getDataManager().set(YAW, yaw);
    }

    public float getPitch() {
        return getDataManager().get(PITCH);
    }

    public void setPitch(float pitch) {
        getDataManager().set(PITCH, pitch);
    }

    public int getDuration() {
        return getDataManager().get(DURATION);
    }

    public void setDuration(int duration) {
        getDataManager().set(DURATION, duration);
    }

    public boolean getHasPlayer() {
        return getDataManager().get(HAS_PLAYER);
    }

    public void setHasPlayer(boolean player) {
        getDataManager().set(HAS_PLAYER, player);
    }

    public int getCasterID() {
        return getDataManager().get(CASTER);
    }

    public void setCasterID(int id) {
        getDataManager().set(CASTER, id);
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {}

    private void calculateEndPos() {
        endPosX = posX + RADIUS * Math.cos(getYaw()) * Math.cos(getPitch());
        endPosZ = posZ + RADIUS * Math.sin(getYaw()) * Math.cos(getPitch());
        endPosY = posY + RADIUS * Math.sin(getPitch());
    }

    public HitResult raytraceEntities(World world, Vec3d from, Vec3d to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        HitResult result = new HitResult();
        result.setBlockHit(world.rayTraceBlocks(new Vec3d(from.x, from.y, from.z), to, stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock));
        if (result.blockHit != null) {
            collidePosX = result.blockHit.hitVec.x;
            collidePosY = result.blockHit.hitVec.y;
            collidePosZ = result.blockHit.hitVec.z;
            blockSide = result.getBlockHit().sideHit;
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(Math.min(posX, collidePosX), Math.min(posY, collidePosY), Math.min(posZ, collidePosZ), Math.max(posX, collidePosX), Math.max(posY, collidePosY), Math.max(posZ, collidePosZ)).grow(1, 1, 1));
        for (EntityLivingBase entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getCollisionBorderSize() + 0.5f;
            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(pad, pad, pad);
            RayTraceResult hit = aabb.calculateIntercept(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit != null) {
                result.addEntityHit(entity);
            }
        }
        return result;
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

    private void updateWithPlayer() {
        this.setYaw((float) ((caster.rotationYawHead + 90) * Math.PI / 180));
        this.setPitch((float) (-caster.rotationPitch * Math.PI / 180));
        this.setPosition(caster.posX, caster.posY + 1.2f, caster.posZ);
    }

    public static class HitResult {
        private RayTraceResult blockHit;

        private List<EntityLivingBase> entities = new ArrayList<>();

        public RayTraceResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(RayTraceResult blockHit) {
            this.blockHit = blockHit;
        }

        public void addEntityHit(EntityLivingBase entity) {
            entities.add(entity);
        }
    }
}
