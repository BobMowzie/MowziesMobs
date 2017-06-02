package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityRing;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 5/25/2017.
 */
public class EntityIceBreath extends Entity {
    private static final int RANGE = 10;
    private static final int ARC = 30;
    private static final int DAMAGE_PER_HIT = 2;
    public EntityLivingBase caster;
    private static final DataParameter<Integer> CASTER = EntityDataManager.createKey(EntityIceBreath.class, DataSerializers.VARINT);

    public EntityIceBreath(World world) {
        super(world);
        setSize(0, 0);
    }

    public EntityIceBreath(World world, EntityLivingBase caster) {
        super(world);
        setSize(0, 0);
        if (!world.isRemote) {
            this.setCasterID(caster.getEntityId());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted == 1) {
            caster = (EntityLivingBase) world.getEntityByID(getCasterID());
        }
        float yaw = (float) Math.toRadians(-rotationYaw);
        float pitch = (float) Math.toRadians(-rotationPitch);
        float spread = 0.5f;
        float speed = 0.7f;
        float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
        float yComp = (float) (Math.sin(pitch));
        float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
        if (ticksExisted % 4 == 0) {
            EntityRing ring = new EntityRing(world, (float)posX, (float)posY, (float)posZ, new Vec3d(xComp, yComp, zComp), 25, 0.8f, 0.8f, 1f, 1f, 10 * spread, false);
            if (world.isRemote) world.spawnEntity(ring);
            ring.motionX = 0.5 * xComp;
            ring.motionZ = 0.5 * zComp;
            ring.motionY = 0.5 * yComp;
        }

        for (int i = 0; i < 50; i++) {
            float xSpeed = speed * xComp + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
            float ySpeed = speed * yComp + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
            float zSpeed = speed * zComp + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
            world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
        }
        for (int i = 0; i < 50; i++) {
            float xSpeed = speed * xComp + (spread * 0.6f * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
            float ySpeed = speed * yComp + (spread * 0.6f * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
            float zSpeed = speed * zComp + (spread * 0.6f * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
            world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
        }
        if (ticksExisted > 10) hitEntities();

        if (ticksExisted > 65) setDead();
    }

    public void hitEntities() {
        List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(RANGE, RANGE, RANGE, RANGE);
        float damage = DAMAGE_PER_HIT;
        for (EntityLivingBase entityHit : entitiesHit) {
            if (entityHit == caster) continue;
            float entityHitYaw = (float) ((Math.atan2(entityHit.posZ - posZ, entityHit.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingYaw = rotationYaw % 360;
            if (entityHitYaw < 0) {
                entityHitYaw += 360;
            }
            if (entityAttackingYaw < 0) {
                entityAttackingYaw += 360;
            }
            float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

            float xzDistance = (float) Math.sqrt((entityHit.posZ - posZ) * (entityHit.posZ - posZ) + (entityHit.posX - posX) * (entityHit.posX - posX));
            float entityHitPitch = (float) ((Math.atan2((entityHit.posY - posY), xzDistance) * (180 / Math.PI)) % 360);
            float entityAttackingPitch = -rotationPitch % 360;
            if (entityHitPitch < 0) {
                entityHitPitch += 360;
            }
            if (entityAttackingPitch < 0) {
                entityAttackingPitch += 360;
            }
            float entityRelativePitch = entityHitPitch - entityAttackingPitch;

            float entityHitDistance = (float) Math.sqrt((entityHit.posZ - posZ) * (entityHit.posZ - posZ) + (entityHit.posX - posX) * (entityHit.posX - posX) + (entityHit.posY - posY) * (entityHit.posY - posY));

            boolean inRange = entityHitDistance <= RANGE;
            boolean yawCheck = (entityRelativeYaw <= ARC / 2 && entityRelativeYaw >= -ARC / 2) || (entityRelativeYaw >= 360 - ARC / 2 || entityRelativeYaw <= -360 + ARC / 2);
            boolean pitchCheck = (entityRelativePitch <= ARC / 2 && entityRelativePitch >= -ARC / 2) || (entityRelativePitch >= 360 - ARC / 2 || entityRelativePitch <= -360 + ARC / 2);
            if (inRange && yawCheck && pitchCheck) {
//                entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), damage);
                MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entityHit, MowzieLivingProperties.class);
                property.freezeProgress += 0.12;
            }
        }
    }

    public  List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(EntityLivingBase.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().expand(dX, dY, dZ), e -> e != this && getDistanceToEntity(e) <= r && e.posY <= posY + dY);
    }

    @Override
    protected void entityInit() {
        getDataManager().register(CASTER, -1);
    }

    public int getCasterID() {
        return getDataManager().get(CASTER);
    }

    public void setCasterID(int id) {
        getDataManager().set(CASTER, id);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
