package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Josh on 5/25/2017.
 */
public class EntityIceBreath extends Entity {
	private static final int RANGE = 10;
	private static final int ARC = 45;
	private static final int DAMAGE_PER_HIT = 1;
	private static final DataParameter<Integer> CASTER = EntityDataManager.createKey(EntityIceBreath.class, DataSerializers.VARINT);
	public EntityLivingBase caster;

	public EntityIceBreath(World world) {
		super(world);
		setSize(0, 0);
		if (world.isRemote) {
			MowziesMobs.PROXY.playIceBreathSound(this);
		}
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
		if (ticksExisted == 1) playSound(MMSounds.ENTITY_FROSTMAW_ICEBREATH_START, 1, 0.6f);
		if (caster instanceof EntityPlayer) {
			rotationYaw = ((EntityPlayer) caster).rotationYaw;
			rotationPitch = ((EntityPlayer) caster).rotationPitch;
			posX = ((EntityPlayer) caster).posX;
			posY = ((EntityPlayer) caster).posY + ((EntityPlayer) caster).eyeHeight - 0.5f;
			posZ = ((EntityPlayer) caster).posZ;
		}

		if (ticksExisted == 1) {
			caster = (EntityLivingBase) world.getEntityByID(getCasterID());
		}
		float yaw = (float) Math.toRadians(-rotationYaw);
		float pitch = (float) Math.toRadians(-rotationPitch);
		float spread = 0.25f;
		float speed = 0.56f;
		float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
		float yComp = (float) (Math.sin(pitch));
		float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
		if (world.isRemote) {
			if (ticksExisted % 8 == 0) {
				if (world.isRemote)
					MMParticle.RING.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(yaw, -pitch, 40, 1f, 1f, 1f, 1f, 110f * spread, false, 0.5f * xComp, 0.5f * yComp, 0.5f * zComp));
			}

			for (int i = 0; i < 6; i++) {
				double xSpeed = speed * 1f * xComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
				double ySpeed = speed * 1f * yComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
				double zSpeed = speed * 1f * zComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
				MMParticle.SNOWFLAKE.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 37d, 1d));
			}
			for (int i = 0; i < 5; i++) {
				double xSpeed = speed * xComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
				double ySpeed = speed * yComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
				double zSpeed = speed * zComp + (spread * 0.7 * (rand.nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
				double value = rand.nextFloat() * 0.15f;
				MMParticle.CLOUD.spawn(world, posX, posY, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, ySpeed, zSpeed, 0.75d + value, 0.75d + value, 1d, true, 10d + rand.nextDouble() * 20d, 40, ParticleCloud.EnumCloudBehavior.GROW));
			}
		}
		if (ticksExisted > 10) hitEntities();

		if (ticksExisted > 65 && !(caster instanceof EntityPlayer)) setDead();
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
				entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(caster, null), damage);
				MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entityHit, MowzieLivingProperties.class);
				property.freezeProgress += 0.12;
			}
		}
	}

	public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
		return getEntitiesNearby(EntityLivingBase.class, distanceX, distanceY, distanceZ, radius);
	}

	public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
		return world.getEntitiesWithinAABB(entityClass, getEntityBoundingBox().expand(dX, dY, dZ), e -> e != this && getDistance(e) <= r && e.posY <= posY + dY);
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
