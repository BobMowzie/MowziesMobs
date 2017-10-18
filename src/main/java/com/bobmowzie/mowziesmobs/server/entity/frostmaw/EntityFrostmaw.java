package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.MMParticle;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particles.ParticleCloud;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.entity.LegSolverQuadruped;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.base.Optional;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Josh on 5/8/2017.
 */
public class EntityFrostmaw extends MowzieEntity {
	public static final Animation DIE_ANIMATION = Animation.create(94);
	public static final Animation HURT_ANIMATION = Animation.create(0);
	public static final Animation ROAR_ANIMATION = Animation.create(76);
	public static final Animation SWIPE_ANIMATION = Animation.create(28);
	public static final Animation SWIPE_TWICE_ANIMATION = Animation.create(57);
	public static final Animation ICE_BREATH_ANIMATION = Animation.create(92);
	public static final Animation ACTIVATE_ANIMATION = Animation.create(118);
	public static final Animation ACTIVATE_NO_CRYSTAL_ANIMATION = Animation.create(100);
	public static final Animation DEACTIVATE_ANIMATION = Animation.create(25);
	public static final Animation DODGE_ANIMATION = Animation.create(15);
	public static final Animation LAND_ANIMATION = Animation.create(14);
	public static final Animation SLAM_ANIMATION = Animation.create(113);
	public static final int ICE_BREATH_COOLDOWN = 260;
	public static final int SLAM_COOLDOWN = 500;
	public static final int DODGE_COOLDOWN = 200;
	private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityFrostmaw.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_CRYSTAL = EntityDataManager.createKey(EntityFrostmaw.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> CRYSTAL = EntityDataManager.createKey(EntityFrostmaw.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	public EntityItem crystal;
	public EntityIceBreath iceBreath;

	public boolean swingWhichArm = false;
	public LegSolverQuadruped legSolver;
	private Vec3d prevRightHandPos = new Vec3d(0, 0, 0);
	private Vec3d prevLeftHandPos = new Vec3d(0, 0, 0);
	private int iceBreathCooldown = 0;
	private int slamCooldown = 0;
	private int timeWithoutTarget;
	private int shouldDodgeMeasure = 0;
	private int dodgeCooldown = 0;
	private boolean shouldDodge;
	private float dodgeYaw = 0;
	private boolean shouldPlayLandAnimation = false;

	public EntityFrostmaw(World world) {
		super(world);
		setSize(4f, 4f);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.tasks.addTask(2, new AnimationAreaAttackAI<>(this, SWIPE_ANIMATION, null, null, 2, 7, 6, 120, 1f, 9));
		this.tasks.addTask(2, new AnimationAreaAttackAI<>(this, SWIPE_TWICE_ANIMATION, null, null, 1, 7, 6, 120, 1f, 9));
		this.tasks.addTask(2, new AnimationAI<>(this, ICE_BREATH_ANIMATION, true));
		this.tasks.addTask(2, new AnimationAI<>(this, ROAR_ANIMATION, false));
		this.tasks.addTask(2, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
		this.tasks.addTask(2, new AnimationActivateAI<>(this, ACTIVATE_NO_CRYSTAL_ANIMATION));
		this.tasks.addTask(2, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
		this.tasks.addTask(2, new AnimationAI<>(this, LAND_ANIMATION, false));
		this.tasks.addTask(2, new AnimationAI<>(this, SLAM_ANIMATION, false));
		this.tasks.addTask(2, new AnimationAI<>(this, DODGE_ANIMATION, true));
		this.tasks.addTask(3, new AnimationTakeDamage<>(this));
		this.tasks.addTask(1, new AnimationDieAI<>(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, null));
		stepHeight = 1;
		frame += rand.nextInt(50);
		legSolver = new LegSolverQuadruped(1f, 2f, -1, 1.5f);
		socketPosArray = new Vec3d[]{new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 0)};
		active = false;
		playsHurtAnimation = false;
		rotationYaw = renderYawOffset = rand.nextFloat() * 360;
		experienceValue = 60;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(220.0D * MowziesMobs.CONFIG.difficultyScaleFrostmaw);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(9.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		getDataManager().register(ACTIVE, false);
		getDataManager().register(HAS_CRYSTAL, true);
		getDataManager().register(CRYSTAL, Optional.absent());
	}

	@Override
	public void playLivingSound() {
		if (!active) return;
		int i = rand.nextInt(4);
		super.playLivingSound();
		if (i == 0 && getAnimation() == NO_ANIMATION) {
			AnimationHandler.INSTANCE.sendAnimationMessage(this, ROAR_ANIMATION);
			return;
		}
		if (i < MMSounds.ENTITY_FROSTMAW_LIVING.length)
			playSound(MMSounds.ENTITY_FROSTMAW_LIVING[i], 2, 0.8f + rand.nextFloat() * 0.3f);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return super.getAmbientSound();
	}

	@Override
	public void onUpdate() {
		rotationYaw = renderYawOffset;
		super.onUpdate();
		this.repelEntities(3.8f, 3.8f, 3.8f, 3.8f);

		if (ticksExisted == 1) {
//            System.out.println("Frostmaw at " + getPosition());
			if (getHasCrystal()) {
				Optional<UUID> crystalID = getCrystalID();
				if (!getCrystalID().isPresent() && !world.isRemote && crystal == null && !getActive()) {
					crystal = dropItem(ItemHandler.INSTANCE.iceCrystal, 1);
					setCrystalID(Optional.of(crystal.getUniqueID()));
				}
				crystal = getCrystal();
				if (crystal != null) {
					crystal.setNoDespawn();
					crystal.setNoGravity(true);
				}
			}
		}

		if (!world.isRemote && crystal != null && crystal.isDead && !getActive() && getAnimation() != DEACTIVATE_ANIMATION) {
			setHasCrystal(false);
			setCrystalID(Optional.absent());
		}

		if (getActive() && getAnimation() != ACTIVATE_ANIMATION && getAnimation() != ACTIVATE_NO_CRYSTAL_ANIMATION) {
			legSolver.update(this);

			if (getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) {
				if (getAnimationTick() == 1) swingWhichArm = rand.nextBoolean();
				if (getAnimationTick() == 3) {
					int i = MathHelper.getInt(rand, 0, MMSounds.ENTITY_FROSTMAW_ATTACK.length);
					if (i < MMSounds.ENTITY_FROSTMAW_ATTACK.length) {
						playSound(MMSounds.ENTITY_FROSTMAW_ATTACK[i], 2, 0.9f + rand.nextFloat() * 0.2f);
					}
				}
			}

			if (getAnimation() == SWIPE_ANIMATION) {
				if (getAnimationTick() == 6) {
					playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.8f);
				}
			}

			if (getAnimation() == SWIPE_TWICE_ANIMATION && currentAnim instanceof AnimationAreaAttackAI<?>) {
				if (getAnimationTick() == 21) {
					((AnimationAreaAttackAI<?>) currentAnim).hitEntities();
				}
				if (getAnimationTick() == 16) {
					playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.7f);
				}
				if (getAnimationTick() == 6) {
					playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.8f);
				}
			}

			if (getAnimation() == ROAR_ANIMATION) {
				if (getAnimationTick() == 10) {
					playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
				}
				if (getAnimationTick() >= 8 && getAnimationTick() < 65) {
					doRoarEffects();
				}
			}

			if (getAnimation() == LAND_ANIMATION) {
				if (getAnimationTick() == 3) {
					playSound(MMSounds.ENTITY_FROSTMAW_LAND, 3, 0.9f);
				}
			}

			if (getAnimation() == SLAM_ANIMATION) {
				if (getAnimationTick() == 82) {
					playSound(MMSounds.ENTITY_FROSTMAW_LIVING_1, 2, 1);
				}
				if (getAttackTarget() != null) getLookHelper().setLookPositionWithEntity(getAttackTarget(), 30, 30);
				if (getAnimationTick() == 82) {
					int i = MathHelper.getInt(rand, 0, MMSounds.ENTITY_FROSTMAW_ATTACK.length - 1);
					if (i < MMSounds.ENTITY_FROSTMAW_ATTACK.length) {
						playSound(MMSounds.ENTITY_FROSTMAW_ATTACK[i], 2, 0.9f + rand.nextFloat() * 0.2f);
					}
					playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.7f);
				}
				if (getAnimationTick() == 87) {
					playSound(MMSounds.ENTITY_FROSTMAW_LAND, 3, 1f);
					float radius = 4;
					float slamPosX = (float) (posX + radius * Math.cos(Math.toRadians(rotationYaw + 90)));
					float slamPosZ = (float) (posZ + radius * Math.sin(Math.toRadians(rotationYaw + 90)));
					if (world.isRemote)
						MMParticle.RING.spawn(world, slamPosX, posY + 0.2f, slamPosZ, ParticleFactory.ParticleArgs.get().withData(0f, (float) Math.PI / 2f, 17, 1f, 1f, 1f, 1f, 60f, false, 0f, 0f, 0f));
					AxisAlignedBB hitBox = new AxisAlignedBB(new BlockPos(slamPosX - 0.5f, posY, slamPosZ - 0.5f)).expand(3, 3, 3);
					List<EntityLivingBase> entitiesHit = world.getEntitiesWithinAABB(EntityLivingBase.class, hitBox);
					for (EntityLivingBase entity : entitiesHit) {
						if (entity != this) attackEntityAsMob(entity, 4f);
					}
				}
			}
			if (getAnimation() == DODGE_ANIMATION && !world.isRemote) {
				getNavigator().clearPath();
				if (getAnimationTick() == 2) {
					dodgeYaw = (float) Math.toRadians(targetAngle + 90 + rand.nextFloat() * 150 - 75);
				}
				if (getAnimationTick() == 6 && onGround) {
					motionY = 0.6;
					float speed = 1.7f;
					motionX += (float) (speed * Math.cos(dodgeYaw));
					motionZ += (float) (speed * Math.sin(dodgeYaw));
				}
				if (getAttackTarget() != null) getLookHelper().setLookPositionWithEntity(getAttackTarget(), 30, 30);
			}

			if (getAnimation() == ICE_BREATH_ANIMATION) {
				if (getAttackTarget() != null) getLookHelper().setLookPositionWithEntity(getAttackTarget(), 15, 15);
				Vec3d mouthPos = socketPosArray[2];
				if (getAnimationTick() == 13) {
					iceBreath = new EntityIceBreath(world, this);
					iceBreath.setPositionAndRotation(mouthPos.x, mouthPos.y, mouthPos.z, rotationYawHead, rotationPitch + 10);
					if (!world.isRemote) world.spawnEntity(iceBreath);
				}
				if (iceBreath != null)
					iceBreath.setPositionAndRotation(mouthPos.x, mouthPos.y, mouthPos.z, rotationYawHead, rotationPitch + 10);
			}

			spawnSwipeParticles();

			if (getAttackTarget() != null) {
				timeWithoutTarget = 0;

				float entityHitAngle = (float) ((Math.atan2(getAttackTarget().posZ - posZ, getAttackTarget().posX - posX) * (180 / Math.PI) - 90) % 360);
				float entityAttackingAngle = renderYawOffset % 360;
				if (entityHitAngle < 0) {
					entityHitAngle += 360;
				}
				if (entityAttackingAngle < 0) {
					entityAttackingAngle += 360;
				}
				float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
				if (getNavigator().noPath() && !((entityRelativeAngle <= 30 / 2 && entityRelativeAngle >= -30 / 2) || (entityRelativeAngle >= 360 - 30 / 2 || entityRelativeAngle <= -360 + 30 / 2))) {
					getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.85);
				}

				if (shouldDodgeMeasure >= 14) shouldDodge = true;
				if (targetDistance < 6 && shouldDodge && getAnimation() == NO_ANIMATION) {
					shouldDodge = false;
					dodgeCooldown = DODGE_COOLDOWN;
					shouldDodgeMeasure = 0;
					AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
				}
				if (targetDistance > 6 && !(getAnimation() == ICE_BREATH_ANIMATION && targetDistance < 8) && onGround) {
					if (getAnimation() != SLAM_ANIMATION) getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1);
					else getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.95);
				} else getNavigator().clearPath();
				if (targetDistance <= 9 && getAnimation() == NO_ANIMATION && slamCooldown <= 0 && rand.nextInt(4) == 0 && !getAttackTarget().isPotionActive(PotionHandler.INSTANCE.frozen) && getHealth() / getMaxHealth() < 0.6) {
					AnimationHandler.INSTANCE.sendAnimationMessage(this, SLAM_ANIMATION);
					slamCooldown = SLAM_COOLDOWN;
				}
				if (targetDistance <= 7 && getAnimation() == NO_ANIMATION) {
					if (rand.nextInt(4) == 0)
						AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_TWICE_ANIMATION);
					else AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_ANIMATION);
				}
				if (targetDistance >= 4 && targetDistance <= 14 && getAnimation() == NO_ANIMATION && iceBreathCooldown <= 0 && getHasCrystal()) {
					AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BREATH_ANIMATION);
					iceBreathCooldown = ICE_BREATH_COOLDOWN;
				}
			} else if (!world.isRemote) {
				timeWithoutTarget++;
				if (timeWithoutTarget > 1200) {
					timeWithoutTarget = 0;
					if (getAnimation() == NO_ANIMATION) {
						AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
						setActive(false);
					}
				}
			}
		} else {
			getNavigator().clearPath();
			renderYawOffset = prevRenderYawOffset;
			addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, 1, true, true));
			if (getAttackTarget() != null && getAttackTarget().isPotionActive(MobEffects.INVISIBILITY)) {
				setAttackTarget(null);
			}
			if (!getAttackableEntityLivingBaseNearby(8, 8, 8, 8).isEmpty() && getAttackTarget() != null && getAnimation() == NO_ANIMATION) {
				if (getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
				else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
				setActive(true);
			}

//            renderYawOffset++;
			if (!world.isRemote && crystal != null) {
				Vec3d rightHandPos = socketPosArray[3];
				crystal.setNoGravity(true);
				crystal.motionX = 0;
				crystal.motionY = 0;
				crystal.motionZ = 0;
				crystal.setPosition(rightHandPos.x, rightHandPos.y + 0.1, rightHandPos.z);
			}
		}

		if (getAnimation() == ACTIVATE_ANIMATION || getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION) {
			if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP, 1, 1);
			if (getAnimation() == ACTIVATE_ANIMATION && getAnimationTick() == 18)
				playSound(MMSounds.ENTITY_FROSTMAW_ATTACK[0], 1.5f, 1);
			if (!world.isRemote && crystal != null) {
				crystal.setDead();
				setCrystalID(Optional.absent());
			}
			if ((getAnimation() == ACTIVATE_ANIMATION && getAnimationTick() == 52) || (getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION && getAnimationTick() == 34)) {
				playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
			}
			if ((getAnimation() == ACTIVATE_ANIMATION && getAnimationTick() >= 51 && getAnimationTick() < 108) || (getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION && getAnimationTick() >= 33 && getAnimationTick() < 90)) {
				doRoarEffects();
			}
		}

		if (getAnimation() == DEACTIVATE_ANIMATION) {
			if (getAnimationTick() == 10) {
				if (getHasCrystal()) {
					Optional<UUID> crystalID = getCrystalID();
					if (!getCrystalID().isPresent() && !world.isRemote && (crystal == null || crystal.isDead)) {
						crystal = dropItem(ItemHandler.INSTANCE.iceCrystal, 1);
						setCrystalID(Optional.of(crystal.getUniqueID()));
					}
					crystal = getCrystal();
					if (crystal != null) {
						crystal.setNoDespawn();
						crystal.setNoGravity(true);
					}
				}
			}
		}

		//Footstep Sounds
		float moveX = (float) (posX - prevPosX);
		float moveZ = (float) (posZ - prevPosZ);
		float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
		if (frame % 16 == 5 && speed > 0.05 && active) {
			playSound(MMSounds.ENTITY_FROSTMAW_STEP, 3F, 0.8F + rand.nextFloat() * 0.2f);
		}

		//Breathing sounds
		if (frame % 118 == 1 && !active) {
			int i = MathHelper.getInt(rand, 0, 1);
			playSound(MMSounds.ENTITY_FROSTMAW_BREATH[i], 1.5F, 1.1F + rand.nextFloat() * 0.1f);
		}

		if ((fallDistance > 0.2 && !onGround) || getAnimation() == DODGE_ANIMATION) shouldPlayLandAnimation = true;
		if (onGround && shouldPlayLandAnimation && getAnimation() != DODGE_ANIMATION) {
			if (!world.isRemote && getAnimation() == NO_ANIMATION) {
				AnimationHandler.INSTANCE.sendAnimationMessage(this, LAND_ANIMATION);
			}
			shouldPlayLandAnimation = false;
		}

//        if (getAnimation() == NO_ANIMATION && onGround) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, ROAR_ANIMATION);
//            setActive(true);
//        }

		if (iceBreathCooldown > 0) iceBreathCooldown--;
		if (slamCooldown > 0) slamCooldown--;
		if (shouldDodgeMeasure > 0 && ticksExisted % 7 == 0) shouldDodgeMeasure--;
		if (dodgeCooldown > 0) slamCooldown--;
		prevRotationYaw = rotationYaw;
	}

	private void doRoarEffects() {
		List<EntityLivingBase> entities = getEntityLivingBaseNearby(10, 3, 10, 10);
		for (EntityLivingBase entity : entities) {
			double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
			double distance = getDistance(entity) - 4;
			entity.motionX += Math.min(1 / (distance * distance), 1) * -1 * Math.cos(angle);
			entity.motionZ += Math.min(1 / (distance * distance), 1) * -1 * Math.sin(angle);
		}
		if (getAnimationTick() % 12 == 0 && world.isRemote) {
			int particleCount = 15;
			for (int i = 1; i <= particleCount; i++) {
				double yaw = i * 360 / particleCount;
				double speed = 0.9;
				double xSpeed = speed * Math.cos(Math.toRadians(yaw));
				double zSpeed = speed * Math.sin(Math.toRadians(yaw));
				MMParticle.CLOUD.spawn(world, posX, posY + 1f, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, 0d, zSpeed, 0.75d, 0.75d, 1d, true, 40d, 22, ParticleCloud.EnumCloudBehavior.GROW));
			}
			for (int i = 1; i <= particleCount; i++) {
				double yaw = i * 360 / particleCount;
				double speed = 0.65;
				double xSpeed = speed * Math.cos(Math.toRadians(yaw));
				double zSpeed = speed * Math.sin(Math.toRadians(yaw));
				MMParticle.CLOUD.spawn(world, posX, posY + 1f, posZ, ParticleFactory.ParticleArgs.get().withData(xSpeed, 0d, zSpeed, 0.75d, 0.75d, 1d, true, 35d, 22, ParticleCloud.EnumCloudBehavior.GROW));
			}
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		List<EntityLivingBase> nearby = getEntityLivingBaseNearby(20, 4, 20, 20);
		for (EntityLivingBase nearbyEntity : nearby) {
			if (nearbyEntity instanceof EntityFrostmaw || nearbyEntity instanceof EntityVillager) {
				return false;
			}
		}
		return super.getCanSpawnHere();
	}

	private void spawnSwipeParticles() {
		if (world.isRemote) {
			int snowflakeDensity = 4;
			float snowflakeRandomness = 0.5f;
			int cloudDensity = 2;
			float cloudRandomness = 0.5f;
			if (getAnimation() == SWIPE_ANIMATION || getAnimation() == SWIPE_TWICE_ANIMATION) {
				Vec3d rightHandPos = socketPosArray[0];
				Vec3d leftHandPos = socketPosArray[1];
				if (getAnimation() == SWIPE_ANIMATION) {
					if (getAnimationTick() > 8 && getAnimationTick() < 14) {
						if (swingWhichArm) {
							double length = prevRightHandPos.subtract(rightHandPos).lengthVector();
							int numClouds = (int) Math.floor(2 * length);
							for (int i = 0; i < numClouds; i++) {
								double x = prevRightHandPos.x + i * (rightHandPos.x - prevRightHandPos.x) / numClouds;
								double y = prevRightHandPos.y + i * (rightHandPos.y - prevRightHandPos.y) / numClouds;
								double z = prevRightHandPos.z + i * (rightHandPos.z - prevRightHandPos.z) / numClouds;
								for (int j = 0; j < snowflakeDensity; j++) {
									float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
									float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
									float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
									MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
								}
								for (int j = 0; j < cloudDensity; j++) {
									float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
									float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
									float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
									double value = rand.nextFloat() * 0.1f;
									MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
								}
							}
						} else {
							double length = prevLeftHandPos.subtract(leftHandPos).lengthVector();
							int numClouds = (int) Math.floor(2.5 * length);
							for (int i = 0; i < numClouds; i++) {
								double x = prevLeftHandPos.x + i * (leftHandPos.x - prevLeftHandPos.x) / numClouds;
								double y = prevLeftHandPos.y + i * (leftHandPos.y - prevLeftHandPos.y) / numClouds;
								double z = prevLeftHandPos.z + i * (leftHandPos.z - prevLeftHandPos.z) / numClouds;
								for (int j = 0; j < snowflakeDensity; j++) {
									float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
									float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
									float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
									MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
								}
								for (int j = 0; j < cloudDensity; j++) {
									float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
									float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
									float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
									double value = rand.nextFloat() * 0.1f;
									MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
								}
							}
						}
					}
				} else {
					if ((swingWhichArm && getAnimationTick() > 8 && getAnimationTick() < 14) || (!swingWhichArm && getAnimationTick() > 19 && getAnimationTick() < 25)) {
						double length = prevRightHandPos.subtract(rightHandPos).lengthVector();
						int numClouds = (int) Math.floor(2 * length);
						for (int i = 0; i < numClouds; i++) {
							double x = prevRightHandPos.x + i * (rightHandPos.x - prevRightHandPos.x) / numClouds;
							double y = prevRightHandPos.y + i * (rightHandPos.y - prevRightHandPos.y) / numClouds;
							double z = prevRightHandPos.z + i * (rightHandPos.z - prevRightHandPos.z) / numClouds;
							for (int j = 0; j < snowflakeDensity; j++) {
								float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
								float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
								float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
								MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
							}
							for (int j = 0; j < cloudDensity; j++) {
								float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
								float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
								float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
								double value = rand.nextFloat() * 0.1f;
								MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
							}
						}
					} else if ((!swingWhichArm && getAnimationTick() > 8 && getAnimationTick() < 14) || (swingWhichArm && getAnimationTick() > 19 && getAnimationTick() < 25)) {
						double length = prevLeftHandPos.subtract(leftHandPos).lengthVector();
						int numClouds = (int) Math.floor(2.5 * length);
						for (int i = 0; i < numClouds; i++) {
							double x = prevLeftHandPos.x + i * (leftHandPos.x - prevLeftHandPos.x) / numClouds;
							double y = prevLeftHandPos.y + i * (leftHandPos.y - prevLeftHandPos.y) / numClouds;
							double z = prevLeftHandPos.z + i * (leftHandPos.z - prevLeftHandPos.z) / numClouds;
							for (int j = 0; j < snowflakeDensity; j++) {
								float xOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
								float yOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
								float zOffset = snowflakeRandomness * (2 * rand.nextFloat() - 1);
								MMParticle.SNOWFLAKE.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY - 0.01f, motionZ));
							}
							for (int j = 0; j < cloudDensity; j++) {
								float xOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
								float yOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
								float zOffset = cloudRandomness * (2 * rand.nextFloat() - 1);
								double value = rand.nextFloat() * 0.1f;
								MMParticle.CLOUD.spawn(world, x + xOffset, y + yOffset, z + zOffset, ParticleFactory.ParticleArgs.get().withData(motionX, motionY, motionZ, 0.8d + value, 0.8d + value, 1d, true, 10d + rand.nextDouble() * 10d, 40, ParticleCloud.EnumCloudBehavior.SHRINK));
							}
						}
					}
				}
				prevLeftHandPos = leftHandPos;
				prevRightHandPos = rightHandPos;
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		if (source == DamageSource.FALL) return false;
		boolean attack = super.attackEntityFrom(source, damage);
		if (attack) {
			shouldDodgeMeasure += damage;
			if (source.getEntity() != null && source.getEntity() instanceof EntityLivingBase && (!(source.getEntity() instanceof EntityPlayer) || !((EntityPlayer) source.getEntity()).capabilities.isCreativeMode))
				setAttackTarget((EntityLivingBase) source.getEntity());
			if (!getActive()) {
				if (getAnimation() != DIE_ANIMATION) {
					if (getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
					else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
				}
				setActive(true);
			}
		}
		return attack;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
						this.world.setEntityState(entityplayer, (byte) 30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	public boolean attackEntityAsMob(Entity entityIn, float damageMultiplier) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * damageMultiplier;
		int i = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
						this.world.setEntityState(entityplayer, (byte) 30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		if (getAnimationTick() == 5) playSound(MMSounds.ENTITY_FROSTMAW_DIE, 2.5f, 1);
		if (getAnimationTick() == 53) playSound(MMSounds.ENTITY_FROSTMAW_LAND, 2.5f, 1);
	}

	@Override
	public Animation getDeathAnimation() {
		return DIE_ANIMATION;
	}

	@Override
	public Animation getHurtAnimation() {
		return HURT_ANIMATION;
	}

	public void setActive(boolean active) {
		getDataManager().set(ACTIVE, active);
	}

	public boolean getActive() {
		this.active = getDataManager().get(ACTIVE);
		return active;
	}

	public void setHasCrystal(boolean hasCrystal) {
		getDataManager().set(HAS_CRYSTAL, hasCrystal);
	}

	public boolean getHasCrystal() {
		return getDataManager().get(HAS_CRYSTAL);
	}

	public void setCrystalID(Optional<UUID> crystalID) {
		getDataManager().set(CRYSTAL, crystalID);
	}

	public Optional<UUID> getCrystalID() {
		return getDataManager().get(CRYSTAL);
	}

	public EntityItem getCrystal() {
		Optional<UUID> uuid = getCrystalID();
		if (uuid.isPresent()) {
			List<EntityItem> potentialCrystals = world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(32, 32, 32));
			for (EntityItem entity : potentialCrystals) {
				if (uuid.get().equals(entity.getUniqueID())) {
					return entity;
				}
			}
		}
		return null;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ROAR_ANIMATION, SWIPE_ANIMATION, SWIPE_TWICE_ANIMATION, ICE_BREATH_ANIMATION, ACTIVATE_ANIMATION, ACTIVATE_NO_CRYSTAL_ANIMATION, DEACTIVATE_ANIMATION, SLAM_ANIMATION, LAND_ANIMATION, DODGE_ANIMATION};
	}

	@Override
	public void setDead() {
		super.setDead();
		if (crystal != null) crystal.setDead();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setHasCrystal(compound.getBoolean("has_crystal"));
		setActive(compound.getBoolean("active"));
		String uuid = compound.getString("crystalUUID");
		if (uuid.isEmpty()) {
			setCrystalID(Optional.absent());
		} else {
			setCrystalID(Optional.of(UUID.fromString(uuid)));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("has_crystal", getHasCrystal());
		compound.setBoolean("active", getActive());
		Optional<UUID> crystalUUID = getCrystalID();
		if (crystalUUID.isPresent()) {
			compound.setString("crystalUUID", crystalUUID.get().toString());
		}
	}

	@Override
	protected void dropLoot() {
		super.dropLoot();
		if (getHasCrystal()) dropItem(ItemHandler.INSTANCE.iceCrystal, 1);
	}
}
